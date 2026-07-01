package com.tics.ticket_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tics.ticket_management_system.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}