import base64
import datetime
import json
import logging
from typing import Any, Optional

import pytest

from datahub_actions.utils import kafka_gcp_managed_kafka as sut

ACCOUNT = "datahub@my-project.iam.gserviceaccount.com"
EXPIRY = datetime.datetime(2030, 1, 1, 12, 0, 0)  # naive UTC, like google-auth


class FakeCredentials:
    def __init__(self, service_account_email: Optional[str] = ACCOUNT) -> None:
        self.token: Optional[str] = None
        self.expiry: Optional[datetime.datetime] = None
        self.refreshes = 0
        if service_account_email is not None:
            self.service_account_email = service_account_email

    @property
    def valid(self) -> bool:
        return self.token is not None

    def refresh(self, request: Any) -> None:
        self.refreshes += 1
        self.token = f"ya29.token-{self.refreshes}"
        self.expiry = EXPIRY


@pytest.fixture
def fake_default(monkeypatch: Any) -> Any:
    """Patch google.auth.default and reset the module's cached credentials."""
    calls: list = []

    def install(creds: FakeCredentials) -> list:
        def fake_default(scopes: Any = None) -> tuple:
            calls.append(scopes)
            return creds, "my-project"

        monkeypatch.setattr(sut.google.auth, "default", fake_default)
        return calls

    monkeypatch.setattr(sut, "_credentials", None)
    monkeypatch.delenv(sut.PRINCIPAL_ENV_VAR, raising=False)
    return install


def _decode(part: str) -> str:
    return base64.urlsafe_b64decode(part + "=" * (-len(part) % 4)).decode()


def test_oauth_cb_builds_google_envelope_token(fake_default: Any) -> None:
    calls = fake_default(FakeCredentials())

    token, expiry = sut.oauth_cb({})

    expected_exp = EXPIRY.replace(tzinfo=datetime.timezone.utc).timestamp()
    assert expiry == expected_exp
    assert calls == [["https://www.googleapis.com/auth/cloud-platform"]]

    parts = token.split(".")
    assert len(parts) == 3
    assert "=" not in token
    assert json.loads(_decode(parts[0])) == {"typ": "JWT", "alg": "GOOG_OAUTH2_TOKEN"}
    claims = json.loads(_decode(parts[1]))
    assert claims["exp"] == expected_exp
    assert claims["iss"] == "Google"
    assert claims["sub"] == ACCOUNT
    assert isinstance(claims["iat"], float)
    assert _decode(parts[2]) == "ya29.token-1"


def test_oauth_cb_loads_credentials_once_and_refreshes_only_when_invalid(
    fake_default: Any,
) -> None:
    creds = FakeCredentials()
    calls = fake_default(creds)

    sut.oauth_cb({})
    sut.oauth_cb({})
    assert len(calls) == 1
    assert creds.refreshes == 1

    creds.token = None  # expired
    token, _ = sut.oauth_cb({})
    assert creds.refreshes == 2
    assert _decode(token.split(".")[2]) == "ya29.token-2"


def test_oauth_cb_principal_override(fake_default: Any, monkeypatch: Any) -> None:
    fake_default(FakeCredentials(service_account_email=None))
    monkeypatch.setenv(sut.PRINCIPAL_ENV_VAR, "user@example.com")

    token, _ = sut.oauth_cb({})

    assert json.loads(_decode(token.split(".")[1]))["sub"] == "user@example.com"


def test_oauth_cb_raises_and_logs_without_principal(
    fake_default: Any, caplog: Any
) -> None:
    fake_default(FakeCredentials(service_account_email=None))
    caplog.set_level(logging.ERROR)

    with pytest.raises(ValueError, match=sut.PRINCIPAL_ENV_VAR):
        sut.oauth_cb({})

    assert any(
        rec.levelno == logging.ERROR
        and "Error generating GCP Managed Kafka authentication token"
        in rec.getMessage()
        for rec in caplog.records
    )
