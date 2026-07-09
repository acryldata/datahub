"""Emit the acryl-datahub four-part releases from roughly the last year.

Queries PyPI, keeps only final four-part versions (``X.Y.Z.W``, all numeric —
no rc/post/dev), filters to uploads within ``--days`` (default 365), and prints a
JSON array (newest first). Used to build the docsbase generation matrix.
"""

import argparse
import datetime as dt
import json
import re
import sys
import urllib.request

_PYPI = "https://pypi.org/pypi/acryl-datahub/json"
_FOUR_PART = re.compile(r"^\d+\.\d+\.\d+\.\d+$")


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--days", type=int, default=365)
    ap.add_argument("--limit", type=int, default=0, help="cap count (0 = no cap)")
    args = ap.parse_args()

    with urllib.request.urlopen(_PYPI, timeout=30) as r:
        data = json.load(r)

    cutoff = dt.datetime.now(dt.timezone.utc) - dt.timedelta(days=args.days)
    picked: list[tuple[dt.datetime, str]] = []
    for version, files in data.get("releases", {}).items():
        if not _FOUR_PART.match(version) or not files:
            continue
        uploads = [
            dt.datetime.fromisoformat(f["upload_time_iso_8601"].replace("Z", "+00:00"))
            for f in files
            if f.get("upload_time_iso_8601")
        ]
        if not uploads:
            continue
        when = min(uploads)
        if when >= cutoff:
            picked.append((when, version))

    picked.sort(reverse=True)
    versions = [v for _, v in picked]
    if args.limit:
        versions = versions[: args.limit]
    print(json.dumps(versions))
    print(f"\n{len(versions)} four-part releases in the last {args.days} days", file=sys.stderr)
    if versions:
        print(f"newest: {versions[0]}   oldest: {versions[-1]}", file=sys.stderr)


if __name__ == "__main__":
    main()
