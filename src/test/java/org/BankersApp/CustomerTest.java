package org.BankersApp;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.BankersApp.controller.CustomerController;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.repository.CustomerRepository;
import org.BankersApp.service.CustomerService;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.mockito.Mockito;

import java.util.Collections;

/**
 * @author ankushk
 */
@QuarkusTest
public class CustomerTest {

    @InjectMock
    CustomerService customerService;

    @Inject
    CustomerController customerController;
    @Inject
    Validator validator;

    @Test
    public void testGetAllCustomersForSuccess() {

        given()
                .when().get("/api/v1/customers")
                .then()
                .statusCode(200)
                .body("size()", not(0));
    }
    @Test
    public void testGetAllCustomersForFailed() {
        given()
                .when().get("/api/v1/customers")
                .then()
                .statusCode(404)
                .body("errors[0].message", is("Data not in DB")); // Match the error message within the response body
    }

    @Test
    public void testCreateSuccess()
    {
        Customer customer= new Customer();
        customer.setFirstName("Ankush");
        customer.setLastName("Kadam");
        customer.setEmail("ankush.kadam@gmail.com");
        customer.setCity("Pune");
        customer.setPhoneNumber(9657395789l);
        CustomerDTO customerDTO = customerService.createCustomer(customer);
        assertEquals("Ankush", customerDTO.getFirstName());
        assertNotNull(customerDTO);
    }


    @Test
    public void testCreateCustomerSuccess() {
        Customer validCustomer = new Customer();
        UriInfo uriInfo = Mockito.mock(UriInfo.class);

        CustomerDTO customerDTO = new CustomerDTO();
        Mockito.when(customerService.createCustomer(validCustomer)).thenReturn(customerDTO);

        Response response = customerController.createCustomer(validCustomer, uriInfo);
        Mockito.verify(customerService).createCustomer(validCustomer);
    }
    @Test
    public void testCreateCustomerValidationFailure() {
        Customer invalidCustomer = new Customer();
        UriInfo uriInfo = Mockito.mock(UriInfo.class);

        Response response = customerController.createCustomer(invalidCustomer, uriInfo);
        Mockito.verify(customerService, Mockito.never()).createCustomer(Mockito.any());
    }


}
