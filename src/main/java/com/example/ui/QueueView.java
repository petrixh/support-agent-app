package com.example.ui;

import com.example.domain.Agent;
import com.example.domain.Ticket;
import com.example.domain.TicketStatus;
import com.example.service.AgentService;
import com.example.service.TicketService;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Route(value = "queue", layout = MainLayout.class)
@PageTitle("Ticket Queue")
@RolesAllowed("AGENT")
public class QueueView extends VerticalLayout {

    private final TicketService ticketService;
    private final AgentService agentService;
    private final AuthenticationContext auth;

    private final Grid<Ticket> grid = new Grid<>(Ticket.class, false);
    private final TextField search = new TextField();
    private final MultiSelectComboBox<TicketStatus> statusFilter = new MultiSelectComboBox<>("Status");

    private List<Ticket> allTickets = List.of();

    public QueueView(TicketService ticketService, AgentService agentService, AuthenticationContext auth) {
        this.ticketService = ticketService;
        this.agentService = agentService;
        this.auth = auth;

        setSizeFull();
        setPadding(true);

        add(new H2("Ticket queue"));
        Paragraph hint = new Paragraph("Open and in-progress tickets assigned to you or unassigned, sorted by priority.");
        hint.getStyle().set("color", "var(--vaadin-text-color-secondary)");
        add(hint);

        search.setPlaceholder("Search by ID or customer name");
        search.setClearButtonVisible(true);
        search.setValueChangeMode(ValueChangeMode.LAZY);
        search.addValueChangeListener(e -> applyFilters());

        statusFilter.setItems(EnumSet.allOf(TicketStatus.class));
        statusFilter.setItemLabelGenerator(TicketStatus::getLabel);
        statusFilter.setValue(Set.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS));
        statusFilter.addValueChangeListener(e -> applyFilters());

        HorizontalLayout controls = new HorizontalLayout(search, statusFilter);
        controls.setWidthFull();
        controls.expand(search);
        add(controls);

        configureGrid();
        add(grid);
        setFlexGrow(1, grid);

        reload();
    }

    private void configureGrid() {
        grid.addColumn(t -> "#" + t.getId()).setHeader("ID").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(t -> t.getCustomer().getName() != null ? t.getCustomer().getName() : t.getCustomer().getEmail())
            .setHeader("Customer").setAutoWidth(true);
        grid.addColumn(Ticket::getTitle).setHeader("Title").setFlexGrow(1);
        grid.addComponentColumn(t -> UiUtils.priorityBadge(t.getPriority())).setHeader("Priority").setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(t -> UiUtils.statusBadge(t.getStatus())).setHeader("Status").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(t -> UiUtils.timeAgo(t.getCreatedAt())).setHeader("Created").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(t -> t.getAssignedAgent() == null ? "Unassigned" : t.getAssignedAgent().getName())
            .setHeader("Assigned").setAutoWidth(true);

        grid.addItemClickListener(e -> getUI().ifPresent(ui -> ui.navigate(TicketDetailView.class, e.getItem().getId())));
        grid.setSizeFull();
    }

    private void reload() {
        Agent agent = currentAgent();
        if (agent == null) return;
        allTickets = ticketService.queueForAgent(agent);
        applyFilters();
    }

    private void applyFilters() {
        String q = search.getValue() == null ? "" : search.getValue().trim().toLowerCase();
        Set<TicketStatus> statuses = statusFilter.getValue();
        List<Ticket> filtered = allTickets.stream()
            .filter(t -> statuses.isEmpty() || statuses.contains(t.getStatus()))
            .filter(t -> {
                if (q.isEmpty()) return true;
                String idStr = String.valueOf(t.getId());
                String customer = t.getCustomer().getName() == null ? "" : t.getCustomer().getName().toLowerCase();
                return idStr.contains(q) || customer.contains(q);
            })
            .toList();
        grid.setItems(filtered);
    }

    private Agent currentAgent() {
        return auth.getPrincipalName()
            .flatMap(agentService::findByUsername)
            .orElse(null);
    }
}
