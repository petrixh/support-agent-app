package com.example.init;

import com.example.domain.*;
import com.example.repository.*;
import com.example.service.TicketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AgentRepository agents;
    private final CustomerRepository customers;
    private final TicketService ticketService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AgentRepository agents, CustomerRepository customers,
                           TicketService ticketService, PasswordEncoder passwordEncoder) {
        this.agents = agents;
        this.customers = customers;
        this.ticketService = ticketService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (agents.count() > 0) return;

        Agent alice = agents.save(new Agent("alice", "alice@example.com", "Alice Johnson",
            passwordEncoder.encode("password")));
        Agent bob = agents.save(new Agent("bob", "bob@example.com", "Bob Smith",
            passwordEncoder.encode("password")));
        Agent carol = agents.save(new Agent("carol", "carol@example.com", "Carol Davis",
            passwordEncoder.encode("password")));
        carol.setActive(false);
        agents.save(carol);

        Ticket t1 = ticketService.createTicket(
            "Cannot log in to my account",
            "I keep getting an 'invalid credentials' error even though I'm using the right password. I've tried resetting it twice.",
            "jane@example.com", "Jane Doe", "+1-555-0101");
        ticketService.updatePriority(t1.getId(), Priority.HIGH);

        Ticket t2 = ticketService.createTicket(
            "Billing question about last invoice",
            "I see a charge on my invoice from last month that I don't recognize. Could you explain what it's for?",
            "john@example.com", "John Roe", "+1-555-0102");

        Ticket t3 = ticketService.createTicket(
            "Site is down — urgent!",
            "Our entire team is unable to access the platform. This is blocking work for 20 people.",
            "ops@bigcorp.com", "Sam Operator", null);
        ticketService.updatePriority(t3.getId(), Priority.URGENT);
        ticketService.assign(t3.getId(), alice.getId());
        ticketService.addAgentMessage(t3.getId(), alice, "We're investigating now — will update within 15 minutes.");

        Ticket t4 = ticketService.createTicket(
            "Feature request: dark mode",
            "Would love a dark theme option in settings.",
            "jane@example.com", "Jane Doe", null);
        ticketService.updatePriority(t4.getId(), Priority.LOW);
    }
}
