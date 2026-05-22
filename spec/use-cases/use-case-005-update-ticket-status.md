# UC-005: Update Ticket Status

> Support agent updates the status of a ticket to reflect progress or resolution.

---

**As a** support agent, **I want to** change a ticket's status **so that** the team and customer know the current state of the issue.

**Status:** Pending
**Date:** 2024-01-01

---

## Main Flow

- I open a ticket detail view
- I see the current ticket status displayed prominently
- I click on the status or click an "Update Status" button
- A dropdown or selection menu shows available status options: Open, In Progress, Resolved, Closed
- I select the new status
- The system updates the ticket immediately
- The status change is recorded with timestamp
- The customer sees the updated status in their ticket view

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Status transitions follow a logical flow: Open → In Progress → Resolved → Closed |
| BR-02 | Only agents assigned to a ticket can change its status |
| BR-03 | Closing a ticket requires that it be in "Resolved" status first |
| BR-04 | Status changes are recorded with timestamp |
| BR-05 | Customers can see the status history of their ticket |

---

## Acceptance Criteria

- [ ] Agent can view current ticket status
- [ ] Agent can change status through dropdown or button
- [ ] All valid status options are available based on current status
- [ ] Status updates immediately after selection
- [ ] Status change is recorded with timestamp
- [ ] Customer sees updated status in real-time
- [ ] Closing a resolved ticket works correctly

---

## Tests

- [ ] `UpdateStatusTest` — Verify status dropdown shows appropriate options
- [ ] Verify status updates immediately on selection
- [ ] Verify status change is persisted in database
- [ ] Verify customer sees updated status
- [ ] Verify timestamp is recorded for each status change

---

## UI / Routes

Status update functionality integrated into ticket detail view.

- Status displayed as badge or highlighted field in ticket header
- Click to open dropdown with valid status options
- Color-coded statuses (Open=blue, In Progress=orange, Resolved=green, Closed=gray)
- Confirmation message after update
- Status history visible in ticket timeline or details section

| Route | Access | Notes |
|-------|--------|-------|
| `/ticket/{id}` | authenticated (agents) | Vaadin @Route |
