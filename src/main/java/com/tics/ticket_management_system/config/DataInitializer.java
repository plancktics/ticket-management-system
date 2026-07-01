package com.tics.ticket_management_system.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tics.ticket_management_system.model.Event;
import com.tics.ticket_management_system.model.Ticket;
import com.tics.ticket_management_system.model.TicketStatus;
import com.tics.ticket_management_system.repository.EventRepository;
import com.tics.ticket_management_system.repository.TicketRepository;

@Configuration
public class DataInitializer {

    @Bean
    @SuppressWarnings("unused")
    CommandLineRunner initDatabase(EventRepository eventRepository, TicketRepository ticketRepository) {
        return args -> {
            System.out.println("====== POPULATING DATABASE WITH TEST DATA ======");

            // 1. Create and save a Rock Concert Event
            Event rockEvent = new Event();
            rockEvent.setTitle("Rock Concert 2026");
            rockEvent.setDescription("The biggest rock festival of the summer.");
            rockEvent.setDate(LocalDateTime.of(2026, 8, 15, 21, 0));
            rockEvent.setVenue("Olympic Stadium");
            rockEvent.setTotalCapacity(50000);
            
            eventRepository.save(rockEvent);

            // 2. Create and save Tickets for the Rock Concert
            Ticket ticket1 = new Ticket();
            ticket1.setTicketCode("a"); // Generates a unique secure string
            ticket1.setPrice(new BigDecimal("85.50"));
            ticket1.setStatus(TicketStatus.AVAILABLE);
            ticket1.setEvent(rockEvent); // Establishing the @ManyToOne relationship

            Ticket ticket2 = new Ticket();
            ticket2.setTicketCode("b");
            ticket2.setPrice(new BigDecimal("150.00"));
            ticket2.setStatus(TicketStatus.AVAILABLE);
            ticket2.setEvent(rockEvent);

            ticketRepository.save(ticket1);
            ticketRepository.save(ticket2);

            System.out.println("Saved Event: " + rockEvent.getTitle());
            System.out.println("Saved Ticket 1 (Code): " + ticket1.getTicketCode());
            System.out.println("Saved Ticket 2 (Code): " + ticket2.getTicketCode());
            System.out.println("=================================================");
        };
    }
}