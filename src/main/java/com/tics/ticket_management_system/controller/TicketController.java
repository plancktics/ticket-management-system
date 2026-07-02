package com.tics.ticket_management_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tics.ticket_management_system.model.Ticket;
import com.tics.ticket_management_system.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tickets") // URL base para las entradas
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // (GET http://localhost:8080/api/tickets)
    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    // (POST http://localhost:8080/api/tickets/{id}/book)
    @PostMapping("/{id}/book")
    public ResponseEntity<Ticket> bookTicket(
            @PathVariable Long id,
            @RequestParam Long userId) { // (?userId=1)

        Ticket bookedTicket = ticketService.bookTicket(id, userId);
        return ResponseEntity.ok(bookedTicket);
    }
}
