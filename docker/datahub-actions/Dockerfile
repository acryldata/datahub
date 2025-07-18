# This image has two variants: full and slim.
# The full variant has additional deps preinstalled, like a JRE and Oracle client.
ARG APP_ENV=full
ARG PYTHON_VERSION=3.10

# INLINE-BEGIN @/docker/snippets/ingestion_base
# This is the "base" image workflow.
# While it has a bunch of intermediate stages, it "exports" a couple
# stages for consumption.
# - python-base: A basic stage, with basic deps, Python, and a venv.
# - ingestion-base-slim: Currently the same as base-empty.
# - ingestion-base-full: Adds a JRE and Oracle client.

FROM ubuntu:24.04 AS python-base

# TODO: This may not work on Ubuntu 24.04 due to the new deb822 package format.
ARG UBUNTU_REPO_URL=http://ports.ubuntu.com/ubuntu-ports
RUN if [ "${UBUNTU_REPO_URL}" != "http://ports.ubuntu.com/ubuntu-ports" ] ; then sed -i "s#http.*://ports.ubuntu.com/ubuntu-ports#${UBUNTU_REPO_URL}#g" /etc/apt/sources.list ; fi

ENV HOME=/home/datahub
RUN existing_group=$(getent group 1000 | cut -d: -f1) && \
    if [ -n "$existing_group" ] && [ "$existing_group" != "datahub" ]; then \
        echo "Renaming existing group $existing_group to datahub"; \
        groupmod -n datahub "$existing_group"; \
    elif [ -z "$existing_group" ]; then \
        echo "Creating new group datahub with GID 1000"; \
        addgroup --gid 1000 datahub; \
    fi && \
    existing_user=$(id -nu 1000 2>/dev/null || echo "") && \
    if [ -n "$existing_user" ] && [ "$existing_user" != "datahub" ]; then \
        echo "Renaming existing user $existing_user to datahub"; \
        usermod -l datahub -d $HOME "$existing_user"; \
        usermod -g datahub datahub; \
    elif [ -z "$existing_user" ]; then \
        echo "Creating new user datahub with UID 1000"; \
        adduser --disabled-password --uid 1000 --gid 1000 --home $HOME datahub; \
    fi && \
    # Create and set proper permissions for datahub directories
    mkdir -p $HOME && \
    chown -R datahub:datahub $HOME && \
    chmod g-s $HOME

