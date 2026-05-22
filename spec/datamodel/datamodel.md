# Data Model

> Entity definitions and relationships. Evolves as features are added.

| Entity | Key Fields | Relationships |
|--------|-----------|-----------------|
| Customer | id, email, name, phone, created_at | Has many Tickets |
| Ticket | id, title, description, priority, status, created_at, updated_at, resolved_at | Belongs to Customer; Has many Agents (through Assignment); Has many Messages |
| Agent | id, email, name, username, is_active, created_at | Has many Assignments; Has many Messages |
| Assignment | id, ticket_id, agent_id, assigned_at, unassigned_at | Belongs to Ticket; Belongs to Agent |
| Message | id, ticket_id, sender_id, sender_type (agent/customer), content, created_at | Belongs to Ticket; Sent by Agent or Customer |

## Key Relationships

- **Customer → Ticket** — One customer can have many support tickets
- **Ticket → Agent** — A ticket can be assigned to one agent at a time through the Assignment relationship
- **Ticket → Message** — A ticket has a communication history with multiple messages from both customers and agents
- **Agent → Message** — An agent creates messages in response to tickets

## Enumerations

**Ticket Priority:** Low, Medium, High, Urgent
**Ticket Status:** Open, In Progress, Resolved, Closed
**Message Sender Type:** Agent, Customer
**Agent Status:** Active, Inactive
