package org.BankersApp;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.BankersApp.controller.CustomerControllerTest;

@QuarkusIntegrationTest
public class CustomerTestIT extends CustomerControllerTest {
    // Execute the same tests but in packaged mode.
}
