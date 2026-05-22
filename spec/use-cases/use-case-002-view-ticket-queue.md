# UC-002: View Ticket Queue

> Support agent views all open and in-progress tickets organized by priority.

---

**As a** support agent, **I want to** view all tickets in a prioritized queue **so that** I can see what needs attention and take action.

**Status:** Pending
**Date:** 2024-01-01

---

## Main Flow

- I navigate to the Ticket Queue page
- I see a list of all open and in-progress tickets
- Tickets are displayed in rows, sorted by priority (Urgent first, then High, Medium, Low)
- For each ticket, I can see: ticket ID, customer name, title, priority, status, and time since created
- I can click on a ticket to view its details
- I can filter tickets by status (Open, In Progress, Resolved, Closed)
- I can search for tickets by ticket ID or customer name

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Only open and in-progress tickets appear in the default queue view |
| BR-02 | Tickets are sorted by priority (Urgent > High > Medium > Low) |
| BR-03 | Within same priority, newer tickets appear first |
| BR-04 | Agents can only see tickets assigned to them or unassigned tickets |
| BR-05 | Queue is automatically refreshed when new tickets arrive |

---

## Acceptance Criteria

- [ ] Agent can view a list of open and in-progress tickets
- [ ] Tickets are sorted by priority with Urgent tickets at the top
- [ ] Agent can see ticket ID, customer name, title, priority, status, and time created
- [ ] Agent can click on a ticket to open its detail view
- [ ] Agent can filter tickets by status
- [ ] Agent can search tickets by ID or customer name
- [ ] Queue updates to show new tickets without page reload

---

## Tests

- [ ] `TicketQueueTest` — Verify tickets display correctly with all required fields
- [ ] Verify tickets are sorted by priority (Urgent first)
- [ ] Verify search filters tickets by ID and customer name
- [ ] Verify status filter shows only selected statuses
- [ ] Verify clicking ticket navigates to detail page

---

## UI / Routes

A table/grid-based interface showing all tickets with filtering and search capabilities.

- Responsive table layout with columns: ID, Customer, Title, Priority, Status, Time Created
- Color-coded priority badges (red=Urgent, orange=High, yellow=Medium, gray=Low)
- Search bar at top for ID and customer name
- Filter tabs or dropdown for status selection
- Click row to open ticket details

| Route | Access | Notes |
|-------|--------|-------|
| `/queue` | authenticated (agents) | Vaadin @Route |
