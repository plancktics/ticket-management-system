package com.tics.ticket_management_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tics.ticket_management_system.model.Ticket;

import jakarta.persistence.LockModeType;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Concurrency control: Lock the ticket record for update to prevent concurrent modifications
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Ticket t WHERE t.id = :id")
    Optional<Ticket> findByIdWithLock(@Param("id") Long id);
}