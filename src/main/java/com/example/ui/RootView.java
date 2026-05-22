package com.example.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

@Route("")
@AnonymousAllowed
public class RootView extends Div implements BeforeEnterObserver {

    private final AuthenticationContext auth;

    public RootView(AuthenticationContext auth) {
        this.auth = auth;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (auth.isAuthenticated()) {
            event.forwardTo(QueueView.class);
        } else if (CustomerSession.getEmail() != null) {
            event.forwardTo(MyTicketsView.class);
        } else {
            event.forwardTo(CreateTicketView.class);
        }
    }
}
