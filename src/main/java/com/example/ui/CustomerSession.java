package com.example.ui;

import com.vaadin.flow.server.VaadinSession;

public final class CustomerSession {
    private static final String KEY = "customer.email";

    private CustomerSession() {}

    public static void setEmail(String email) {
        VaadinSession.getCurrent().setAttribute(KEY, email);
    }

    public static String getEmail() {
        VaadinSession s = VaadinSession.getCurrent();
        return s == null ? null : (String) s.getAttribute(KEY);
    }

    public static void clear() {
        VaadinSession s = VaadinSession.getCurrent();
        if (s != null) s.setAttribute(KEY, null);
    }
}
