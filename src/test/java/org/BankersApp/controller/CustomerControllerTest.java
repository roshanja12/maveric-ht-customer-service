package org.BankersApp.controller;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@QuarkusTest
public class CustomerControllerTest {

    @InjectMock
    CustomerService customerService;

    @Inject
    CustomerController customerController;

    @Inject
    Validator validator;

    @InjectMock
    UriInfo uriInfo;

    @Test
    public void testCreateCustomerSuccess() {
        Customer customer = new Customer();
        customer.setFirstName("FirstName");
        customer.setLastName("LastName");
        customer.setEmail("Test@test.com");
        customer.setCity("City");
        customer.setPhoneNumber(9657395789L);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setCity(customer.getCity());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());

        Mockito.when(customerService.createCustomer(customer)).thenReturn(customerDTO);

        Response response = customerController.createCustomer(customer);

        assertNotNull(response);
        assertNotNull(response.getEntity());
    }


    @Test
    public void testCreateCustomerValidationFailure() {
        Customer invalidCustomer = new Customer();

        Response response = customerController.createCustomer(invalidCustomer);

        Mockito.verify(customerService, Mockito.never()).createCustomer(Mockito.any());
    }


    @Test
    public void testCreateCustomerFailure() {
        Customer customer = new Customer();
        customer.setFirstName("FirstName");
        customer.setLastName("LastName");
        customer.setEmail("Test@test.com");
        customer.setCity("City");
        customer.setPhoneNumber(9657395789L);


        Mockito.when(customerService.createCustomer(customer)).thenReturn(null);

        Response response = customerController.createCustomer(customer);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetCustomerByCustomerId_Success()  {
        Customer customer = new Customer();
        customer.setFirstName("FirstName");
        customer.setLastName("LastName");
        customer.setEmail("Test@test.com");
        customer.setCity("City");
        customer.setPhoneNumber(9657395789L);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setCity(customer.getCity());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());


        Mockito.when(customerService.getCustomerByCustomerId(customer.getCustomerId())).thenReturn(customerDTO);

        Response response = customerController.getCustomerByCustomerId(customer.getCustomerId());

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetCustomerByCriteria_Success() {
        String searchValue = "city";
        List<CustomerDTO> customers = new ArrayList<>();
        customers.add(new CustomerDTO());
        Mockito.when(customerService.getCustomerByCriteria(searchValue)).thenReturn(customers);

        Response response = customerController.getCustomerByCriteria(searchValue);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }

    /**
     * @author rohit
     */

    @Test
    public void testDeleteCustomerSuccess() {
        Long customerId = 1L;
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
        Long customerId = 10L;
        UriInfo uriInfo = mock(UriInfo.class);
        when(customerService.deleteCustomer(customerId)).thenReturn(null);
        Response response = customerController.deleteCustomer(customerId, uriInfo);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }


    @Test
    public void testUpdateCustomerSuccess() {
        Long customerId = 1L;
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
        Long customerId = 10L;
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
