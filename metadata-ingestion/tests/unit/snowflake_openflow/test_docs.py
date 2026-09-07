import pathlib
import re
from typing import List, Set

import yaml

DOC_DIR = pathlib.Path("docs/sources/snowflake-openflow")
PRE = DOC_DIR / "snowflake-openflow_pre.md"
POST = DOC_DIR / "snowflake-openflow_post.md"
README = DOC_DIR / "README.md"
RECIPE = DOC_DIR / "snowflake-openflow_recipe.yml"


def headings(path: pathlib.Path, level: int) -> List[str]:
    pattern = re.compile(r"^#{%d} (.+)$" % level, re.MULTILINE)
    return pattern.findall(path.read_text())


def all_heading_levels(path: pathlib.Path) -> Set[int]:
    return {
        len(m.group(1)) for m in re.finditer(r"^(#+) ", path.read_text(), re.MULTILINE)
    }


def test_all_doc_files_exist():
    missing = [p.name for p in [README, PRE, POST, RECIPE] if not p.exists()]
    assert not missing, f"missing doc files: {missing}"


def test_readme_matches_docgen_contract():
    # docgen allows ONLY these two H2s, in this order, and forbids H1.
    assert headings(README, 2) == ["Overview", "Concept Mapping"]
    assert 1 not in all_heading_levels(README)


def test_pre_matches_docgen_contract():
    assert headings(PRE, 3) == ["Overview", "Prerequisites"]
    assert not {1, 2} & all_heading_levels(PRE)


def test_post_matches_docgen_contract():
    assert headings(POST, 3) == ["Capabilities", "Limitations", "Troubleshooting"]
    assert not {1, 2} & all_heading_levels(POST)


def test_permissions_are_documented_under_prerequisites():
    # The permissions material has to be H4+, since H3 is restricted to
    # Overview/Prerequisites. Assert the content is present and correctly nested.
    text = PRE.read_text()
    prerequisites = text.split("### Prerequisites", 1)[1]
    for required in ["MONITOR", "ACCOUNT_USAGE", "FUTURE"]:
        assert required in prerequisites, f"permissions docs omit {required}"


def test_measured_limitations_are_stated():
    # Each was measured against a live account and will otherwise arrive as a bug report.
    text = POST.read_text()
    assert "Gen 1" in text
    assert "zero rows" in text


def test_recipe_parses_and_names_the_source():
    recipe = yaml.safe_load(RECIPE.read_text())
    assert recipe["source"]["type"] == "snowflake-openflow"
