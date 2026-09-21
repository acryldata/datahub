#!/usr/bin/env bash
# next-version.sh
#
# Determines the next release version from existing git tags.
# Ported from the release.sh monolith version calculation logic.
#
# Usage:
#   next-version.sh [rc|stable] [fourth|patch|minor|major]
#
# Defaults:
#   - release_type: rc
#   - bump_type:    fourth  (unless latest tag is already an RC, in which case bumps RC number)
#
# Output: a single line with the next version, e.g. "v1.5.0.8rc1"
#
# Prerequisites:
#   - git with tags fetched (run: git fetch origin --tags --quiet)
#   - gh CLI authenticated (used as fallback for release listing)

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
# REPO_ROOT can be overridden (e.g. by tests) to point at a sandbox repo. Defaults
# to the project root inferred from this script's install location.
REPO_ROOT="${REPO_ROOT:-$(cd "${SCRIPT_DIR}/../../.." && pwd)}"
cd "$REPO_ROOT"

RELEASE_TYPE="${1:-rc}"
BUMP_TYPE="${2:-fourth}"

if [[ "$RELEASE_TYPE" != "rc" && "$RELEASE_TYPE" != "stable" ]]; then
    echo "Error: release_type must be 'rc' or 'stable', got: $RELEASE_TYPE" >&2
    exit 1
fi
if [[ "$BUMP_TYPE" != "fourth" && "$BUMP_TYPE" != "patch" && "$BUMP_TYPE" != "minor" && "$BUMP_TYPE" != "major" ]]; then
    echo "Error: bump_type must be 'fourth', 'patch', 'minor', or 'major', got: $BUMP_TYPE" >&2
    exit 1
fi

# ── version helpers ───────────────────────────────────────────────────────────

# Produces a zero-padded sortable key; stable versions rank above RC of same base.
_version_sort_key() {
    local v="${1#v}"
    local major=0 minor=0 patch=0 fourth=0 rc=99999
    if   [[ $v =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)\.([0-9]+)rc([0-9]+)$ ]]; then
        major=${BASH_REMATCH[1]}; minor=${BASH_REMATCH[2]}; patch=${BASH_REMATCH[3]}
        fourth=${BASH_REMATCH[4]}; rc=${BASH_REMATCH[5]}
    elif [[ $v =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)\.([0-9]+)$ ]]; then
        major=${BASH_REMATCH[1]}; minor=${BASH_REMATCH[2]}; patch=${BASH_REMATCH[3]}
        fourth=${BASH_REMATCH[4]}
    elif [[ $v =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)rc([0-9]+)$ ]]; then
        major=${BASH_REMATCH[1]}; minor=${BASH_REMATCH[2]}; patch=${BASH_REMATCH[3]}; rc=${BASH_REMATCH[4]}
    elif [[ $v =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)-rc([0-9]+)$ ]]; then
        major=${BASH_REMATCH[1]}; minor=${BASH_REMATCH[2]}; patch=${BASH_REMATCH[3]}; rc=${BASH_REMATCH[4]}
    elif [[ $v =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)$ ]]; then
        major=${BASH_REMATCH[1]}; minor=${BASH_REMATCH[2]}; patch=${BASH_REMATCH[3]}
    fi
    printf "%010d%010d%010d%010d%010d" "$major" "$minor" "$patch" "$fourth" "$rc"
}

_sort_tags() {
    while IFS= read -r tag; do
        [[ $tag =~ ^v[0-9] ]] && echo "$(_version_sort_key "$tag") $tag"
    done | sort -rn | awk '{print $2}'
}

_parse_version() {
    local v="${1#v}"
    if   [[ $v =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)\.([0-9]+)rc([0-9]+)$ ]]; then
        echo "${BASH_REMATCH[1]} ${BASH_REMATCH[2]} ${BASH_REMATCH[3]} ${BASH_REMATCH[4]} ${BASH_REMATCH[5]}"
    elif [[ $v =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)\.([0-9]+)$ ]]; then
        echo "${BASH_REMATCH[1]} ${BASH_REMATCH[2]} ${BASH_REMATCH[3]} ${BASH_REMATCH[4]} 0"
    elif [[ $v =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)-rc([0-9]+)$ ]]; then
        echo "${BASH_REMATCH[1]} ${BASH_REMATCH[2]} ${BASH_REMATCH[3]} 0 ${BASH_REMATCH[4]}"
    elif [[ $v =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)rc([0-9]+)$ ]]; then
        echo "${BASH_REMATCH[1]} ${BASH_REMATCH[2]} ${BASH_REMATCH[3]} 0 ${BASH_REMATCH[4]}"
    elif [[ $v =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)$ ]]; then
        echo "${BASH_REMATCH[1]} ${BASH_REMATCH[2]} ${BASH_REMATCH[3]} 0 0"
    else
        echo "Error: cannot parse version: $1" >&2; exit 1
    fi
}

