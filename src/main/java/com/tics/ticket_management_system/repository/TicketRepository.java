package com.tics.ticket_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tics.ticket_management_system.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEventId(Long eventId);
}