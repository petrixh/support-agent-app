package com.example.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "assignments")
public class Assignment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @Column(nullable = false)
    private Instant assignedAt = Instant.now();

    private Instant unassignedAt;

    protected Assignment() {}

    public Assignment(Ticket ticket, Agent agent) {
        this.ticket = ticket;
        this.agent = agent;
    }

    public Long getId() { return id; }
    public Ticket getTicket() { return ticket; }
    public Agent getAgent() { return agent; }
    public Instant getAssignedAt() { return assignedAt; }
    public Instant getUnassignedAt() { return unassignedAt; }
    public void setUnassignedAt(Instant unassignedAt) { this.unassignedAt = unassignedAt; }
}
