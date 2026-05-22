# UC-003: Assign Ticket to Agent

> Support agent assigns a ticket to themselves or another agent.

---

**As a** support agent, **I want to** assign a ticket to an agent **so that** the ticket is claimed and work can begin.

**Status:** Pending
**Date:** 2024-01-01

---

## Main Flow

- I view a ticket in the queue or detail view
- I see an "Assign" button or dropdown showing available agents
- I select myself or another available agent
- I click "Assign"
- The system updates the ticket and shows the assignment confirmation
- The ticket status changes to "In Progress" if it was "Open"
- Other agents see the ticket is now assigned

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Only unassigned or previously assigned tickets can be reassigned |
| BR-02 | A ticket can only be assigned to active agents |
| BR-03 | Assigning a ticket changes its status to "In Progress" if it was "Open" |
| BR-04 | An agent can see which agent currently has the ticket |
| BR-05 | Assignment history is tracked (when assigned and by whom) |

---

## Acceptance Criteria

- [ ] Agent can see a list of available agents to assign to
- [ ] Agent can assign ticket to themselves
- [ ] Agent can assign ticket to another agent
- [ ] System prevents assigning to inactive agents
- [ ] Ticket status changes to "In Progress" after assignment
- [ ] Ticket assignment is reflected immediately in the queue
- [ ] All agents see the updated assignment status

---

## Tests

- [ ] `AssignTicketTest` — Verify agent list shows only active agents
- [ ] Verify agent can assign to themselves
- [ ] Verify agent can assign to another agent
- [ ] Verify ticket status updates to "In Progress"
- [ ] Verify assignment is visible to all agents immediately

---

## UI / Routes

Assignment functionality embedded in ticket detail view or queue row.

- Dropdown or button showing "Assign to..." with agent list
- Currently assigned agent is highlighted
- Confirmation message after assignment
- Clear indication of assignment status in ticket list

| Route | Access | Notes |
|-------|--------|-------|
| `/ticket/{id}` | authenticated (agents) | Vaadin @Route |
