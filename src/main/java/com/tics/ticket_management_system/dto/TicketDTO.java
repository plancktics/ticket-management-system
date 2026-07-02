package com.tics.ticket_management_system.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDTO {
    private Long id;
    private String ticketCode;
    private BigDecimal price;
    private String status;
    private String eventTitle;     
    private String purchaserName;  
}