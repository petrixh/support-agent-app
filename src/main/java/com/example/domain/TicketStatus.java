package com.example.domain;

public enum TicketStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved"),
    CLOSED("Closed");

    private final String label;
    TicketStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
}
