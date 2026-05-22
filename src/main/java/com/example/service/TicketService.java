package com.example.service;

import com.example.domain.*;
import com.example.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository tickets;
    private final CustomerRepository customers;
    private final AgentRepository agents;
    private final AssignmentRepository assignments;

    public TicketService(TicketRepository tickets, CustomerRepository customers,
                         AgentRepository agents, AssignmentRepository assignments) {
        this.tickets = tickets;
        this.customers = customers;
        this.agents = agents;
        this.assignments = assignments;
    }

    public Ticket createTicket(String title, String description, String email, String name, String phone) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title is required");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description is required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email is required");

        Customer customer = customers.findByEmailIgnoreCase(email.trim()).orElseGet(() -> {
            Customer c = new Customer(email.trim(), name, phone);
            return customers.save(c);
        });
        if (name != null && !name.isBlank()) customer.setName(name);
        if (phone != null && !phone.isBlank()) customer.setPhone(phone);

        Ticket ticket = new Ticket(title.trim(), description.trim(), customer);
        return tickets.save(ticket);
    }

    @Transactional(readOnly = true)
    public Optional<Ticket> findById(Long id) {
        return tickets.findById(id).map(t -> {
            org.hibernate.Hibernate.initialize(t.getMessages());
            org.hibernate.Hibernate.initialize(t.getAssignments());
            return t;
        });
    }

    @Transactional(readOnly = true)
    public List<Ticket> queueForAgent(Agent agent) {
        // BR-04 UC-002: agent sees tickets assigned to them or unassigned, only Open / In Progress.
        List<Ticket> all = tickets.findByStatusIn(List.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS));
        return all.stream()
            .filter(t -> t.getAssignedAgent() == null || t.getAssignedAgent().getId().equals(agent.getId()))
            .sorted(Comparator
                .comparingInt((Ticket t) -> -t.getPriority().getRank())
                .thenComparing(Ticket::getCreatedAt, Comparator.reverseOrder()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<Ticket> ticketsForCustomer(String email) {
        return customers.findByEmailIgnoreCase(email)
            .map(tickets::findByCustomerOrderByUpdatedAtDesc)
            .orElseGet(List::of);
    }

    public Ticket assign(Long ticketId, Long agentId) {
        Ticket ticket = tickets.findById(ticketId).orElseThrow(() -> new EntityNotFoundException("Ticket"));
        Agent agent = agents.findById(agentId).orElseThrow(() -> new EntityNotFoundException("Agent"));
        if (!agent.isActive()) throw new IllegalStateException("Cannot assign to inactive agent");

        // close out previous assignment
        ticket.getAssignments().stream()
            .filter(a -> a.getUnassignedAt() == null)
            .forEach(a -> a.setUnassignedAt(Instant.now()));

        Assignment a = new Assignment(ticket, agent);
        ticket.getAssignments().add(a);
        ticket.setAssignedAgent(agent);

        if (ticket.getStatus() == TicketStatus.OPEN) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }
        ticket.touch();
        return ticket;
    }

    public Ticket updateStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = tickets.findById(ticketId).orElseThrow(() -> new EntityNotFoundException("Ticket"));
        // BR-03 UC-005: closing requires Resolved first
        if (newStatus == TicketStatus.CLOSED && ticket.getStatus() != TicketStatus.RESOLVED) {
            throw new IllegalStateException("Ticket must be Resolved before closing");
        }
        ticket.setStatus(newStatus);
        if (newStatus == TicketStatus.RESOLVED) ticket.setResolvedAt(Instant.now());
        ticket.touch();
        return ticket;
    }

    public Ticket updatePriority(Long ticketId, Priority priority) {
        Ticket ticket = tickets.findById(ticketId).orElseThrow(() -> new EntityNotFoundException("Ticket"));
        ticket.setPriority(priority);
        ticket.touch();
        return ticket;
    }

    public Message addAgentMessage(Long ticketId, Agent agent, String content) {
        if (content == null || content.isBlank()) throw new IllegalArgumentException("Message cannot be empty");
        Ticket ticket = tickets.findById(ticketId).orElseThrow(() -> new EntityNotFoundException("Ticket"));
        Message m = new Message(ticket, SenderType.AGENT, agent.getName(), content.trim());
        ticket.getMessages().add(m);
        ticket.touch();
        return m;
    }

    public Message addCustomerMessage(Long ticketId, Customer customer, String content) {
        if (content == null || content.isBlank()) throw new IllegalArgumentException("Message cannot be empty");
        Ticket ticket = tickets.findById(ticketId).orElseThrow(() -> new EntityNotFoundException("Ticket"));
        Message m = new Message(ticket, SenderType.CUSTOMER, customer.getName(), content.trim());
        ticket.getMessages().add(m);
        ticket.touch();
        return m;
    }
}
