package com.example.service;

import com.example.domain.Agent;
import com.example.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    private final AgentRepository agents;

    public AgentService(AgentRepository agents) {
        this.agents = agents;
    }

    public List<Agent> activeAgents() {
        return agents.findByActiveTrueOrderByNameAsc();
    }

    public Optional<Agent> findByUsername(String username) {
        return agents.findByUsername(username);
    }
}
