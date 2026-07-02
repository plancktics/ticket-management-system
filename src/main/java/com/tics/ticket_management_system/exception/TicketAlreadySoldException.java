package com.tics.ticket_management_system.exception;

public class TicketAlreadySoldException extends RuntimeException {
    public TicketAlreadySoldException(String message) {
        super(message);
    }
}