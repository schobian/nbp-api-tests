package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import org.junit.Assert;
import utils.ApiClient;

import java.util.List;
import java.util.Map;

public class NbpApiSteps {

    private Response response;
    private final ApiClient apiClient = new ApiClient();
    private Scenario scenario;

    @Before
    public void beforeScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("użytkownik pobiera tabelę kursów walut z API NBP")
    public void fetchExchangeRates() {

        response = apiClient.getExchangeRatesTableA();

        Assert.assertFalse(
                "Rates list is empty",
                getRates().isEmpty()
        );
    }

    @Then("odpowiedź API ma status {int}")
    public void responseStatusCodeShouldBe(int expectedStatusCode) {

        scenario.attach(
                "Response status code: " + response.getStatusCode(),
                "text/plain",
                "API status code"
        );

        Assert.assertEquals(
                "Invalid status code",
                expectedStatusCode,
                response.getStatusCode()
        );
    }

    @Then("wyświetl kurs waluty o kodzie {string}")
    public void displayExchangeRateForCurrencyCode(String code) {

        Map<String, Object> currency =
                findCurrencyByField("code", code);

        scenario.attach(
                "Currency code: " + currency.get("code")
                        + "\nCurrency name: " + currency.get("currency")
                        + "\nRate: " + currency.get("mid"),
                "text/plain",
                "Currency by code"
        );

        Assert.assertEquals(
                "Invalid currency code",
                code,
                currency.get("code")
        );
    }

    @Then("wyświetl kurs waluty o nazwie {string}")
    public void displayExchangeRateForCurrencyName(String currencyName) {

        Map<String, Object> currency =
                findCurrencyByField("currency", currencyName);

        scenario.attach(
                "Currency code: " + currency.get("code")
                        + "\nCurrency name: " + currency.get("currency")
                        + "\nRate: " + currency.get("mid"),
                "text/plain",
                "Currency by name"
        );

        Assert.assertEquals(
                "Invalid currency name",
                currencyName,
                currency.get("currency")
        );
    }

    @Then("wyświetl waluty o kursie powyżej {int}")
    public void displayCurrenciesWithRateGreaterThan(int rate) {

        List<Map<String, Object>> filteredRates = getRates().stream()
                .filter(currency ->
                        ((Number) currency.get("mid")).doubleValue() > rate)
                .toList();

        Assert.assertFalse(
                "No currencies found with rate greater than " + rate,
                filteredRates.isEmpty()
        );
        scenario.attach(
                "Currencies with rate greater than "
                        + rate
                        + ": "
                        + filteredRates.size(),
                "text/plain",
                "Currencies above threshold"
        );

        filteredRates.forEach(currency -> {

            double currencyRate =
                    ((Number) currency.get("mid")).doubleValue();

            Assert.assertTrue(
                    "Invalid currency rate: " + currencyRate,
                    currencyRate > rate
            );
        });
    }

    @Then("wyświetl waluty o kursie poniżej {int}")
    public void displayCurrenciesWithRateLowerThan(int rate) {

        List<Map<String, Object>> filteredRates = getRates().stream()
                .filter(currency ->
                        ((Number) currency.get("mid")).doubleValue() < rate)
                .toList();

        Assert.assertFalse(
                "No currencies found with rate lower than " + rate,
                filteredRates.isEmpty()
        );

        scenario.attach(
                "Currencies with rate lower than "
                        + rate
                        + ": "
                        + filteredRates.size(),
                "text/plain",
                "Currencies below threshold"
        );

        filteredRates.forEach(currency -> {

            double currencyRate =
                    ((Number) currency.get("mid")).doubleValue();

            Assert.assertTrue(
                    "Invalid currency rate: " + currencyRate,
                    currencyRate < rate
            );
        });
    }

    private List<Map<String, Object>> getRates() {

        return response
                .jsonPath()
                .getList("[0].rates");
    }

    private Map<String, Object> findCurrencyByField(
            String field,
            String value
    ) {

        return getRates().stream()
                .filter(rate -> rate.get(field).equals(value))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError(
                                "Currency not found for "
                                        + field
                                        + ": "
                                        + value
                        )
                );
    }
}