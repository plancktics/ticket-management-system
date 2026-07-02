package com.tics.ticket_management_system.exception;

public class TicketPurchaseLimitExceededException extends RuntimeException {
    public TicketPurchaseLimitExceededException(String message) {
        super(message);
    }
}