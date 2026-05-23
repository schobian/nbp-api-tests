package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps"},
        plugin = {
                "pretty",
                "summary",
                "html:reports/cucumber-report.html",
                "json:reports/cucumber.json",
                "timeline:reports/timeline"
        },
        monochrome = true
)
public class TestRunner {
}