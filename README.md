# How to run in local
- cd to project folder
- start keycloak and postgres local
> docker compose up 
- start app local for dev:
> mvn quarkus:dev
- should add 2 users, one's role is admin and one is viewer

# How to call APIs
- call to get access token:
> curl --location 'http://localhost:8090/realms/tenant-1/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=doc-service' \
--data-urlencode 'client_secret=xNTAF56ZiUkyvMk0v0QxD5m05dNetxY9' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=admin-user' \
--data-urlencode 'password=password1'

- create document:
> curl --location 'http://localhost:8080/documents/' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer xxxx.xxxx.xxxxxxx' \
--data '{
"id": "bcd7fdc5-a63f-4241-8e98-ae34a0aaa7f6",
"title": "test title",
"content": "123",
"tenantId": "tenant-1"
}'

- get document:
> curl --location 'http://localhost:8080/documents/08d9454e-8d4e-4eb8-970b-4c33b08e3064' \
--header 'Authorization: Bearer xxx.xxxx.xxxx'

- gRPC call for document processing
> grpcurl -H "content-type: application/json" -H "Authorization: Bearer jwtToken" -d '{"document_id": "08d9454e-8d4e-4eb8-970b-4c33b08e3064"}' -plaintext localhost:8080 DocumentProcessor/process

# JWT token structure:
add tenantId as claim 
```JWT access token payload
{
    "exp": 1743308852,
    "iat": 1743308552,
    "jti": "12676e95-2bd6-46e8-bba0-05f036512e46",
    "iss": "http://localhost:8090/realms/tenant-1",
    "aud": "account",
    "sub": "32781ac1-836a-47e5-92a2-1103f2b7992d",
    "typ": "Bearer",
    "azp": "doc-service",
    "session_state": "7eafbaaa-e73a-42e6-b791-c8b86a0427bb",
    "acr": "1",
    "allowed-origins": [
        "/*"
    ],
    "realm_access": {
        "roles": [
            "offline_access",
            "default-roles-tenant-1",
            "admin",
            "uma_authorization"
        ]
    },
    "resource_access": {
        "account": {
            "roles": [
                "manage-account",
                "manage-account-links",
                "view-profile"
            ]
        }
    },
    "scope": "profile email",
    "sid": "7eafbaaa-e73a-42e6-b791-c8b86a0427bb",
    "tenant_id": "tenant-1",
    "email_verified": false,
    "name": "admin test",
    "preferred_username": "admin-user",
    "given_name": "admin",
    "family_name": "test",
    "email": "admin@example.com"
}
```