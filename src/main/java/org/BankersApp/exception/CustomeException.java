package org.BankersApp.exception;

import lombok.Data;

/**
 * @author ankushk
 */

public class CustomeException extends RuntimeException{

    public CustomeException(String message){
        super(message);
    }


}
