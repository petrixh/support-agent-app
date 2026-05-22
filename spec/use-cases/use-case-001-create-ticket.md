# UC-001: Create Support Ticket

> Customer creates a new support ticket to report an issue.

---

**As a** customer, **I want to** create a support ticket **so that** I can report an issue and get help from the support team.

**Status:** Pending
**Date:** 2024-01-01

---

## Main Flow

- I navigate to the "Create Ticket" page
- I see a form with fields for title, description, and optional contact information
- I enter a title and description of my issue
- I can optionally provide my email, name, and phone number
- I click "Submit Ticket"
- The system creates the ticket and displays a confirmation with the ticket number
- I am shown a link to view my ticket status

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Title and description fields are mandatory |
| BR-02 | New tickets are created with "Open" status by default |
| BR-03 | New tickets are assigned "Medium" priority by default |
| BR-04 | System generates a unique ticket ID for each new ticket |
| BR-05 | Customer email is required to track and communicate about the ticket |

---

## Acceptance Criteria

- [ ] Customer can submit a ticket with title and description
- [ ] Customer can provide optional name and phone number
- [ ] System validates that title and description are not empty
- [ ] System validates that email address is in valid format
- [ ] Ticket is created with "Open" status and "Medium" priority
- [ ] Customer receives confirmation with ticket ID after submission

---

## Tests

- [ ] `CreateTicketTest` — Verify that all form fields are present and functional
- [ ] Validates that empty title or description shows validation error
- [ ] Validates that invalid email format shows error
- [ ] Validates that ticket is successfully created with correct default values

---

## UI / Routes

A form-based interface for ticket creation with clear visual feedback on submission.

- Single-column form layout with labeled input fields
- Title and description are multi-line text inputs
- Submit button is disabled until mandatory fields are filled
- Success message shows generated ticket ID

| Route | Access | Notes |
|-------|--------|-------|
| `/ticket/create` | public | Vaadin @Route |
