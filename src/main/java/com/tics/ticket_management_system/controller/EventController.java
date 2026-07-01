package com.tics.ticket_management_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tics.ticket_management_system.model.Event;
import com.tics.ticket_management_system.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events") // URL base 
@RequiredArgsConstructor 
public class EventController {

    private final EventService eventService;

    // (GET http://localhost:8080/api/events)
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // (GET http://localhost:8080/api/events/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok) // 200 OK with event data
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }
}