package com.example.ui;

import com.example.domain.*;
import com.example.repository.CustomerRepository;
import com.example.service.AgentService;
import com.example.service.TicketService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

import java.util.EnumSet;
import java.util.List;

@Route(value = "ticket", layout = MainLayout.class)
@PageTitle("Ticket")
@AnonymousAllowed
public class TicketDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private final TicketService ticketService;
    private final AgentService agentService;
    private final AuthenticationContext auth;
    private final CustomerRepository customers;

    private Long ticketId;

    public TicketDetailView(TicketService ticketService, AgentService agentService,
                            AuthenticationContext auth, CustomerRepository customers) {
        this.ticketService = ticketService;
        this.agentService = agentService;
        this.auth = auth;
        this.customers = customers;
        setSizeFull();
        setPadding(true);
    }

    @Override
    public void setParameter(BeforeEvent event, Long id) {
        this.ticketId = id;
        render();
    }

    private void render() {
        removeAll();
        Ticket ticket = ticketService.findById(ticketId).orElse(null);
        if (ticket == null) {
            add(new H2("Ticket not found"));
            return;
        }

        boolean isAgent = auth.isAuthenticated();
        boolean isCustomerOwner = !isAgent && ticket.getCustomer().getEmail().equalsIgnoreCase(CustomerSession.getEmail());

        if (!isAgent && !isCustomerOwner) {
            add(new H2("Sign in required"));
            Paragraph p = new Paragraph("Provide the email used to create this ticket on the My Tickets page.");
            add(p);
            return;
        }

        // Header
        H2 title = new H2("#" + ticket.getId() + " — " + ticket.getTitle());
        title.getStyle().set("margin", "0");
        HorizontalLayout badges = new HorizontalLayout(
            UiUtils.priorityBadge(ticket.getPriority()),
            UiUtils.statusBadge(ticket.getStatus()));
        badges.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        VerticalLayout header = new VerticalLayout(title, badges);
        header.setPadding(false);
        header.setSpacing(false);
        header.getStyle().set("gap", "var(--vaadin-gap-s)");

        // Two columns: details (left) + thread (right)
        HorizontalLayout body = new HorizontalLayout();
        body.setWidthFull();
        body.getStyle().set("gap", "var(--vaadin-gap-l)").set("align-items", "flex-start");

        VerticalLayout details = buildDetails(ticket, isAgent);
        details.setWidth("320px");
        details.getStyle()
            .set("padding", "var(--vaadin-padding-m)")
            .set("background", "var(--vaadin-background-container)")
            .set("border-radius", "var(--vaadin-radius-l)");

        VerticalLayout thread = buildThread(ticket, isAgent, isCustomerOwner);
        thread.getStyle().set("flex", "1");

        body.add(details, thread);
        body.expand(thread);

        add(header, body);
    }

    private VerticalLayout buildDetails(Ticket ticket, boolean isAgent) {
        VerticalLayout v = new VerticalLayout();
        v.setPadding(false);
        v.setSpacing(false);
        v.getStyle().set("gap", "var(--vaadin-gap-s)");

        v.add(new H3("Customer"));
        v.add(line("Name", ticket.getCustomer().getName()));
        v.add(line("Email", ticket.getCustomer().getEmail()));
        if (ticket.getCustomer().getPhone() != null)
            v.add(line("Phone", ticket.getCustomer().getPhone()));

        v.add(new H3("Ticket"));
        v.add(line("Created", UiUtils.formatTimestamp(ticket.getCreatedAt())));
        v.add(line("Last update", UiUtils.formatTimestamp(ticket.getUpdatedAt())));
        if (ticket.getResolvedAt() != null) v.add(line("Resolved", UiUtils.formatTimestamp(ticket.getResolvedAt())));
        v.add(line("Assigned", ticket.getAssignedAgent() == null ? "Unassigned" : ticket.getAssignedAgent().getName()));

        v.add(new H3("Description"));
        Paragraph desc = new Paragraph(ticket.getDescription());
        desc.getStyle().set("white-space", "pre-wrap").set("margin", "0");
        v.add(desc);

        if (isAgent) {
            v.add(new H3("Actions"));
            v.add(buildActions(ticket));
        }
        return v;
    }

    private VerticalLayout buildActions(Ticket ticket) {
        VerticalLayout v = new VerticalLayout();
        v.setPadding(false);
        v.setSpacing(false);
        v.getStyle().set("gap", "var(--vaadin-gap-s)");

        // Assign
        ComboBox<Agent> assignCombo = new ComboBox<>("Assign to");
        List<Agent> active = agentService.activeAgents();
        assignCombo.setItems(active);
        assignCombo.setItemLabelGenerator(Agent::getName);
        assignCombo.setValue(ticket.getAssignedAgent());
        assignCombo.setWidthFull();
        assignCombo.addValueChangeListener(e -> {
            Agent a = e.getValue();
            if (a == null) return;
            try {
                ticketService.assign(ticket.getId(), a.getId());
                Notification.show("Assigned to " + a.getName());
                render();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        // Status
        ComboBox<TicketStatus> statusCombo = new ComboBox<>("Status");
        statusCombo.setItems(EnumSet.allOf(TicketStatus.class));
        statusCombo.setItemLabelGenerator(TicketStatus::getLabel);
        statusCombo.setValue(ticket.getStatus());
        statusCombo.setWidthFull();
        statusCombo.addValueChangeListener(e -> {
            TicketStatus s = e.getValue();
            if (s == null || s == ticket.getStatus()) return;
            try {
                ticketService.updateStatus(ticket.getId(), s);
                Notification.show("Status updated to " + s.getLabel());
                render();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        // Priority
        ComboBox<Priority> priorityCombo = new ComboBox<>("Priority");
        priorityCombo.setItems(EnumSet.allOf(Priority.class));
        priorityCombo.setItemLabelGenerator(Priority::getLabel);
        priorityCombo.setValue(ticket.getPriority());
        priorityCombo.setWidthFull();
        priorityCombo.addValueChangeListener(e -> {
            Priority p = e.getValue();
            if (p == null || p == ticket.getPriority()) return;
            ticketService.updatePriority(ticket.getId(), p);
            Notification.show("Priority updated");
            render();
        });

        v.add(assignCombo, statusCombo, priorityCombo);
        return v;
    }

    private VerticalLayout buildThread(Ticket ticket, boolean isAgent, boolean isCustomerOwner) {
        VerticalLayout v = new VerticalLayout();
        v.setPadding(false);
        v.setSpacing(false);
        v.getStyle().set("gap", "var(--vaadin-gap-m)");

        v.add(new H3("Conversation"));

        if (ticket.getMessages().isEmpty()) {
            Paragraph empty = new Paragraph("No messages yet.");
            empty.getStyle().set("color", "var(--vaadin-text-color-secondary)");
            v.add(empty);
        } else {
            for (Message m : ticket.getMessages()) {
                v.add(messageBubble(m));
            }
        }

        if (isAgent || isCustomerOwner) {
            TextArea reply = new TextArea();
            reply.setPlaceholder("Type your response...");
            reply.setWidthFull();
            reply.setMinHeight("100px");

            Button send = new Button(isAgent ? "Send response" : "Send message");
            send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            send.addClickListener(e -> {
                if (reply.isEmpty()) {
                    showError("Message cannot be empty");
                    return;
                }
                try {
                    if (isAgent) {
                        Agent agent = agentService.findByUsername(auth.getPrincipalName().orElse("")).orElseThrow();
                        ticketService.addAgentMessage(ticket.getId(), agent, reply.getValue());
                    } else {
                        Customer customer = customers.findByEmailIgnoreCase(CustomerSession.getEmail()).orElseThrow();
                        ticketService.addCustomerMessage(ticket.getId(), customer, reply.getValue());
                    }
                    reply.clear();
                    render();
                } catch (Exception ex) {
                    showError(ex.getMessage());
                }
            });

            v.add(reply, send);
        }
        return v;
    }

    private Div messageBubble(Message m) {
        Div d = new Div();
        d.getStyle()
            .set("padding", "var(--vaadin-padding-m)")
            .set("border-radius", "var(--vaadin-radius-l)")
            .set("max-width", "85%");

        boolean fromAgent = m.getSenderType() == SenderType.AGENT;
        if (fromAgent) {
            d.getStyle()
                .set("background", "var(--aura-accent-surface)")
                .set("align-self", "flex-start")
                .set("margin-right", "auto");
        } else {
            d.getStyle()
                .set("background", "var(--vaadin-background-container)")
                .set("margin-left", "auto");
        }

        Span sender = new Span(m.getSenderName() == null ? m.getSenderType().name() : m.getSenderName());
        sender.getStyle().set("font-weight", "var(--aura-font-weight-semibold)");
        Span role = new Span(fromAgent ? " · Agent" : " · Customer");
        role.getStyle().set("color", "var(--vaadin-text-color-secondary)").set("font-size", "var(--aura-font-size-xs)");
        Span time = new Span(" · " + UiUtils.formatTimestamp(m.getCreatedAt()));
        time.getStyle().set("color", "var(--vaadin-text-color-secondary)").set("font-size", "var(--aura-font-size-xs)");

        Paragraph content = new Paragraph(m.getContent());
        content.getStyle().set("margin", "var(--vaadin-gap-s) 0 0 0").set("white-space", "pre-wrap");

        d.add(sender, role, time, content);
        return d;
    }

    private HorizontalLayout line(String label, String value) {
        Span l = new Span(label);
        l.getStyle().set("color", "var(--vaadin-text-color-secondary)").set("min-width", "90px");
        Span v = new Span(value == null ? "—" : value);
        HorizontalLayout row = new HorizontalLayout(l, v);
        row.setSpacing(false);
        row.getStyle().set("gap", "var(--vaadin-gap-s)");
        return row;
    }

    private void showError(String msg) {
        Notification n = Notification.show(msg, 3000, Notification.Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