# Setup the PPA for alternative Python versions.
# TODO: Eventually we should switch to using uv's support for python-build-standalone.
RUN apt-get update && apt-get install -y \
    software-properties-common \
    lsb-release \
    gnupg \
    ca-certificates \
    && add-apt-repository --no-update ppa:deadsnakes/ppa \
    && rm -rf /var/lib/apt/lists/*

ARG PYTHON_VERSION
RUN test -n "${PYTHON_VERSION}"  # PYTHON_VERSION must be set

RUN apt-get update && apt-get install -y \
    python${PYTHON_VERSION} \
    python${PYTHON_VERSION}-venv \
    python${PYTHON_VERSION}-dev \
    python${PYTHON_VERSION}-distutils \
    python-is-python3 \
    git \
    wget \
    curl \
    zip \
    unzip \
    nano \
    && rm -rf /var/lib/apt/lists/*

# Set the default python version.
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python${PYTHON_VERSION} 1 \
    && update-alternatives --install /usr/bin/python python /usr/bin/python3 1

COPY --from=ghcr.io/astral-sh/uv:latest /uv /uvx /bin/

ARG PIP_MIRROR_URL=https://pypi.python.org/simple
RUN if [ "${PIP_MIRROR_URL}" != "https://pypi.python.org/simple" ] ; then uvx --no-cache pip config set global.index-url ${PIP_MIRROR_URL} ; fi
ENV UV_INDEX_URL=${PIP_MIRROR_URL}

USER datahub
WORKDIR $HOME
RUN uv venv --python "$PYTHON_VERSION"
ENV VIRTUAL_ENV=$HOME/.venv
ENV PATH="${VIRTUAL_ENV}/bin:$PATH"

# We always want to use the system CA bundle.
# Requests comes with it's own CA bundle, which we need to override.
ENV REQUESTS_CA_BUNDLE=/etc/ssl/certs/ca-certificates.crt
# uv uses a different mechanism. See https://github.com/astral-sh/uv/issues/1474.
ENV SSL_CERT_FILE="/etc/ssl/certs/ca-certificates.crt"


FROM python-base AS ingestion-base-slim

USER 0
RUN apt-get update && apt-get install -y \
    python3-ldap \
    libldap2-dev \
    libsasl2-dev \
    libsasl2-modules \
    libaio-dev \
    libaio1t64 \
    libsasl2-modules-gssapi-mit \
    krb5-user \
    krb5-config \
    libkrb5-dev \
    librdkafka-dev \
    ldap-utils \
    unixodbc \
    libodbc2 \
    && rm -rf /var/lib/apt/lists/*
USER datahub

FROM ingestion-base-slim AS ingestion-base-full

USER 0
# We need to install build-essential in order to build some Python packages (e.g. python-ldap)
RUN apt-get update && apt-get install --no-install-recommends -y -qq \
    default-jre-headless \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

RUN --mount=type=bind,source=./docker/snippets/oracle_instantclient.sh,target=/oracle_instantclient.sh \
    /oracle_instantclient.sh

USER datahub
# INLINE-END

FROM ingestion-base-${APP_ENV} AS final

USER root

COPY --from=powerman/dockerize:0.19 /usr/local/bin/dockerize /usr/local/bin
COPY --chown=datahub:datahub ./docker/datahub-actions/start.sh /start_datahub_actions.sh
COPY --chown=datahub:datahub ./docker/datahub-actions/readiness-check.sh /readiness-check.sh

RUN chmod a+x /start_datahub_actions.sh && \
    mkdir -p /etc/datahub/actions && \
    mkdir -p /tmp/datahub/logs/actions/system && \
    chown -R datahub:datahub /etc/datahub /tmp/datahub

# Install a cacheble layer that installs external dependencies and does not get invalidated due to changes in ingestion or actions code
# Copy just enough to enable pip compile to work. Other code changes wont invalidate this layer.
COPY --chown=datahub:datahub ./metadata-ingestion/setup.py /metadata-ingestion/
COPY --chown=datahub:datahub ./metadata-ingestion/src/datahub/_version.py /metadata-ingestion/src/datahub/
COPY --chown=datahub:datahub ./datahub-actions/setup.py /datahub-actions/
COPY --chown=datahub:datahub ./datahub-actions/src/datahub_actions/_version.py /datahub-actions/src/datahub_actions/
COPY --chown=datahub:datahub ./datahub-actions/README.md /datahub-actions/

USER datahub
RUN echo "-e /metadata-ingestion/ \n -e /datahub-actions/[all]" | uv pip compile /dev/stdin | grep -v "\-e" | uv pip install -r /dev/stdin
USER 0

COPY --chown=datahub:datahub ./metadata-ingestion /metadata-ingestion
COPY --chown=datahub:datahub ./datahub-actions /datahub-actions
# Add other default configurations into this!
COPY --chown=datahub:datahub ./docker/datahub-actions/config /etc/datahub/actions/system/conf

USER datahub

ARG RELEASE_VERSION
RUN test -n "$RELEASE_VERSION"  # RELEASE_VERSION is a required build arg
RUN --mount=type=bind,source=./python-build/version_updater.py,target=/version_updater.py \
    python /version_updater.py --directory /metadata-ingestion/ --version "$RELEASE_VERSION" --expected-update-count 1 && \
    python /version_updater.py --directory /datahub-actions/ --version "$RELEASE_VERSION" --expected-update-count 1

# For the datahub-actions build, we explicitly want to retain the uv cache.
# This speeds up the process of creating venvs at runtime.
# Because uv uses hardlinks for installing packages, keeping the cache around does not
# really impact image size.
RUN uv pip install -e '/metadata-ingestion/' -e '/datahub-actions/[all]'

ENTRYPOINT [ ]
CMD dockerize -wait ${DATAHUB_GMS_PROTOCOL:-http}://$DATAHUB_GMS_HOST:$DATAHUB_GMS_PORT/health -timeout 240s /start_datahub_actions.sh
