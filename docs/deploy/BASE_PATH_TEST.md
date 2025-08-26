# Base Path Configuration Test

## Manual Testing Guide

### Prerequisites

1. DataHub services running with base path configuration
2. Browser for testing frontend functionality

### Test Configuration

Set the following environment variable before starting DataHub:

```bash
export DATAHUB_BASE_PATH="/my-app"
```

### Test Cases

#### 1. Frontend Service Configuration Test

```bash
# Check that configuration is loaded
curl http://localhost:9002/my-app/config

# Expected: Response includes "basePath": "/my-app"
```

#### 2. Authentication Endpoints Test

```bash
# Test login endpoint
curl -X POST http://localhost:9002/my-app/logIn \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test"}'

# Expected: Should not return 404
```

#### 3. Frontend Serving Test

```bash
# Test base path serves the React app
curl http://localhost:9002/my-app/

# Expected: Returns React app HTML
```

#### 4. API Proxy Test

```bash
# Test API proxying with base path
curl http://localhost:9002/my-app/api/v2/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"query { __typename }"}'

# Expected: GraphQL response or authentication error (not 404)
```

#### 5. React App Runtime Test

1. Open browser to `http://localhost:3000/my-app/`
2. Check that:
   - Page loads correctly
   - Login form appears
   - Attempting login makes request to `/my-app/logIn`
   - Static assets load from correct paths

### Expected Results

✅ **Working Configuration:**

- Config endpoint returns base path in configuration
- Authentication endpoints respond (not 404)
- React app loads and functions correctly
- All API calls include base path prefix

❌ **Issues to Debug:**

- 404 errors on authentication endpoints
- React app not loading assets correctly
- API calls going to wrong paths

### Testing Without Base Path

To verify backward compatibility:

```bash
unset DATAHUB_BASE_PATH
# Restart services
# Test all endpoints without base path prefix
```

### Browser Testing

1. **Base Path URL**: `http://localhost:3000/my-app/`
2. **Check Developer Tools**:
   - Network tab for correct request URLs
   - Console for any errors
   - Application tab for correct cookie domains

### Verification Commands

```bash
# Verify frontend service base path
echo "Testing frontend config..."
curl -s http://localhost:9002/my-app/config | jq .config.basePath

# Verify GMS context path
echo "Testing GMS health..."
curl -s http://localhost:8080/my-app/health

# Verify authentication
echo "Testing authentication endpoint..."
curl -I http://localhost:9002/my-app/authenticate
```

## Troubleshooting

### Common Issues

1. **404 on all endpoints**: Base path not configured correctly
2. **Assets not loading**: Frontend base path detection not working
3. **Authentication redirects wrong**: Base path not included in redirects
4. **CORS errors**: Proxy configuration missing base path

### Debug Steps

1. Check logs for base path loading messages
2. Verify environment variables are set
3. Test both with and without trailing slashes
4. Check reverse proxy configuration if using one

### Environment Variables Summary

```bash
# Required for all components
export DATAHUB_BASE_PATH="/my-app"

# Optional for development
export REACT_APP_BASE_PATH="/my-app"
export REACT_APP_PROXY_TARGET="http://localhost:9002"
```
