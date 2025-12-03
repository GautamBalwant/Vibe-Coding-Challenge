# Prompt: Generate API-Gateway Tests with Circuit-Breaker, Filters, and Reactive Clients

Purpose

This prompt is an API-Gateway–focused variant of the original test-generation prompt. It's tuned for Spring Cloud Gateway projects (reactive stack), Resilience4j circuit-breaker behavior, gateway filters (pre/post/route), and fallback endpoints. Use this when you want LLM-generated unit, component, and lightweight integration tests for the `api-gateway` module.

What this file generates

- Controller/filter/unit tests using JUnit 5 and Mockito.
- Reactive WebTestClient examples for end-to-end and slice tests.
- Resilience4j circuit-breaker unit tests and fallback endpoint verification.
- Filter behavior tests for `LoggingFilter` and `FallbackController`.
- Examples for mocking static helpers and constructing test doubles for classes created with `new`.
- Integration test template using `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)` plus `@AutoConfigureWebTestClient` for lightweight reactive flows.

Context to provide (fill these before running the LLM):
- Module: api-gateway
- File or class under test (path under `api-gateway/src/main/java/...`)
- Test types wanted: (unit | component | integration)
- Dependencies to mock or stub (external services, static utilities, Resilience4j registries)
- Key behaviors and edge cases (fallback triggers, timeout, error status mapping, header transformation)

Prompt template (what to feed the LLM)
```
You are a senior Java engineer generating tests for a Spring Cloud Gateway project.

Context:
- Module: api-gateway
- File: {{file_path}}
- Frameworks: JUnit 5, Mockito, Spring WebFlux Test (WebTestClient), Resilience4j
- Important dependencies to mock: {{dependencies}}
- Sample input/outcomes: {{examples}} (e.g., route to downstream returns 500 -> circuit opens -> gateway calls fallback)

Task (what the LLM should output):
1. Produce a test class ready to paste under `api-gateway/src/test/java/...` with imports, annotations and test methods.
2. For controller/filter/unit tests show both:
   - Mockito-based unit tests for filter logic (standalone, non-reactive),
   - Reactive tests using `WebTestClient` for route/fallback behavior.
3. Provide a Resilience4j-focused test: mock the downstream failure (5xx or timeout), then assert the circuit-breaker records failures and that the fallback path (e.g., `FallbackController`) is invoked.
4. Demonstrate mocking static utilities with `MockedStatic` (try-with-resources) and `mockConstruction()` if classes are instantiated directly in code.
5. Include an integration-style example using `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)` together with `@AutoConfigureWebTestClient` that verifies a route and its fallback.
6. Provide test data snippets (e.g., synthetic reactive responses with `Mono.just`, `Mono.error`, `Flux.just`) and a minimal MockServer or WireMock usage snippet for simulating downstream services.
7. Add per-module commands and CI suggestions tuned for reactive/gateway testing.

Deliverable format:
- Full test file content ready to paste into `api-gateway/src/test/java/...`.
- Brief explanation of each test and why it exists.
- Per-module powershell test commands and CI recommendations.
```

Concrete examples included below

- Filter unit test (LoggingFilter)
- Fallback controller test (FallbackController)
- Circuit-breaker behavior test simulating downstream 500 responses and verifying fallback invocation
- WebTestClient integration test with MockWebServer/WireMock to simulate downstream

Example: LoggingFilter unit test (standalone)

```java
package com.realtyx.gateway.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.DefaultServerWebExchange;

import static org.mockito.Mockito.*;

class LoggingFilterTest {
    private LoggingFilter filter;

    @BeforeEach
    void setup() {
        filter = new LoggingFilter();
    }

    @Test
    void filterAddsExpectedHeadersOrLogs() {
        ServerWebExchange exchange = MockServerHttpRequest.get("/test").toExchange();
        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());

        filter.filter(exchange, chain).block();

        verify(chain).filter(any());
        // Additional assertions based on filter behavior (headers, attributes)
    }
}
```

Example: FallbackController + circuit-breaker behavior (WebTestClient)

```java
package com.realtyx.gateway.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FallbackControllerIntegrationTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void whenDownstreamFails_thenFallbackIsReturned() {
        // Configure a MockWebServer/WireMock to return 500 for the proxied route
        webTestClient.get().uri("/fallback-endpoint")
            .exchange()
            .expectStatus().isOk()
            .expectBody().consumeWith(b -> {
                // assert fallback content
            });
    }
}
```

WireMock snippet (recommended for simulating downstream services)

```java
WireMockServer server = new WireMockServer(options().dynamicPort());
server.start();
server.stubFor(get(urlEqualTo("/downstream/resource"))
    .willReturn(aResponse().withStatus(500)));

// Point gateway route in test to server.baseUrl()
```

Notes and tips (API-Gateway specific)

- Use `WebTestClient` for reactive, non-blocking tests and `MockMvc` only for non-reactive controllers.
- Keep Resilience4j configuration small for tests: use an in-memory registry or programmatic config to shorten circuit breaker windows.
- For pure filter logic, use lightweight unit tests with mocked `GatewayFilterChain` and `ServerWebExchange`.
- For route/fallback behavior, prefer WireMock/MockWebServer + `@SpringBootTest` and `WebTestClient`.
- When mocking static registries or helpers, scope mocks tightly with try-with-resources (MockedStatic).

Per-module test commands (PowerShell)

Reactive integration (run api-gateway tests):
```powershell
mvn -pl api-gateway -am -Dtest="**/*Gateway*Test" test
```

Run a single test class:
```powershell
mvn -pl api-gateway -Dtest=FallbackControllerIntegrationTest test
```

CI recommendations

- Run unit and filter tests first (fast) in one job.
- Run WebTestClient + WireMock integration tests in a separate job that brings up stub servers.
- Shorten Resilience4j time windows in test profile to avoid long sleeps.

Deliverables

- Paste-ready test class examples (filter unit test, fallback integration test).
- Short guidance for mocking downstream services with WireMock/MockWebServer.
- PowerShell commands for running tests locally and in CI.

Would you like me to:
1) Generate a full `FallbackControllerIntegrationTest.java` for `api-gateway` that uses WireMock to simulate a downstream service and asserts fallback behavior? 
2) Convert a chosen filter or controller to constructor injection to make it easier to test and provide the updated tests? 
3) Add a GitHub Actions workflow snippet that runs `mvn -pl api-gateway test` and separates unit vs integration jobs?

If yes, pick (1), (2), or (3) and confirm the target package path (default: `com.realtyx.gateway.filter` or `com.realtyx.gateway.config`).