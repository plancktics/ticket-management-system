package com.tics.ticket_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // @Transactional asegura que si algo falla dentro, se haga un rollback automático
    @Transactional 
    public Optional<Ticket> bookTicket(Long id) {
        // 1. Buscamos usando el método con bloqueo pesimista
        Optional<Ticket> ticketOptional = ticketRepository.findByIdWithLock(id);

        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            
            if (ticket.getStatus() == TicketStatus.AVAILABLE) {
                ticket.setStatus(TicketStatus.SOLD);
                return Optional.of(ticketRepository.save(ticket));
            }
        }
        
        return Optional.empty();
    }
}