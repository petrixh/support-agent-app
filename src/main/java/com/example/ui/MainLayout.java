package com.example.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

@AnonymousAllowed
public class MainLayout extends AppLayout {

    private final AuthenticationContext auth;

    public MainLayout(AuthenticationContext auth) {
        this.auth = auth;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Help Desk");
        title.getStyle()
            .set("font-size", "var(--aura-font-size-l)")
            .set("margin", "0");

        HorizontalLayout header = new HorizontalLayout(toggle, title);
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(title);
        header.getStyle().set("padding", "0 var(--vaadin-padding-m)");

        if (auth.isAuthenticated()) {
            String name = auth.getPrincipalName().orElse("");
            Span userBadge = new Span("Agent: " + name);
            userBadge.getStyle().set("color", "var(--vaadin-text-color-secondary)");
            Button logout = new Button("Logout", e -> auth.logout());
            logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            header.add(userBadge, logout);
        } else if (CustomerSession.getEmail() != null) {
            Span userBadge = new Span("Customer: " + CustomerSession.getEmail());
            userBadge.getStyle().set("color", "var(--vaadin-text-color-secondary)");
            Button signOut = new Button("Sign out", e -> {
                CustomerSession.clear();
                getUI().ifPresent(ui -> ui.getPage().setLocation("/"));
            });
            signOut.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            header.add(userBadge, signOut);
        }

        addToNavbar(header);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Help Desk");
        appName.getStyle().set("font-size", "var(--aura-font-size-m)").set("margin", "var(--vaadin-padding-m)");

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Create ticket", "/ticket/create"));
        nav.addItem(new SideNavItem("My tickets", "/my-tickets"));

        if (auth.isAuthenticated()) {
            nav.addItem(new SideNavItem("Agent queue", "/queue"));
        } else {
            nav.addItem(new SideNavItem("Agent login", "/login"));
        }

        VerticalLayout drawer = new VerticalLayout(appName, nav);
        drawer.setPadding(false);
        drawer.setSpacing(false);
        addToDrawer(drawer);
    }
}
