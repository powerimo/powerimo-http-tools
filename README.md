# Powerimo HTTP Tools

A Java library providing an OkHttp3-based API client framework with Keycloak OAuth2 integration.

## Installation

```xml
<dependency>
    <groupId>org.powerimo</groupId>
    <artifactId>powerimo-http-tools</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Features

- Typed HTTP client (GET, POST, PUT, DELETE) built on OkHttp3
- Pluggable authentication via OkHttp interceptors
- Automatic API key header injection
- Keycloak client-credentials token acquisition with TTL-aware caching and retry logic
- Generic `Envelope<T>` response wrapper for structured API responses
- Two payload converter strategies: direct deserialization and envelope unwrapping

## Usage

### HTTP Client

Subclass `BaseOkHttpApiClient` and supply a configuration:

```java
BaseOkHttpApiClientLocalConfig config = BaseOkHttpApiClientLocalConfig.builder()
        .url("https://api.example.com")
        .apiKey("my-api-key")
        .useApiKey(true)
        .connectTimeout(5)
        .callTimeout(30)
        .build();

BaseOkHttpApiClient client = new BaseOkHttpApiClient();
client.setConfig(config);
client.setPayloadConverter(new DefaultPayloadConverter());

// GET
MyDto result = client.executeGet(client.buildUrl("items", 42), MyDto.class);

// POST
MyDto created = client.executePost(client.buildUrl("items"), MyDto.class, requestBody);

// PUT / DELETE
client.executePut(client.buildUrl("items", 42), MyDto.class, updatedBody);
client.executeDelete(client.buildUrl("items", 42), null, null);
```

Override `customizeHttpClient(OkHttpClient.Builder)` to add interceptors or configure the underlying OkHttp client.

### Keycloak Integration

Obtain and cache access tokens using the client credentials grant:

```java
KeycloakParameters params = KeycloakStaticParameters.builder()
        .serverUrl("https://keycloak.example.com")
        .realm("my-realm")
        .clientId("my-client")
        .clientSecret("secret")
        .build();

KeycloakServiceAccessTokenRequester requester =
        new KeycloakServiceAccessTokenRequester(params);

// Returns a cached token, refreshing automatically when expired
String token = requester.getAccessToken();
```

Wire automatic token injection into an HTTP client:

```java
KeycloakServiceAccessTokenInterceptor interceptor =
        new KeycloakServiceAccessTokenInterceptor(requester);

client.setAuthenticationInterceptor(interceptor);
```

### Envelope

`Envelope<T>` is a generic response wrapper used by APIs that follow the powerimo response convention:

```java
// Server side
Envelope<MyDto> response = Envelope.of(myDto);
Envelope<Void>  error    = Envelope.error(null, 404, "Not found");

// Client side — use EnvelopePayloadConverter to unwrap automatically
client.setPayloadConverter(new EnvelopePayloadConverter());
MyDto data = client.executeGet(url, MyDto.class);
```

Fields: `code` (HTTP status), `data`, `timestamp`, `message`, `messageCode`, `path`.

## Configuration Reference

### BaseOkHttpClientConfig

| Property | Default | Description |
|---|---|---|
| `url` | — | Base URL of the API |
| `apiKey` | — | API key value |
| `useApiKey` | `false` | Enables `x-api-key` header injection |
| `connectTimeout` | — | Connection timeout (seconds) |
| `callTimeout` | — | Call/read timeout (seconds) |

### KeycloakParameters

| Property | Description |
|---|---|
| `serverUrl` | Keycloak base URL |
| `realm` | Realm name |
| `clientId` | OAuth2 client ID |
| `clientSecret` | OAuth2 client secret |
| `tokenUrl` | Override token endpoint (auto-computed from serverUrl + realm if omitted) |
| `introspectionUrl` | Override introspection endpoint (auto-computed if omitted) |

`KeycloakServiceAccessTokenRequester` retries failed token requests up to 3 times by default (`maxRetries`).

## License

MIT — see [LICENSE](https://mit-license.org/).
