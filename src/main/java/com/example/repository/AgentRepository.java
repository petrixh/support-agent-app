package com.example.repository;

import com.example.domain.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByUsername(String username);
    List<Agent> findByActiveTrueOrderByNameAsc();
}
