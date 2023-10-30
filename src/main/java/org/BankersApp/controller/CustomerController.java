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
import org.BankersApp.exception.CustomeException;
import org.BankersApp.util.CommonUtil;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
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

    @GET
    @Operation(summary = "Get all customers", description = "This API endpoint retrieves a list of all customers in the system.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of customers"),
            @APIResponse(responseCode = "404", description = "Customer list not found"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getAllCustomers(UriInfo uriInfo) throws CustomeException {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return commonUtil.buildSuccessResponse("Retrive data successfully", Response.Status.OK,null, customers, uriInfo);
    }

    @POST
    @Operation(summary = "Create a customer", description = "This API endpoint allows you to create a new customer in the system")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Customer created successfully"),
            @APIResponse(responseCode = "400", description = "Validation error"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    @RequestBody(
            content = @Content(
                    schema = @Schema(implementation = Customer.class),
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n" +
                            "  \"firstName\": \"string\",\n" +
                            "  \"lastName\": \"string\",\n" +
                            "  \"email\": \"string\",\n" +
                            "  \"phoneNumber\": 0,\n" +
                            "  \"city\": \"string\"\n" +
                            "}")
            )
    )
    public Response createCustomer( Customer customer ,UriInfo uriInfo) {
    Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty()) {
            List<ErrorMessage> errorList = violations.stream()
                    .map(violation -> new ErrorMessage(violation.getPropertyPath().toString(), violation.getMessage()))
                    .collect(Collectors.toList());
              return commonUtil.buildErrorResponse("Validation failed", Response.Status.BAD_REQUEST, errorList,null,uriInfo);
        }
        else {
            logger.info("created  method controller called!!!!!!!!!!!!");
            CustomerDTO createdCustomer = customerService.createCustomer(customer);
            if (createdCustomer != null) {
                return commonUtil.buildSuccessResponse("Created data successfully", Response.Status.BAD_REQUEST,null, createdCustomer, uriInfo);
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GET
    @Path("/search")
    @Operation(summary = "Search for customers", description = "Search for customers based on various criteria, such as a search value ")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer(s) found"),
            @APIResponse(responseCode = "404", description = "Customer not found"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getCustomerByCriteria
            (@QueryParam("searchValue") String searchValue,UriInfo uriInfo)  {
        logger.info("get customer by id called !!!!!!!!!!!!!!!!!!!>>>>>>>>>>");
        List<CustomerDTO> customers= customerService.getCustomerByCriteria(searchValue);
        return commonUtil.buildSuccessResponse("Retrive data successfully", Response.Status.OK,null, customers, uriInfo);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a customer", description = "Update an existing customer by CustomerID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer updated successfully"),
            @APIResponse(responseCode = "404", description = "Customer not found"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response updateCustomer( @PathParam("id") Long id ,Customer customer) throws CustomeException  {
        customer.setCustomerId(id);
        CustomerDTO updatedCustomer = customerService.updateCustomer(customer);
        if (updatedCustomer != null) {
            return Response.ok(updatedCustomer).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @DELETE
    @Path("/{customerId}")
    @Operation(summary = "Delete a customer", description = "Delete an existing customer by CustomerID")

    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer deleted successfully"),
            @APIResponse(responseCode = "404", description = "Customer not found"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response deleteCustomer(@PathParam("customerId") Long customerId) throws CustomeException {
        String message = customerService.deleteCustomer(customerId);
        return Response.ok().entity("{\"message\":\"" + message + "\"}").build();
    }


    }


