package com.example.ui;

import com.example.domain.Ticket;
import com.example.service.TicketService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "ticket/create", layout = MainLayout.class)
@PageTitle("Create Ticket")
@AnonymousAllowed
public class CreateTicketView extends VerticalLayout {

    private final TicketService ticketService;

    private final TextField title = new TextField("Title");
    private final TextArea description = new TextArea("Description");
    private final EmailField email = new EmailField("Email");
    private final TextField name = new TextField("Name (optional)");
    private final TextField phone = new TextField("Phone (optional)");
    private final Button submit = new Button("Submit Ticket");

    public CreateTicketView(TicketService ticketService) {
        this.ticketService = ticketService;
        setPadding(true);
        setMaxWidth("640px");
        getStyle().set("margin", "0 auto");

        H2 heading = new H2("Create a support ticket");
        Paragraph intro = new Paragraph("Tell us what's going on and we'll get back to you.");
        intro.getStyle().set("color", "var(--vaadin-text-color-secondary)");

        title.setRequired(true);
        title.setWidthFull();
        title.setValueChangeMode(com.vaadin.flow.data.value.ValueChangeMode.EAGER);

        description.setRequired(true);
        description.setWidthFull();
        description.setMinHeight("160px");
        description.setValueChangeMode(com.vaadin.flow.data.value.ValueChangeMode.EAGER);

        email.setRequired(true);
        email.setWidthFull();
        email.setValueChangeMode(com.vaadin.flow.data.value.ValueChangeMode.EAGER);
        email.setErrorMessage("Enter a valid email address");

        name.setWidthFull();
        phone.setWidthFull();

        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submit.setEnabled(false);
        submit.addClickListener(e -> doSubmit());

        // BR-01: enable only when mandatory fields are filled
        Runnable updateEnabled = () -> submit.setEnabled(
            !title.isEmpty() && !description.isEmpty() && !email.isEmpty() && !email.isInvalid());
        title.addValueChangeListener(e -> updateEnabled.run());
        description.addValueChangeListener(e -> updateEnabled.run());
        email.addValueChangeListener(e -> updateEnabled.run());

        add(heading, intro, title, description, email, name, phone, submit);
    }

    private void doSubmit() {
        try {
            Ticket t = ticketService.createTicket(
                title.getValue(), description.getValue(),
                email.getValue(), name.getValue(), phone.getValue());

            // Remember the customer's email for "My tickets"
            CustomerSession.setEmail(email.getValue().trim());

            removeAll();
            H2 ok = new H2("Ticket #" + t.getId() + " created");
            Paragraph p = new Paragraph("Thanks — we received your ticket. You can track it from your tickets page.");
            RouterLink view = new RouterLink("View ticket", TicketDetailView.class, t.getId());
            RouterLink myTickets = new RouterLink("My tickets", MyTicketsView.class);
            HorizontalLayout actions = new HorizontalLayout(view, myTickets);
            actions.getStyle().set("gap", "var(--vaadin-gap-m)");
            add(ok, p, actions);
        } catch (IllegalArgumentException ex) {
            Notification n = Notification.show(ex.getMessage(), 3000, Notification.Position.MIDDLE);
            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
