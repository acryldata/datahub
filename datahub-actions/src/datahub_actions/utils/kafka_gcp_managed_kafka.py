"""Module for Google Cloud Managed Service for Apache Kafka OAuth authentication."""

import base64
import datetime
import json
import logging
import os
import threading
from typing import Any, Optional

import google.auth
import google.auth.transport.requests

logger = logging.getLogger(__name__)

PRINCIPAL_ENV_VAR = "GOOGLE_MANAGED_KAFKA_AUTH_PRINCIPAL"
_SCOPES = ["https://www.googleapis.com/auth/cloud-platform"]
_HEADER = json.dumps({"typ": "JWT", "alg": "GOOG_OAUTH2_TOKEN"})

_lock = threading.Lock()
_credentials: Optional[Any] = None


def _b64(data: str) -> str:
    return base64.urlsafe_b64encode(data.encode()).decode().rstrip("=")


def oauth_cb(oauth_config: Any) -> tuple[str, float]:
    """
    OAuth callback function for Google Cloud Managed Service for Apache Kafka.

    This function is invoked by the Kafka client to generate the SASL/OAUTHBEARER token
    for authentication with GCP Managed Kafka. It wraps a Google access token from
    Application Default Credentials (e.g. GKE Workload Identity) in the JWT-shaped
    envelope the brokers expect, matching Google's kafka-auth-local-server.

    The principal is the service account email of the credentials. Set
    GOOGLE_MANAGED_KAFKA_AUTH_PRINCIPAL to override it for credential types that do
    not expose one (e.g. end-user credentials).

    Returns:
        tuple[str, float]: (auth_token, expiry_time_seconds)
    """
    global _credentials
    try:
        with _lock:
            if _credentials is None:
                _credentials, _ = google.auth.default(scopes=_SCOPES)
            creds = _credentials
            if not creds.valid:
                creds.refresh(google.auth.transport.requests.Request())
            token = creds.token
            expiry = creds.expiry
        if not token or expiry is None:
            raise ValueError(
                "Google credentials returned no access token with an expiry"
            )

        principal = os.getenv(PRINCIPAL_ENV_VAR) or getattr(
            creds, "service_account_email", None
        )
        if not principal or principal == "default":
            raise ValueError(
                f"Unable to determine the Managed Kafka principal for credentials of "
                f"type {type(creds).__name__}; set {PRINCIPAL_ENV_VAR}"
            )

        # google-auth expiry is a naive UTC datetime.
        exp = expiry.replace(tzinfo=datetime.timezone.utc).timestamp()
        now = datetime.datetime.now(datetime.timezone.utc).timestamp()
        claims = {"exp": exp, "iss": "Google", "iat": now, "sub": principal}
        auth_token = ".".join([_b64(_HEADER), _b64(json.dumps(claims)), _b64(token)])
        return auth_token, exp
    except Exception as e:
        logger.error(
            f"Error generating GCP Managed Kafka authentication token: {e}",
            exc_info=True,
        )
        raise
