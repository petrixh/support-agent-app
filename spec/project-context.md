# Project Context

> High-level context for the project: the problem being solved, who it's for, what's in scope, and what constraints apply.

## 1. Vision

A customer support help desk that enables support agents to efficiently manage customer issues through a ticketing system. The system tracks customer problems from creation through resolution, organizing them by priority and assigning them to available agents. Customers can view the status of their tickets and communicate with support agents through a shared ticket communication history. Success means support teams can respond quickly to customer issues and maintain complete communication records.

## 2. Users

**Support Agents** — Help desk staff who:
- View and manage support tickets
- Prioritize tickets in a queue
- Assign tickets to themselves or other agents
- Update ticket status (open, in-progress, resolved, closed)
- Add notes and responses to customer tickets
- View communication history with customers

**Customers** — Users who:
- Create support tickets for issues
- View their tickets and current status
- View all communications and responses from support agents
- Update ticket information if needed

**Support Administrators** — Managers who:
- Manage agents and their availability
- View dashboard with queue metrics

## 3. Constraints

- Single-application system with no external integrations
- Support agent authentication required
- All data is stored and managed within the application
- Email notifications are not required (in-app communication only)

---

# Related Documents

- [Spec README](README.md) — process overview and workflow
- [Architecture](architecture.md) — technology stack and application structure
- [Design System](design-system.md) — theme, component usage, and visual standards
- [Use Case Template](use-cases/use-case-template.md) — template for feature specifications
- [Skills](skills/) — implementation, testing, and visual verification guides
