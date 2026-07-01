package com.tics.ticket_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tics.ticket_management_system.model.Ticket;
import com.tics.ticket_management_system.model.TicketStatus;
import com.tics.ticket_management_system.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Optional<Ticket> bookTicket(Long id) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);

        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            
            if (ticket.getStatus() == TicketStatus.AVAILABLE) {
                ticket.setStatus(TicketStatus.SOLD); 
                return Optional.of(ticketRepository.save(ticket)); // Save the updated ticket in H2 and return it
            }
        }
        
        return Optional.empty();
    }
}