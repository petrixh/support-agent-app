package com.example.repository;

import com.example.domain.Customer;
import com.example.domain.Ticket;
import com.example.domain.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCustomerOrderByUpdatedAtDesc(Customer customer);
    List<Ticket> findByStatusIn(List<TicketStatus> statuses);
}
