package com.example.ui;

import com.example.domain.Ticket;
import com.example.domain.TicketStatus;
import com.example.service.TicketService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Route(value = "my-tickets", layout = MainLayout.class)
@PageTitle("My Tickets")
@AnonymousAllowed
public class MyTicketsView extends VerticalLayout {

    private final TicketService ticketService;
    private final Grid<Ticket> grid = new Grid<>(Ticket.class, false);
    private final TextField search = new TextField();
    private final MultiSelectComboBox<TicketStatus> statusFilter = new MultiSelectComboBox<>("Status");
    private List<Ticket> all = List.of();

    public MyTicketsView(TicketService ticketService) {
        this.ticketService = ticketService;
        setSizeFull();
        setPadding(true);

        if (CustomerSession.getEmail() == null) {
            renderEmailPrompt();
        } else {
            renderList();
        }
    }

    private void renderEmailPrompt() {
        H2 h = new H2("View my tickets");
        Paragraph p = new Paragraph("Enter the email address you used when creating your tickets.");
        p.getStyle().set("color", "var(--vaadin-text-color-secondary)");
        EmailField field = new EmailField("Email");
        field.setRequired(true);
        field.setWidth("320px");
        Button go = new Button("Continue", e -> {
            if (field.isEmpty() || field.isInvalid()) {
                field.setInvalid(true);
                return;
            }
            CustomerSession.setEmail(field.getValue().trim());
            getUI().ifPresent(ui -> ui.getPage().reload());
        });
        go.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(h, p, field, go);
    }

    private void renderList() {
        String email = CustomerSession.getEmail();
        H2 h = new H2("My tickets");
        Paragraph p = new Paragraph("Showing tickets for " + email + ".");
        p.getStyle().set("color", "var(--vaadin-text-color-secondary)");
        add(h, p);

        search.setPlaceholder("Search by ID or title");
        search.setClearButtonVisible(true);
        search.setValueChangeMode(ValueChangeMode.LAZY);
        search.addValueChangeListener(e -> applyFilters());

        statusFilter.setItems(EnumSet.allOf(TicketStatus.class));
        statusFilter.setItemLabelGenerator(TicketStatus::getLabel);
        statusFilter.addValueChangeListener(e -> applyFilters());

        HorizontalLayout controls = new HorizontalLayout(search, statusFilter);
        controls.setWidthFull();
        controls.expand(search);
        add(controls);

        grid.addColumn(t -> "#" + t.getId()).setHeader("ID").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Ticket::getTitle).setHeader("Title").setFlexGrow(1);
        grid.addComponentColumn(t -> UiUtils.priorityBadge(t.getPriority())).setHeader("Priority").setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(t -> UiUtils.statusBadge(t.getStatus())).setHeader("Status").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(t -> UiUtils.timeAgo(t.getUpdatedAt())).setHeader("Last update").setAutoWidth(true).setFlexGrow(0);
        grid.addItemClickListener(e -> getUI().ifPresent(ui -> ui.navigate(TicketDetailView.class, e.getItem().getId())));
        grid.setSizeFull();
        add(grid);
        setFlexGrow(1, grid);

        all = ticketService.ticketsForCustomer(email);
        applyFilters();
    }

    private void applyFilters() {
        String q = search.getValue() == null ? "" : search.getValue().trim().toLowerCase();
        Set<TicketStatus> statuses = statusFilter.getValue();
        List<Ticket> filtered = all.stream()
            .filter(t -> statuses.isEmpty() || statuses.contains(t.getStatus()))
            .filter(t -> {
                if (q.isEmpty()) return true;
                return String.valueOf(t.getId()).contains(q)
                    || t.getTitle().toLowerCase().contains(q);
            })
            .toList();
        grid.setItems(filtered);
    }
}
