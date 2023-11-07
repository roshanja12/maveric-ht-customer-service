package org.BankersApp.service;


import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.exception.CustomerException;
import org.BankersApp.exception.ResourceNotFoundException;
import org.BankersApp.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * @author ankushk
 */
@QuarkusTest
public class CustomerServiceTest {

    @Inject
    CustomerService customerService;

    @InjectMock
    CustomerRepository customerRepository;


    @Test
    public void testCreateCustomerSuccess() {

        Customer customer = new Customer();
        customer.setFirstName("FirstName");
        customer.setEmail("test@test.com");

        doNothing().when(customerRepository).persist(any(Customer.class));

        CustomerDTO customerDTO = customerService.createCustomer(customer);

        verify(customerRepository).persist(customer);

        assertEquals("FirstName", customerDTO.getFirstName());
        assertEquals("test@test.com", customerDTO.getEmail());
    }

    @Test
    public void testCreateCustomerFailed() {
        Customer customer = new Customer();

        doThrow(new CustomerException("Failed to create a data")).when(customerRepository).persist(customer);

        assertThrows(CustomerException.class, () -> customerService.createCustomer(customer));
    }

    @Test
    public void testGetCustomerByCriteriaSuccess() {
        String searchValue = "searchValue";
        List<Customer> customerList=new ArrayList<>();
        customerList.add(new Customer(1l,"FirstName","LastName","test@test.com",9899989898l,"City", Instant.now(), Instant.now()));
        customerList.add(new Customer(2l,"FirstName","LastName","test@test.com",9899989898l,"City",Instant.now(), Instant.now()));

        Mockito.when(customerRepository.getCustomerByCriteria(searchValue)).thenReturn(customerList);

        List<CustomerDTO> result = customerService.getCustomerByCriteria(searchValue);

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCustomerByCriteriaFailed() {
        String searchValue = "searchValue";
        when(customerRepository.getCustomerByCriteria(searchValue)).thenReturn(Collections.emptyList());

        CustomerException exception = assertThrows(CustomerException.class, () -> {
            customerService.getCustomerByCriteria(searchValue);
        });

        assertEquals("No customers found in the database with the given Field.", exception.getMessage());
    }

    @Test
    public void testGetCustomerByCustomerId_Sucess() {
        Long customerId = 1L;
        Customer mockCustomer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(mockCustomer);
        CustomerDTO result = customerService.getCustomerByCustomerId(customerId);
        assertNotNull(result);
    }

    @Test
    public void testGetCustomerByCustomerId_Failed() {
        Long customerId = 20L;
        when(customerRepository.findById(customerId)).thenReturn(null);

        CustomerException exception = assertThrows(CustomerException.class, () -> {
            customerService.getCustomerByCustomerId(customerId);
        });
        assertEquals("Customer not found in the database.", exception.getMessage());
    }
    /**
     * @author rohit
     */

    @Test
    public void testDeleteCustomerSuccess() {
        Long customerId = 1L;
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(existingCustomer);
        CustomerDTO deletedCustomerDTO = customerService.deleteCustomer(customerId);
        assertNotNull(deletedCustomerDTO);
        assertEquals(customerId, deletedCustomerDTO.getCustomerId());
    }

    @Test
    public void testDeleteCustomerNotFound() {
        Long customerId = 20L;
        when(customerRepository.findById(customerId)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(customerId));
    }

    @Test
    public void testUpdateCustomerSuccess() {
        Long customerId = 1L;
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerId(customerId);
        updatedCustomer.setEmail("rohan123@gmail.com");
        updatedCustomer.setPhoneNumber(Long.valueOf("9403821914"));
        updatedCustomer.setCity("Delhi");
        when(customerRepository.findById(customerId)).thenReturn(updatedCustomer);
        CustomerDTO updatedCustomerDTO = customerService.updateCustomer(updatedCustomer);
        assertNotNull(updatedCustomerDTO);
        assertEquals("rohan123@gmail.com", updatedCustomerDTO.getEmail());
        assertEquals(Long.valueOf("9403821914"), updatedCustomerDTO.getPhoneNumber());
        assertEquals("Delhi", updatedCustomerDTO.getCity());
    }

    @Test
    public void testUpdateCustomerNotFound() {
        Long customerId = 20L;
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        when(customerRepository.findById(customerId)).thenReturn(null);
        assertThrows(CustomerException.class, () -> customerService.updateCustomer(new Customer()));
    }


}