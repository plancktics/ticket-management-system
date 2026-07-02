package com.tics.ticket_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tics.ticket_management_system.exception.ResourceNotFoundException;
import com.tics.ticket_management_system.exception.TicketAlreadySoldException;
import com.tics.ticket_management_system.model.Ticket;
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

    // @Transactional asegura que si algo falla dentro, se haga un rollback automático
    @Transactional
    public Ticket bookTicket(Long id) { 
        Ticket ticket = ticketRepository.findByIdWithLock(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + id));

        if (ticket.getStatus() == com.tics.ticket_management_system.model.TicketStatus.SOLD) {
            throw new TicketAlreadySoldException("Ticket with ID " + id + " is already sold.");
        }

        ticket.setStatus(com.tics.ticket_management_system.model.TicketStatus.SOLD);
        return ticketRepository.save(ticket);
    }
}