_is_rc() { [[ "$1" =~ rc[0-9]+$ ]]; }

# Tags that exist on ORIGIN (acryldata/datahub) — the only ones this fork
# releases from. Local `git tag -l` is NOT a safe substitute: prep Step 1 runs
# compare-upstream.sh, which does `git fetch <upstream> master`, and git
# auto-follows tags reachable from the fetched ref. datahub-project/datahub's
# own release tags therefore land in the same local tag namespace on every run.
# Upstream numbers some lines with three segments (v1.8.0rc3) where the fork
# uses four (v1.7.0.11), so an upstream tag can outrank every fork tag and
# hijack the version calculation. Asking origin directly is the only way to
# tell the two apart. Cached so repeated calls cost one network round-trip.
_ORIGIN_TAGS_CACHE=""
_origin_tags() {
    if [ -n "$_ORIGIN_TAGS_CACHE" ]; then
        printf '%s\n' "$_ORIGIN_TAGS_CACHE"
        return 0
    fi
    local out
    if out=$(git ls-remote --tags --refs origin 'v*' 2>/dev/null) && [ -n "$out" ]; then
        _ORIGIN_TAGS_CACHE=$(printf '%s\n' "$out" | awk -F'refs/tags/' 'NF>1 {print $2}')
    else
        # Offline or origin unreachable. Fall back to local tags so the script
        # still works, but say so — the fallback is exactly the contaminated
        # namespace described above.
        echo "Warning: could not list tags on origin; falling back to local tags," >&2
        echo "         which may include upstream OSS tags and skew the version." >&2
        _ORIGIN_TAGS_CACHE=$(git tag -l 'v*')
    fi
    printf '%s\n' "$_ORIGIN_TAGS_CACHE"
}

_get_latest_tag() {
    local tag
    tag=$(_origin_tags | _sort_tags | head -n 1)
    if [ -z "$tag" ] && command -v gh &>/dev/null && gh auth token &>/dev/null; then
        tag=$(gh release list --repo acryldata/datahub --limit 100 --json tagName \
            --jq '.[].tagName' 2>/dev/null | _sort_tags | head -n 1)
    fi
    echo "${tag:-v0.1.0.0}"
}

_get_latest_stable_tag() {
    local tag
    tag=$(_origin_tags | grep -v 'rc' | _sort_tags | head -n 1)
    if [ -z "$tag" ] && command -v gh &>/dev/null && gh auth token &>/dev/null; then
        tag=$(gh release list --repo acryldata/datahub --limit 100 --json tagName \
            --jq '.[].tagName' 2>/dev/null | grep -v 'rc' | _sort_tags | head -n 1)
    fi
    echo "${tag:-v0.1.0.0}"
}

# ── main logic ────────────────────────────────────────────────────────────────

"$SCRIPT_DIR/safe-fetch-tags.sh" || \
    echo "Warning: tag fetch failed — version may be based on stale local tags" >&2

LATEST=$(_get_latest_tag)

# ── stale-RC guard ────────────────────────────────────────────────────────────
# _get_latest_tag ranks tags by version number, with no notion of recency. An
# abandoned RC line on a higher minor therefore outranks the live release train
# and silently hijacks auto-detection: a leftover v1.8.0rc3 beat the active
# v1.7.0.11 stable and produced v1.8.0rc4 on top of a 1.7.0.x train. The tags in
# that case were local-only, so "does it exist on the remote" was the tell — but
# checking that needs the network. Creation date is the offline equivalent: a
# genuinely in-flight RC is NEWER than the latest stable, a parked one is older.
_tag_field() { git for-each-ref --format="%(creatordate:$2)" "refs/tags/$1" 2>/dev/null; }

