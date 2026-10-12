"""Emit the acryl-datahub final releases from roughly the last year.

Queries PyPI, keeps only final versions (``X.Y.Z`` or ``X.Y.Z.W``, all numeric —
no rc/post/dev), drops yanked releases, filters to uploads within ``--days``
(default 365), and prints a JSON array (newest first). Used both to build the
docsbase generation matrix and to compute the retention window each publish
prunes to.
"""

import argparse
import datetime as dt
import json
import re
import sys
import time
import urllib.error
import urllib.request
from typing import Any, Optional

_PYPI = "https://pypi.org/pypi/acryl-datahub/json"
_FINAL_RELEASE = re.compile(r"^\d+(\.\d+){2,3}$")


def _fetch(url: str, attempts: int = 3) -> Any:
    # This runs twice per publish — the second call is in assemble-and-publish
    # after every generate leg, where a transient blip would discard tens of
    # job-minutes — so retry a few times before giving up.
    for attempt in range(1, attempts + 1):
        try:
            with urllib.request.urlopen(url, timeout=30) as r:
                return json.load(r)
        except (urllib.error.URLError, TimeoutError, json.JSONDecodeError) as e:
            if attempt == attempts:
                raise
            print(
                f"PyPI fetch failed ({e}); retry {attempt}/{attempts - 1}",
                file=sys.stderr,
            )
            time.sleep(2 * attempt)
    raise RuntimeError("unreachable")


def select_versions(
    data: Any,
    *,
    days: int,
    now: Optional[dt.datetime] = None,
    limit: int = 0,
) -> list[str]:
    """Pick final, non-yanked releases uploaded within ``days``, newest first.

    Callers union this into their retention set and prune everything outside it,
    so a silently empty result would wipe the published corpus. A shape change in
    PyPI's JSON (which has repeatedly signalled intent to drop the ``releases``
    key) raises rather than looking like "no releases".
    """
    now = now or dt.datetime.now(dt.timezone.utc)
    releases = data.get("releases") if isinstance(data, dict) else None
    if not isinstance(releases, dict) or not releases:
        raise ValueError("PyPI JSON for acryl-datahub has no usable 'releases' map")

    cutoff = now - dt.timedelta(days=days)
    picked: list[tuple[dt.datetime, str]] = []
    for version, files in releases.items():
        if not _FINAL_RELEASE.match(version) or not files:
            continue
        # A yanked release must never be built into the corpus or counted in the
        # retention set.
        if any(f.get("yanked") for f in files):
            continue
        uploads = [
            dt.datetime.fromisoformat(f["upload_time_iso_8601"].replace("Z", "+00:00"))
            for f in files
            if f.get("upload_time_iso_8601")
        ]
        if not uploads:
            continue
        if min(uploads) >= cutoff:
            picked.append((min(uploads), version))

    picked.sort(reverse=True)
    versions = [v for _, v in picked]
    return versions[:limit] if limit else versions


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--days", type=int, default=365)
    ap.add_argument("--limit", type=int, default=0, help="cap count (0 = no cap)")
    ap.add_argument(
        "--allow-empty",
        action="store_true",
        help="print [] and exit 0 when nothing matches (default: exit non-zero)",
    )
    args = ap.parse_args()

    versions = select_versions(_fetch(_PYPI), days=args.days, limit=args.limit)

    if not versions and not args.allow_empty:
        # An empty matrix/window is almost always a bug (wrong --days, a PyPI
        # hiccup) that would otherwise silently produce or prune to an empty
        # corpus. Fail loudly; --allow-empty is the escape hatch.
        sys.exit(
            f"no final acryl-datahub releases in the last {args.days} days "
            "(pass --allow-empty to permit this)"
        )

    print(json.dumps(versions))
    print(
        f"\n{len(versions)} final releases in the last {args.days} days",
        file=sys.stderr,
    )
    if versions:
        print(f"newest: {versions[0]}   oldest: {versions[-1]}", file=sys.stderr)


if __name__ == "__main__":
    main()
