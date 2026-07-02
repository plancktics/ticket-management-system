package com.tics.ticket_management_system.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tics.ticket_management_system.dto.TicketDTO;
import com.tics.ticket_management_system.exception.InsufficientBalanceException;
import com.tics.ticket_management_system.exception.ResourceNotFoundException;
import com.tics.ticket_management_system.exception.TicketAlreadySoldException;
import com.tics.ticket_management_system.exception.TicketPurchaseLimitExceededException;
import com.tics.ticket_management_system.model.Ticket;
import com.tics.ticket_management_system.model.TicketStatus;
import com.tics.ticket_management_system.model.User;
import com.tics.ticket_management_system.repository.TicketRepository;
import com.tics.ticket_management_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Ticket> getTicketById(Long id) { //No locks
        return ticketRepository.findById(id);
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setTicketCode(ticket.getTicketCode());
        dto.setPrice(ticket.getPrice());
        dto.setStatus(ticket.getStatus().name());

        if (ticket.getEvent() != null) {
            dto.setEventTitle(ticket.getEvent().getTitle());
        }

        if (ticket.getUser() != null) {
            dto.setPurchaserName(ticket.getUser().getName());
        } else {
            dto.setPurchaserName("None (Available)");
        }

        return dto;
    }

    // @Transactional asegura que si algo falla dentro, se haga un rollback automático
    @Transactional
    public TicketDTO bookTicket(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Ticket ticket = ticketRepository.findByIdWithLock(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + id));

        if (ticket.getStatus() == TicketStatus.SOLD) {
            throw new TicketAlreadySoldException("Ticket with ID " + id + " is already sold.");
        }

        //Max. 4 tickets per user per event
        long ticketsAlreadyOwned = user.getTickets().stream()
                .filter(t -> t.getEvent().getId().equals(ticket.getEvent().getId()))
                .count();
        if (ticketsAlreadyOwned >= 4) {
            throw new TicketPurchaseLimitExceededException("User has reached the limit of 4 tickets for this event.");
        }

        // Validate enough balance
        if (user.getBalance().compareTo(ticket.getPrice()) < 0) {
            throw new InsufficientBalanceException("User does not have enough balance to purchase this ticket.");
        }

        // Deduct ticket price form user balance
        user.setBalance(user.getBalance().subtract(ticket.getPrice()));

        ticket.setStatus(TicketStatus.SOLD);
        ticket.setUser(user);
        Ticket savedTicket = ticketRepository.save(ticket);
        return this.convertToDTO(savedTicket);
    }
}
