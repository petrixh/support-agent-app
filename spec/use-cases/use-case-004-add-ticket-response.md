# UC-004: Add Response to Ticket

> Support agent adds a message response to a ticket to communicate with the customer.

---

**As a** support agent, **I want to** add responses and updates to a ticket **so that** the customer knows their issue is being addressed.

**Status:** Pending
**Date:** 2024-01-01

---

## Main Flow

- I open a ticket detail view
- I see the communication history showing all messages from customer and agents
- At the bottom, I see a text area for typing a response
- I enter my response message
- I can optionally update the ticket priority or status before sending
- I click "Send Response"
- The system adds my message to the ticket's communication history
- The message appears with my name, agent badge, and timestamp
- The customer sees the new message when they view the ticket

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Response messages cannot be empty |
| BR-02 | Messages are associated with the agent who creates them |
| BR-03 | All messages are timestamped and displayed in chronological order |
| BR-04 | Agents can view full communication history (customer and agent messages) |
| BR-05 | Customers can only see messages marked as visible to them |

---

## Acceptance Criteria

- [ ] Agent can view complete message history for a ticket
- [ ] Agent can type and submit a response message
- [ ] System validates that message is not empty
- [ ] Message appears in conversation with agent name and timestamp
- [ ] Customer can see the agent's response in their ticket view
- [ ] Messages are displayed in chronological order (oldest first)
- [ ] Agent can edit priority and status while responding

---

## Tests

- [ ] `AddResponseTest` — Verify message form is present and functional
- [ ] Verify empty message shows validation error
- [ ] Verify message is added to history with correct sender and timestamp
- [ ] Verify message appears for customer immediately
- [ ] Verify message order is chronological

---

## UI / Routes

Ticket detail view with full communication thread and response form.

- Message history displayed as conversation thread (similar to email or chat)
- Each message shows: sender name, role badge (Agent/Customer), timestamp, and content
- Response input area at bottom with text editor
- Optional priority and status selectors above submit button
- Send button submits response

| Route | Access | Notes |
|-------|--------|-------|
| `/ticket/{id}` | authenticated (agents) | Vaadin @Route |
