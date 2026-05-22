package com.example.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "messages")
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType senderType;

    private String senderName;

    @Column(nullable = false, length = 8000)
    private String content;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected Message() {}

    public Message(Ticket ticket, SenderType senderType, String senderName, String content) {
        this.ticket = ticket;
        this.senderType = senderType;
        this.senderName = senderName;
        this.content = content;
    }

    public Long getId() { return id; }
    public Ticket getTicket() { return ticket; }
    public SenderType getSenderType() { return senderType; }
    public String getSenderName() { return senderName; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
}
