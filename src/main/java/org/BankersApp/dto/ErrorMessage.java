package org.BankersApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

        private String field;
        private String message;

        public ErrorMessage(String message) {
                this.message = message;
        }
}