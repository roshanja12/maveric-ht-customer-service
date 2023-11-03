package org.BankersApp.controller;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.exception.CustomeException;
import org.BankersApp.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
        customer.setFirstName("Ankush");
        customer.setLastName("Kadam");
        customer.setEmail("ankush.kadam@gmail.com");
        customer.setCity("Pune");
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
        customer.setFirstName("Ankush");
        customer.setLastName("Kadam");
        customer.setEmail("ankush.kadam@gmail.com");
        customer.setCity("Pune");
        customer.setPhoneNumber(9657395789L);

        Mockito.when(customerService.createCustomer(customer)).thenReturn(null);

        Response response = customerController.createCustomer(customer);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetAllCustomers_Success()  {

        List<CustomerDTO> customers = new ArrayList<>();
        customers.add(new CustomerDTO());
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);

        Response response = customerController.getAllCustomers();

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
    }