if _is_rc "$LATEST" && [ "${OSS_RELEASE_ALLOW_STALE_RC:-}" != "true" ]; then
    _stable=$(_get_latest_stable_tag)
    _rc_ts=$(_tag_field "$LATEST" unix)
    _stable_ts=$(_tag_field "$_stable" unix)
    if [ -n "$_rc_ts" ] && [ -n "$_stable_ts" ] && [ "$_stable_ts" -gt "$_rc_ts" ]; then
        cat >&2 <<STALE
ERROR: stale release-candidate line detected — refusing to guess.

  Highest-ranked tag : $LATEST (created $(_tag_field "$LATEST" short))
  Latest stable tag  : $_stable (created $(_tag_field "$_stable" short))

  The stable tag is NEWER than the highest-ranked RC, so '$LATEST' is very likely
  an abandoned or local-only line. Bumping it would cut the next RC on top of the
  live '$_stable' train instead of continuing it.

  Check whether that RC line is real:
      git ls-remote --tags origin '$LATEST'
      gh release view $LATEST --repo acryldata/datahub

  If it is dead, delete the stale tag(s) locally and on the remote, then re-run.
  To continue that RC line deliberately, re-run with:
      OSS_RELEASE_ALLOW_STALE_RC=true \$0 $*
STALE
        exit 3
    fi
fi

# Auto-mode: if no explicit args and latest tag is already an RC, just bump the RC number
if [ $# -eq 0 ] && _is_rc "$LATEST"; then
    read -r major minor patch fourth rc <<< "$(_parse_version "$LATEST")"
    rc=$((rc + 1))
    if [ "$fourth" -eq 0 ]; then
        echo "v${major}.${minor}.${patch}rc${rc}"
    else
        echo "v${major}.${minor}.${patch}.${fourth}rc${rc}"
    fi
    exit 0
fi

# Stable-promotion shortcut: `stable` with no explicit bump and the latest tag
# is an RC means "finalize this RC" — strip the rcN suffix, don't bump.
# This is what `finish` uses to derive the stable version from LATEST_RC.
if [ "$RELEASE_TYPE" = "stable" ] && [ $# -le 1 ] && _is_rc "$LATEST"; then
    read -r major minor patch fourth rc <<< "$(_parse_version "$LATEST")"
    if [ "$fourth" -eq 0 ]; then
        echo "v${major}.${minor}.${patch}"
    else
        echo "v${major}.${minor}.${patch}.${fourth}"
    fi
    exit 0
fi

# For stable releases with an explicit bump (or when no RC exists), base the
# bump on the latest STABLE tag (skip RCs).
if [ "$RELEASE_TYPE" = "stable" ]; then
    BASE=$(_get_latest_stable_tag)
else
    BASE="$LATEST"
fi

read -r major minor patch fourth rc <<< "$(_parse_version "$BASE")"

if [ "$RELEASE_TYPE" = "rc" ]; then
    if [ "$rc" -gt 0 ]; then
        # Base is already an RC — increment RC number, no version component change
        rc=$((rc + 1))
    else
        # New RC cycle — bump according to BUMP_TYPE
        case "$BUMP_TYPE" in
            major) major=$((major + 1)); minor=0; patch=0; fourth=0 ;;
            minor) minor=$((minor + 1)); patch=0; fourth=0 ;;
            patch) patch=$((patch + 1)); fourth=0 ;;
            fourth) fourth=$((fourth + 1)) ;;
        esac
        rc=1
    fi
    if [ "$BUMP_TYPE" = "major" ]; then
        echo "v${major}.${minor}.${patch}rc${rc}"
    else
        echo "v${major}.${minor}.${patch}.${fourth}rc${rc}"
    fi
else
    # Stable release — same bump semantics as RC, just without the rcN suffix.
    # Keeps RC→stable correspondence (e.g. v2.0.0rc1 → v2.0.0, v1.6.0.0rc1 → v1.6.0.0).
    case "$BUMP_TYPE" in
        major) major=$((major + 1)); minor=0; patch=0; echo "v${major}.${minor}.${patch}" ;;
        minor) minor=$((minor + 1)); patch=0; fourth=0; echo "v${major}.${minor}.${patch}.${fourth}" ;;
        patch) patch=$((patch + 1)); fourth=0; echo "v${major}.${minor}.${patch}.${fourth}" ;;
        fourth) fourth=$((fourth + 1)); echo "v${major}.${minor}.${patch}.${fourth}" ;;
    esac
fi
