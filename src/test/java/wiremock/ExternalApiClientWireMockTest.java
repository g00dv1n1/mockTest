package wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.example.wiremock.ExternalApiClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExternalApiClientWireMockTest {

    private WireMockServer wireMockServer;
    private ExternalApiClient client;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(options().port(8089));
        wireMockServer.start();

        configureFor("localhost", 8089);

        client = new ExternalApiClient(wireMockServer.baseUrl(), 500);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void successfulResponse200() {
        stubFor(get(urlEqualTo("/data"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"title\": \"Hello from WireMock\"}")));

        String result = client.fetchTitle();

        assertEquals("Title: Hello from WireMock", result);
    }

    @Test
    void notFound404() {
        stubFor(get(urlEqualTo("/data"))
                .willReturn(aResponse()
                        .withStatus(404)));

        String result = client.fetchTitle();

        assertEquals("Not found", result);
    }

    @Test
    void serverError500() {
        stubFor(get(urlEqualTo("/data"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "text/plain")
                        .withBody("Internal error occurred")));

        String result = client.fetchTitle();

        assertEquals("Server error: Internal error occurred", result);
    }

    @Test
    void timeoutResponse() {
        stubFor(get(urlEqualTo("/data"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withFixedDelay(2000) // 2 секунды
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"title\": \"Too late\"}")));

        String result = client.fetchTitle();

        assertEquals("Timeout", result);
    }

    @Test
    void invalidJsonResponse() {
        stubFor(get(urlEqualTo("/data"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        // некорректный JSON
                        .withBody("{\"title\": \"Missing quote}")));

        String result = client.fetchTitle();

        assertEquals("Parse error", result);
    }

    @Test
    void unexpectedFormatXml() {
        stubFor(get(urlEqualTo("/data"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<root><title>Hello</title></root>")));

        String result = client.fetchTitle();

        assertEquals("Unexpected format", result);
    }

    @Test
    void twoDifferentResponsesInSequence() {
        // Используем сценарий WireMock
        stubFor(get(urlEqualTo("/data"))
                .inScenario("Two calls scenario")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"title\": \"First call\"}"))
                .willSetStateTo("SECOND_CALL"));

        stubFor(get(urlEqualTo("/data"))
                .inScenario("Two calls scenario")
                .whenScenarioStateIs("SECOND_CALL")
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "text/plain")
                        .withBody("Second call error")));

        String first = client.fetchTitle();
        assertEquals("Title: First call", first);

        String second = client.fetchTitle();
        assertEquals("Server error: Second call error", second);
    }
}
