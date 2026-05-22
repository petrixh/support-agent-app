package com.example.domain;

public enum Priority {
    LOW("Low", 0),
    MEDIUM("Medium", 1),
    HIGH("High", 2),
    URGENT("Urgent", 3);

    private final String label;
    private final int rank;

    Priority(String label, int rank) {
        this.label = label;
        this.rank = rank;
    }

    public String getLabel() { return label; }
    public int getRank() { return rank; }
}
