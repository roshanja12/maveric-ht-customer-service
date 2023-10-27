package org.BankersApp.controller;

import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.dto.ErrorMessage;
import org.BankersApp.dto.ResponseDTO;
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
import org.jboss.logging.Logger;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Path("/api/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@OpenAPIDefinition(info = @Info(title = "Customer API", version = "1.0"))
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
    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of customers"),
            @APIResponse(responseCode = "404", description = "Customer list not found"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getAllCustomers(UriInfo uriInfo) throws CustomeException {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        ResponseDTO setResponse = commonUtil.successResponse("Retrived Data successfully",200, uriInfo.getRequestUri().toString(),customers);
        return Response.ok(setResponse).build();
    }

    @POST
    @Operation(summary = "Create a customer", description = "Create a new customer")
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
    public Response createCustomer( Customer customer ,UriInfo uriInfo) throws CustomeException {
    Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        if (!violations.isEmpty()) {
            List<ErrorMessage> errorList = new ArrayList<>();
            for (ConstraintViolation<Customer> violation : violations) {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                ErrorMessage error = new ErrorMessage(field, message);
                errorList.add(error);
            }
                ResponseDTO responseDTO = new ResponseDTO();
                responseDTO.setStatus("Failed");
                responseDTO.setCode(Response.Status.BAD_REQUEST.getStatusCode());
                responseDTO.setMsg("Validation failed");
                responseDTO.setPath(uriInfo.getPath());
                responseDTO.setTimestamp(Instant.now());
                responseDTO.setErrors(errorList);

            return Response.status(Response.Status.BAD_REQUEST)
                .entity(responseDTO)
                .build();
        }
        else {
            logger.info("created  method controller called!!!!!!!!!!!!");
            CustomerDTO createdCustomer = customerService.createCustomer(customer);
            if (createdCustomer != null) {
                ResponseDTO responseDTO = new ResponseDTO();
                responseDTO.setStatus("Success");
                responseDTO.setCode(Response.Status.CREATED.getStatusCode());
                responseDTO.setMsg("Successfully Created");
                responseDTO.setPath(uriInfo.getPath());
                responseDTO.setTimestamp(Instant.now());
                responseDTO.setErrors(null);
                responseDTO.setData(createdCustomer);
                return Response.status(Response.Status.CREATED)
                        .entity(responseDTO)
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GET
    @Path("/search")
    @Operation(summary = "Search for customers", description = "Search for customers based on various criteria")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer(s) found"),
            @APIResponse(responseCode = "404", description = "Customer not found"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public List<CustomerDTO> getCustomerByCriteria
            (@QueryParam("searchValue") String searchValue)  {
        logger.info("get customer by id called !!!!!!!!!!!!!!!!!!!>>>>>>>>>>");
        return customerService.getCustomerByCriteria(searchValue);
//        ResponseDTO setResponse = commonUtil.successResponse("created",200,customers);

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


