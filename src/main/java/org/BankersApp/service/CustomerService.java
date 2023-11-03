package org.BankersApp.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.validation.Valid;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.entity.Customer;
import org.BankersApp.exception.CustomeException;
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
        logger.info("created  method service called!!!!!!!!!!!!");
        try
        {
            customer.setCreatedAt(Instant.now());
             customer.setModifiedAt(Instant.now());
             customerRepository.persist(customer);
            return entityToDTO(customer);}
        catch (CustomeException ex)
        {
            throw new CustomeException("Failed to create a data");
        }
    }

    @Transactional
    public List<CustomerDTO> getAllCustomers() {
        logger.info("Service called for getCustomer all");
        List<CustomerDTO> customerList= customerRepository.findAll().stream().map(this::entityToDTO).collect(Collectors.toList());
        logger.info("CustomerList"+customerList);
        if(!customerList.isEmpty())
        {
            return customerList;
        }
        else
        {
            throw new CustomeException("Data not in DB");
        }
    }


    /**
     * @Rohit
     */

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
            throw new CustomeException("Customer with ID " + customer.getCustomerId() + " not found");
        }
    }



    public List<CustomerDTO> getCustomerByCriteria(String searchValue) {
        List<CustomerDTO> customerList = customerRepository.getCustomerByCriteria(searchValue).stream().map(this::entityToDTO).collect(Collectors.toList());
        logger.info("CustomerList++++++" + customerList);
        if (customerList.size() != 0) {
            return customerList;
        } else {
            throw new CustomeException("Not found in DB");
        }
    }

    /**
     * @Rohit
     */

    @Transactional
    public CustomerDTO deleteCustomer(Long customerId){
        Customer existingCustomer = customerRepository.findById(customerId);
        if (!Optional.ofNullable(existingCustomer).isEmpty()) {
            CustomerDTO deletedCustomerDTO = entityToDTO(existingCustomer);
            customerRepository.deleteById(customerId);
            return deletedCustomerDTO;
             } else {
            throw new CustomeException("Customer with ID " + customerId + " not found");
            }
    }

    private CustomerDTO entityToDTO(Customer customer) {
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
