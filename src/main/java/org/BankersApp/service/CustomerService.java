package org.BankersApp.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.exception.CustomerException;
import org.BankersApp.exception.ResourceNotFoundException;
import org.BankersApp.repository.CustomerRepository;
import org.jboss.logging.Logger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ApplicationScoped
public class CustomerService {
    @Inject
    CustomerRepository customerRepository;

    @Inject
    Logger logger;


    @Transactional
    public CustomerDTO createCustomer(Customer customer) {
        logger.info("createCustomer method called");
        try
        {
             customer.setCreatedAt(Instant.now());
             customer.setModifiedAt(Instant.now());
             customerRepository.persist(customer);
            return entityToDTO(customer);}
        catch (Exception ex)
        {
            logger.error("Failed to create a customer: " + ex.getMessage());
            throw new CustomerException("Failed to create a customer.");
        }
    }

    @Transactional
    public CustomerDTO getCustomerByCustomerId(Long customerId) {
        logger.info("getCustomerByCustomerId method called");
        Customer customers = customerRepository.findById(customerId);

        if(customers!=null)
        {
            return entityToDTO(customers);
        }
        else
        {
            logger.error("Customer not found in the database with ID: " + customerId);
            throw new CustomerException("Customer not found in the database.");
        }
    }

    @Transactional
    public List<CustomerDTO> getCustomerByCriteria(String searchValue) {
        logger.info("getCustomerByCriteria method called");
        List<CustomerDTO> customerList;
        if (searchValue == null || searchValue.isEmpty()) {
            customerList = customerRepository.findAll().stream().map(this::entityToDTO).collect(Collectors.toList());
        } else {
            customerList = customerRepository.getCustomerByCriteria(searchValue).stream().map(this::entityToDTO).collect(Collectors.toList());
        }
        if (!customerList.isEmpty()) {
            return customerList;
        } else {
            logger.error("No customers found in the database with the given Field: " + searchValue);
            throw new CustomerException("No customers found in the database with the given Field.");
        }
    }

    @Transactional
    public CustomerDTO updateCustomer(Customer customer)  {

        Customer existingCustomer = customerRepository.findById(customer.getCustomerId());
        System.out.println("hello");
        if(!Optional.ofNullable(existingCustomer).isEmpty()) {
            existingCustomer.setModifiedAt(Instant.now());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            existingCustomer.setCity(customer.getCity());
            customerRepository.merge(existingCustomer);
            return entityToDTO(existingCustomer);
        } else {
            throw new CustomerException("Customer with ID " + customer.getCustomerId() + " not found");
        }
    }

    @Transactional
    public CustomerDTO deleteCustomer(Long customerId) {
        Customer existingCustomer = customerRepository.findById(customerId);
        if (Optional.ofNullable(existingCustomer).isPresent()) {
            CustomerDTO deletedCustomerDTO = entityToDTO(existingCustomer);
            customerRepository.deleteById(customerId);
            return deletedCustomerDTO;
        } else {
            throw new ResourceNotFoundException("Customer with ID " + customerId + " not found");
        }
    }




    public CustomerDTO entityToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setCity(customer.getCity());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        return customerDTO;
    }


}
