package org.BankersApp;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.BankersApp.controller.CustomerController;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * @Rohit
 */

@QuarkusTest
public class CustomerControllerTest {


    @InjectMock
    UriInfo uriInfo;

    @Inject
    CustomerController customerController;


    @InjectMock
    CustomerService customerService;

    @Test
    public void testDeleteCustomerSuccess() {
        Long customerId = 10L;
        UriInfo uriInfo = mock(UriInfo.class);
        CustomerDTO deletedCustomerDTO = new CustomerDTO();
        deletedCustomerDTO.setCustomerId(customerId);
        deletedCustomerDTO.setFirstName("Rohit");
        when(customerService.deleteCustomer(customerId)).thenReturn(deletedCustomerDTO);
        Response response = customerController.deleteCustomer(customerId, uriInfo);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDeleteglobalexception() {
        Long customerId = 2L;
        UriInfo uriInfo = mock(UriInfo.class);
        when(customerService.deleteCustomer(customerId)).thenReturn(null);
        Response response = customerController.deleteCustomer(customerId, uriInfo);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }


    @Test
    public void testUpdateCustomerSuccess() {
        Long customerId = 11L;
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("johndoe@example.com");
        customer.setPhoneNumber(Long.valueOf("9403821914"));
        customer.setCity("New York");
        customer.setCreatedAt(Instant.now());
        customer.setModifiedAt(Instant.now());
        CustomerDTO updatedCustomer = new CustomerDTO();
        Mockito.when(customerService.updateCustomer(customer)).thenReturn(updatedCustomer);
        Response response = customerController.updateCustomer(customerId, customer, uriInfo);
        assertEquals(200, response.getStatus());
        Mockito.verify(customerService, Mockito.times(1)).updateCustomer(customer);
        Mockito.reset(customerService);
    }

    @Test
    public void testUpdateCustomerFailure() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setFirstName(null);
        customer.setLastName(null);
        customer.setEmail(null);
        customer.setPhoneNumber(null);
        customer.setCity(null);
        customer.setCreatedAt(null);
        customer.setModifiedAt(null);
        Mockito.when(customerService.updateCustomer(customer)).thenReturn(null);
        Response response = customerController.updateCustomer(customerId, customer, uriInfo);
        assertEquals(400, response.getStatus());
    }
}

