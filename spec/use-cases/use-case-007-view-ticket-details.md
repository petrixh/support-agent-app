# UC-007: View Ticket Details

> User (agent or customer) views complete ticket information and communication history.

---

**As a** support agent or customer, **I want to** view complete ticket details **so that** I understand the full context of an issue and can see all communication.

**Status:** Pending
**Date:** 2024-01-01

---

## Main Flow

- I click on a ticket from the list/queue
- I see the ticket detail page with:
  - Ticket ID and title
  - Customer information (name, email, phone)
  - Current priority, status, and assignment
  - Creation date and last updated date
  - Full message history in chronological order
  - (For agents) Options to assign, update status, or respond
  - (For customers) Read-only view of communication

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Agents can see all ticket information and full message history |
| BR-02 | Customers see all messages but cannot see internal agent notes (if applicable) |
| BR-03 | Ticket details include customer contact information and communication preference |
| BR-04 | Message history displays all messages in chronological order |
| BR-05 | Assigned agent name and assignment time are visible |

---

## Acceptance Criteria

- [ ] Ticket detail page displays all required ticket information
- [ ] Customer information is clearly visible
- [ ] Priority and status are displayed with visual indicators
- [ ] Assignment information shows assigned agent and when
- [ ] Complete message history is shown in chronological order
- [ ] Messages show sender, role, timestamp, and content
- [ ] Agents see action buttons (assign, update status, respond)
- [ ] Customers see read-only view without modification options
- [ ] Page is responsive and readable on all screen sizes

---

## Tests

- [ ] `TicketDetailTest` — Verify all ticket information displays correctly
- [ ] Verify message history is in chronological order
- [ ] Verify sender information is correct for each message
- [ ] Verify action buttons visible for agents only
- [ ] Verify customer sees read-only view without modification options

---

## UI / Routes

Detailed ticket view with complete information and communication thread.

- Header section with ticket ID, title, and key status information
- Two-column layout: left shows ticket details, right shows communication
- Ticket details panel: customer info, priority, status, assigned agent, dates
- Message thread: chronological conversation with visual distinction between agent and customer
- Action panel (agents only): buttons for assign, change status, add response
- Responsive design collapses to single column on mobile

| Route | Access | Notes |
|-------|--------|-------|
| `/ticket/{id}` | authenticated | Vaadin @Route |
