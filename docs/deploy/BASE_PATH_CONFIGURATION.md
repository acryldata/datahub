# DataHub Base Path Configuration

This document describes how to configure DataHub to run from a custom base path (e.g., `/my-app/` instead of `/`).

## Overview

DataHub now supports serving from a custom base path, allowing you to:

- Serve DataHub from a subdirectory (e.g., `https://company.com/datahub/`)
- Deploy multiple DataHub instances on the same domain
- Integrate DataHub behind reverse proxies with path-based routing

## Configuration

### Environment Variables

Set the following environment variable to configure the base path:

```bash
export DATAHUB_BASE_PATH="/my-app"
```

**Important Notes:**

- The base path should start with `/` but not end with `/`
- Examples: `/datahub`, `/my-app`, `/tools/datahub`
- Leave empty or unset for root path deployment

### Component Configuration

The base path configuration affects three main components:

#### 1. Frontend (datahub-web-react)

The frontend determines the base path using multiple detection methods (in priority order):

1. **Environment Variable** (highest priority):

   ```bash
   # Set in .env file or environment
   REACT_APP_BASE_PATH=/my-app
   ```

2. **Server Configuration** (fetched from `/config` endpoint):

   - The React app fetches base path from the backend's configuration
   - Most reliable for production deployments

3. **URL Analysis** (fallback):
   - Analyzes current URL to detect base path
   - Used when server config is unavailable

**Development Configuration:**

```bash
# Method 1: Environment variable (recommended for development)
echo "REACT_APP_BASE_PATH=/my-app" >> .env
yarn start  # Development server at http://localhost:3000/my-app

# Method 2: Using Vite preview (for testing production builds)
yarn build
npx vite preview --port 3001  # Uses base path from REACT_APP_BASE_PATH
# Accessible at: http://localhost:3001/my-app

# Method 3: Override base path via command line
npx vite preview --port 3001 --base /custom-path
```

**Production Behavior:**

- Automatically fetches base path from server configuration
- Falls back to URL detection if server config unavailable
- Provides console warnings to help with debugging

#### 2. DataHub Frontend Service (Play Framework)

The Play Framework application supports base path through:

- `datahub.basePath` configuration in `application.conf`
- `play.http.context` for Play Framework native support
- Environment variable: `DATAHUB_BASE_PATH`

#### 3. Metadata Service (Spring Boot)

The metadata service (GMS) supports base path through:

- `server.servlet.context-path` in `application.yaml`
- Environment variable: `DATAHUB_BASE_PATH`

## Deployment Examples

### Docker Compose

```yaml
version: "3.8"
services:
  datahub-frontend-react:
    environment:
      - DATAHUB_BASE_PATH=/datahub

  datahub-frontend:
    environment:
      - DATAHUB_BASE_PATH=/datahub

  datahub-gms:
    environment:
      - DATAHUB_BASE_PATH=/datahub
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: datahub-frontend
spec:
  template:
    spec:
      containers:
        - name: datahub-frontend
          env:
            - name: DATAHUB_BASE_PATH
              value: "/datahub"
```

### Reverse Proxy Configuration

#### Nginx

```nginx
location /datahub/ {
    proxy_pass http://datahub-frontend:9002/datahub/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}
```

#### Apache

```apache
ProxyPreserveHost On
ProxyPass /datahub/ http://datahub-frontend:9002/datahub/
ProxyPassReverse /datahub/ http://datahub-frontend:9002/datahub/
```

## How It Works

### Request Flow

1. **Client Request**: `https://company.com/datahub/login`
2. **Frontend Service**: Receives request, strips base path `/datahub`, processes as `/login`
3. **Authentication**: Handles login logic normally
4. **Response**: Includes base path in redirects and links

### Frontend Routing

The React application uses `getRuntimeBasePath()` to:

- Configure React Router with the correct basename
- Resolve API endpoints with base path
- Handle authentication redirects properly

### API Proxying

API requests are automatically prefixed with the base path:

- `/api/v2/graphql` → `/datahub/api/v2/graphql`
- Frontend service strips base path before proxying to GMS
- GMS processes requests normally

## Troubleshooting

### Common Issues

1. **404 on Authentication Endpoints**

   - Ensure `DATAHUB_BASE_PATH` is set consistently across all services
   - Check that the base path doesn't end with `/`

2. **Assets Not Loading**

   - Verify reverse proxy configuration passes through static assets
   - Check that `DATAHUB_BASE_PATH` is set for the frontend service

3. **Redirects to Wrong Path**
   - Ensure the frontend can determine the correct base path
   - Check that `baseUrl` configuration includes the base path

### Debugging

Enable verbose logging to debug base path issues:

```bash
# Frontend service
export AUTH_VERBOSE_LOGGING=true

# Metadata service
export METADATA_SERVICE_AUTH_ENABLED=true
```

Check the application configuration endpoint:

```bash
curl https://your-domain/datahub/config
```

## Migration from Root Path

To migrate an existing DataHub deployment from root path to base path:

1. **Update Configuration**:

   ```bash
   export DATAHUB_BASE_PATH="/datahub"
   ```

2. **Update Reverse Proxy**:
   Add base path routing rules

3. **Update Bookmarks/Links**:
   Update any hardcoded links to include base path

4. **Deploy Changes**:
   Restart all DataHub services with new configuration

## Security Considerations

- Base path configuration doesn't change authentication requirements
- Ensure reverse proxy properly handles authentication headers
- Consider path-based access controls in your reverse proxy

## Performance Impact

- Minimal performance impact
- Base path stripping/adding is lightweight string operation
- No additional network requests

## Limitations

- Base path must be consistent across all DataHub components
- Changing base path requires service restart
- Some third-party integrations may need URL updates
