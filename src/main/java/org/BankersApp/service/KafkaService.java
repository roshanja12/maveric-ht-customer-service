package org.BankersApp.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.BankersApp.dto.CustomerDTO;
import org.BankersApp.dto.KafkaRequestDto;
import org.BankersApp.entity.InsightsCustomers;
import org.BankersApp.entity.Type;
import org.BankersApp.exception.CustomerException;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 * @author ankushk
 */
@ApplicationScoped
public class KafkaService {

    @Inject
    Logger logger;

    @Channel("insights-events")
    Emitter<KafkaRequestDto> quoteRequestEmitter;

    public void customerProducer(CustomerDTO customerDTO)
    {
        try{
            KafkaRequestDto kafkaRequestDto= convertToInsightsCustomers(customerDTO);
            quoteRequestEmitter.send(kafkaRequestDto);
        }
        catch (Exception ex)
        {
            logger.error("Failed to send a kafka message" + ex.getMessage());
            throw new CustomerException("Failed to send a kafka message");
        }
    }

    private KafkaRequestDto convertToInsightsCustomers(CustomerDTO customerDTO) {
        InsightsCustomers insightsCustomers = new InsightsCustomers();

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        insightsCustomers.setYear(String.valueOf(zonedDateTime.getYear()));
        String month = zonedDateTime.getMonth().toString();
        month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
        insightsCustomers.setMonth(month);
        insightsCustomers.setCustomerId(customerDTO.getCustomerId());
        insightsCustomers.setCity(customerDTO.getCity());
        insightsCustomers.setCreatedAt(Instant.now());
        insightsCustomers.setType(Type.CUSTOMER_CREATED);
        KafkaRequestDto requestDto = new KafkaRequestDto();
        requestDto.setMessage(insightsCustomers);
        requestDto.setType(Type.CUSTOMER_CREATED);
        return requestDto;
    }
}
