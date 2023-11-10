package org.BankersApp.controller;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.dto.ErrorMessage;
import org.BankersApp.service.KafkaService;
import org.BankersApp.util.CommonUtil;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.BankersApp.entity.Customer;
import org.BankersApp.service.CustomerService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Path("/api/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name="customers_API")
public class CustomerController {

    @Inject
    CustomerService customerService;

    @Inject
    Validator validator;

    @Inject
    CommonUtil commonUtil;

    @Inject
    Logger logger;

    @Inject
    UriInfo uriInfo;

    @Inject
    KafkaService kafkaService;

    @GET
    @Path("/searchByCustomerId")
    @Operation(summary = "Get all customers", description = "This API endpoint retrieves a list of all customers in the system.")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "List of customers",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)
                    )
            ),
            @APIResponse(responseCode = "404", description = "Customer list not found"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getCustomerByCustomerId(@QueryParam ("customerId") Long customerId) {
        logger.info("getCustomerByCustomerId method called with customerId: " + customerId);
        CustomerDTO customers = customerService.getCustomerByCustomerId(customerId);
        return commonUtil.buildSuccessResponse("Retrieve data successfully", Response.Status.OK,null, customers, uriInfo);
    }

    @POST
    @Operation(summary = "Create a customer", description = "This API endpoint allows you to create a new customer in the system")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Customer created successfully",  content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDTO.class)
            )),
            @APIResponse(responseCode = "400", description = "Validation error"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    @RequestBody(
            content = @Content(
                    schema = @Schema(implementation = Customer.class),
                    mediaType = "application/json")
    )
    public Response createCustomer ( Customer customer ) {
    Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty()) {
            List<ErrorMessage> errorList = violations.stream()
                    .map(violation -> new ErrorMessage(violation.getPropertyPath().toString(), violation.getMessage()))
                    .collect(Collectors.toList());
            logger.error("Validation failed: " + errorList);
            return commonUtil.buildErrorResponse("Validation failed", Response.Status.BAD_REQUEST, errorList,null,uriInfo);
        }
        else {
            logger.info("createCustomer method controller called");
            CustomerDTO createdCustomer = customerService.createCustomer(customer);
            if (createdCustomer != null) {
                kafkaService.customerProducer(createdCustomer);
                return commonUtil.buildSuccessResponse("Created data successfully", Response.Status.CREATED,null, createdCustomer, uriInfo);
            } else {
                logger.error("Failed to create customer");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GET
    @Path("/search")
    @Operation(summary = "Search for customers", description = "Search for customers based on various criteria, such as a search value ")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Customer(s) found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)
                    )
            ),
            @APIResponse(responseCode = "404", description = "Customer not found"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getCustomerByCriteria
            (@QueryParam("searchValue") String searchValue)  {
        logger.info("getCustomerByCriteria method called with searchValue: " + searchValue);
        List<CustomerDTO> customers= customerService.getCustomerByCriteria(searchValue);
        return commonUtil.buildSuccessResponse("Retrieve data successfully", Response.Status.OK,null, customers, uriInfo);
    }

    @PUT
    @Path("/{customerId}")
    @Operation(summary = "Update a customer", description = "This API updates an existing customer by CustomerID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer updated successfully", content = @Content(
                    schema = @Schema(implementation = Customer.class)
            )),
            @APIResponse(responseCode = "404", description = "Customer not found"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response updateCustomer(@PathParam("customerId") Long id, Customer customer, UriInfo uriInfo) {
        customer.setCustomerId(id);
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty()) {
            List<ErrorMessage> errorList = violations.stream()
                    .map(violation -> new ErrorMessage(violation.getPropertyPath().toString(), violation.getMessage()))
                    .collect(Collectors.toList());
            return commonUtil.buildErrorResponse("Validation failed", Response.Status.BAD_REQUEST, errorList, null, uriInfo);
        } else {
            CustomerDTO updatedCustomer = customerService.updateCustomer(customer);
            if (updatedCustomer != null) {
                logger.info("Customer with ID " + id + " updated successfully.");
                return commonUtil.buildSuccessResponse("Updated Successfully", Response.Status.OK, null, updatedCustomer, uriInfo);
            }
            logger.error("Failed to update customer with ID: " + id);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DELETE
    @Path("/{customerId}")
    @Operation(summary = "Delete a customer", description = "This API deletes an existing customer by CustomerID")

    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer deleted successfully",content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)
            )),
            @APIResponse(responseCode = "404", description = "Customer not found",content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)
            )),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class),
                    examples = @ExampleObject(value = "{\"customerId\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john@example.com\", \"phoneNumber\": 1234567890, \"city\": \"New York\"}")
            )
    )

    public Response deleteCustomer(@PathParam("customerId") Long customerId, UriInfo uriInfo) {
        CustomerDTO deletedCustomer = customerService.deleteCustomer(customerId);
        if (deletedCustomer != null) {
            logger.info("Customer with ID " + customerId + " deleted successfully.");
            return commonUtil.buildSuccessResponse("Customer deleted successfully.", Response.Status.OK, null, deletedCustomer,uriInfo);
        } else {
            logger.error("Failed to delete customer with ID: " + customerId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();            }
    }





}


