import datetime as dt
import sys

import pytest

sys.path.append(".")

from docsbase.list_versions import select_versions

# Fixed "now" so the window arithmetic is deterministic: cutoff = 2025-09-17.
_NOW = dt.datetime(2026, 9, 17, tzinfo=dt.timezone.utc)


def _rel(date: str, yanked: bool = False) -> list:
    return [{"upload_time_iso_8601": f"{date}T00:00:00Z", "yanked": yanked}]


_DATA = {
    "releases": {
        "1.7.0.10": _rel("2026-09-07"),  # final, in window
        "1.7.0": _rel("2026-08-01"),  # 3-part final, in window
        "1.6.0.13": _rel("2025-06-01"),  # final but older than 365d
        "1.8.0rc1": _rel("2026-09-10"),  # rc — excluded by regex
        "1.7.0.11.dev1": _rel("2026-09-12"),  # dev — excluded by regex
        "1.7.0.9": _rel("2026-09-01", yanked=True),  # yanked — excluded
    }
}


def test_selects_final_in_window_newest_first():
    # In-window, final, non-yanked only; newest first; rc/dev/old/yanked dropped.
    assert select_versions(_DATA, days=365, now=_NOW) == ["1.7.0.10", "1.7.0"]


def test_limit_caps_result():
    assert select_versions(_DATA, days=365, now=_NOW, limit=1) == ["1.7.0.10"]


def test_narrow_window_excludes_by_upload_date():
    # A 6-day window (cutoff 2026-09-11) leaves only the 2026-09-12 dev build,
    # which the final-release regex drops — so nothing qualifies.
    assert select_versions(_DATA, days=6, now=_NOW) == []


@pytest.mark.parametrize("bad", [{}, {"releases": None}, {"releases": {}}, "notjson"])
def test_bad_pypi_shape_raises(bad):
    # A silently empty result would wipe the retention set, so a missing/empty
    # releases map must raise rather than return [].
    with pytest.raises(ValueError):
        select_versions(bad, days=365, now=_NOW)


if __name__ == "__main__":
    sys.exit(pytest.main([__file__, "-v"]))
