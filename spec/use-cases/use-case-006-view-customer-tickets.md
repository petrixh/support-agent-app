# UC-006: View Customer Tickets

> Customer views all their support tickets and current status.

---

**As a** customer, **I want to** view all my support tickets **so that** I can track the status of my issues.

**Status:** Pending
**Date:** 2024-01-01

---

## Main Flow

- I navigate to "My Tickets" or dashboard page
- I see a list of all tickets I have created
- Each ticket shows: ticket ID, title, priority, current status, and last update time
- Status indicators use colors to make priority and status clear at a glance
- I can click on any ticket to view full details and communication history
- I can filter tickets by status (Open, In Progress, Resolved, Closed)
- I can search for specific tickets by ID or title

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Customers can only see their own tickets |
| BR-02 | All ticket statuses are visible to the customer |
| BR-03 | Customers see read-only communication history (no ability to edit) |
| BR-04 | Last update timestamp shows the most recent message or status change |
| BR-05 | Resolved and closed tickets remain visible for historical reference |

---

## Acceptance Criteria

- [ ] Customer can view a list of all their tickets
- [ ] Each ticket shows ID, title, priority, status, and last update time
- [ ] Customer can click on a ticket to see full details
- [ ] Customer can filter tickets by status
- [ ] Customer can search by ticket ID or title
- [ ] Status indicators are color-coded for easy recognition
- [ ] Resolved and closed tickets remain visible in the list

---

## Tests

- [ ] `CustomerTicketListTest` — Verify all customer tickets are displayed
- [ ] Verify status and priority indicators are visible and correct
- [ ] Verify clicking ticket opens detail view
- [ ] Verify status filter works correctly
- [ ] Verify search filters tickets by ID and title

---

## UI / Routes

Dashboard-style interface showing customer's tickets with filtering and search.

- List or table of tickets with columns: ID, Title, Priority, Status, Last Updated
- Status badges with color coding
- Search bar for ID and title
- Filter tabs or dropdown for status
- Click row to open ticket detail page
- Visual indicator if ticket has unread messages

| Route | Access | Notes |
|-------|--------|-------|
| `/my-tickets` | authenticated (customers) | Vaadin @Route |
