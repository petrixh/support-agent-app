package com.example.ui;

import com.example.domain.Priority;
import com.example.domain.TicketStatus;
import com.vaadin.flow.component.html.Span;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class UiUtils {
    private static final DateTimeFormatter TS =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());

    private UiUtils() {}

    public static Span priorityBadge(Priority p) {
        Span s = new Span(p.getLabel());
        s.getElement().getThemeList().add("badge");
        s.getStyle()
            .set("padding", "var(--vaadin-padding-block-container) var(--vaadin-padding-inline-container)")
            .set("border-radius", "var(--vaadin-radius-s)")
            .set("font-size", "var(--aura-font-size-xs)")
            .set("font-weight", "var(--aura-font-weight-medium)")
            .set("white-space", "nowrap");
        switch (p) {
            case URGENT -> {
                s.getStyle().set("background", "var(--aura-red)").set("color", "var(--aura-red-text)");
            }
            case HIGH -> {
                s.getStyle().set("background", "var(--aura-orange)").set("color", "var(--aura-orange-text)");
            }
            case MEDIUM -> {
                s.getStyle().set("background", "var(--aura-yellow)").set("color", "var(--aura-yellow-text)");
            }
            case LOW -> {
                s.getStyle().set("background", "var(--vaadin-background-container)").set("color", "var(--vaadin-text-color-secondary)");
            }
        }
        return s;
    }

    public static Span statusBadge(TicketStatus st) {
        Span s = new Span(st.getLabel());
        s.getStyle()
            .set("padding", "var(--vaadin-padding-block-container) var(--vaadin-padding-inline-container)")
            .set("border-radius", "var(--vaadin-radius-s)")
            .set("font-size", "var(--aura-font-size-xs)")
            .set("font-weight", "var(--aura-font-weight-medium)")
            .set("white-space", "nowrap");
        switch (st) {
            case OPEN -> s.getStyle().set("background", "var(--aura-blue)").set("color", "var(--aura-blue-text)");
            case IN_PROGRESS -> s.getStyle().set("background", "var(--aura-orange)").set("color", "var(--aura-orange-text)");
            case RESOLVED -> s.getStyle().set("background", "var(--aura-green)").set("color", "var(--aura-green-text)");
            case CLOSED -> s.getStyle().set("background", "var(--vaadin-background-container)").set("color", "var(--vaadin-text-color-secondary)");
        }
        return s;
    }

    public static String formatTimestamp(Instant t) {
        return t == null ? "" : TS.format(t);
    }

    public static String timeAgo(Instant t) {
        if (t == null) return "";
        Duration d = Duration.between(t, Instant.now());
        long sec = d.getSeconds();
        if (sec < 60) return sec + "s ago";
        if (sec < 3600) return (sec / 60) + "m ago";
        if (sec < 86400) return (sec / 3600) + "h ago";
        return (sec / 86400) + "d ago";
    }
}
