# Nailing LLD at Amazon — Question List

Generated: 2026-08-07 · Scope: **in-person onsite loop, LLD/Object-Oriented Design round(s)**

This is the LLD companion to
[`../Nailing HLD at Amazon/HLD_Question_List.md`](<../Nailing HLD at Amazon/HLD_Question_List.md>),
built the same way: each problem gets its own note in this folder, built up
**incrementally** — clarify requirements, identify the core classes, grow a class
diagram one capability at a time, apply the design pattern that actually earns its
place, then handle the concurrency/edge-case follow-ups Amazon interviewers chase.
Code skeletons are in Java, matching this repo's `LLD/` and `JAVA Fundametals/`
conventions.

---

## What Amazon's LLD round actually looks like

- LLD shows up as a **dedicated round in the onsite loop**, distinct from the HLD/
  system-design round — a typical SDE2 loop is OA → R1 (DSA) → R2 (LLD) → R3 (Bar
  Raiser: LPs + DSA + LLD) → R4 (HLD). Some recent reports say Amazon has trimmed or
  merged rounds for certain teams/levels, so always confirm the loop shape with your
  recruiter — but LLD/OOD prep is never wasted, since it's asked "effectively
  everywhere" for backend SDE roles industry-wide.
- It is fundamentally a **class-design exercise**, not a distributed-systems one:
  the interviewer wants to see you identify the right nouns (classes), the right
  verbs (methods/interfaces), apply a design pattern *because the problem actually
  needs it* (not to show off pattern vocabulary), and write clean, extensible,
  runnable-looking code — usually on a whiteboard/doc, sometimes in an actual IDE.
- **Level shapes scope, same as HLD.** At L4, something like Tic-Tac-Toe is roughly
  full scope. At L5, interviewers expect you to *bring* ambiguity yourself — e.g.
  for Parking Lot: "should this support multiple vehicle types, multiple levels,
  monthly parkers, and payment?" — and handle follow-ups on concurrency and
  extensibility. Being stuck on a "basic" prompt like Parking Lot or Tic-Tac-Toe at
  L5+ is reported as an instant red flag, so speed-and-depth on the fundamentals
  matters more than breadth of memorized problems.
- LPs get woven in here too, same as HLD — be ready to justify a design choice
  ("why Strategy over a big if/else") in terms of maintainability/ownership, not
  just "because the pattern exists."

---

## Most-Reported LLD/OOD Prompts (aggregated across multiple 2025–2026 sources)

These three are the most consistently reported across every source checked, and
are fully written up in this folder:

| Rank | Problem | Status | Note |
|---|---|---|---|
| 1 | **Parking Lot** | ✅ [01_Parking_Lot.md](01_Parking_Lot.md) | The single most universally reported OOD problem at Amazon and everywhere else; the "multi-threaded" variant is explicitly called out on Amazon-specific question banks |
| 2 | **Elevator System** | ✅ [02_Elevator_System.md](02_Elevator_System.md) | Reported as the hardest of the "classic three" — real scheduling logic (SCAN-style dispatch), not just state tracking |
| 3 | **Vending Machine** | ✅ [03_Vending_Machine.md](03_Vending_Machine.md) | The canonical State-pattern problem; also pulls in the coin-change DP problem already in this repo for the "give correct change" follow-up |

## Also Reported (broader net, 2025–2026 sources)

Ranked roughly by how often each shows up across the sources below; not yet
written up — say the word and any of these gets the same treatment.

| Problem | Why it's worth knowing | Key pattern(s) |
|---|---|---|
| **Rate Limiter** | Explicitly called out as bridging LLD and HLD — the class-design version (token bucket/sliding window as objects) is different from the distributed-systems version already covered in [`../Nailing HLD at Amazon/02_Rate_Limiter.md`](<../Nailing HLD at Amazon/02_Rate_Limiter.md>) | Strategy (algorithm swap) |
| **ATM System** | Amazon-specific question bank lists this directly; good vehicle for State + validating a multi-step transaction | State, Chain of Responsibility (card validation steps) |
| **Meeting Room / Hotel Room Booking** | Amazon-specific bank lists "Meeting Room Reservation" directly; core skill tested is overlapping-interval logic, not the pattern | Observer (notify on booking), sorted interval storage |
| **Movie Ticket / Concert Booking System (BookMyShow-style)** | Appears on nearly every list; concurrency-heavy (seat locking during checkout) | Strategy (seat selection), State (seat: available/locked/booked) |
| **Library Management System** | Amazon-specific bank lists this; tests catalog vs. physical-copy modeling and holds/reservations | Observer (holds), Factory |
| **LRU Cache (design + implement)** | Extremely common as a hybrid DSA/LLD question — O(1) get/put via HashMap + doubly linked list, then wrapped in a clean class API | N/A — data-structure design, not a GoF pattern |
| **Splitwise / Expense Sharing** | "Hard" tier on Amazon-specific banks; debt-simplification logic is the real test | Strategy (equal/percentage/exact split), Observer |
| **Online Shopping Cart / Amazon-style Shopping System** | Directly Amazon-flavored ("design Amazon" shows up on general LLD banks too) | Strategy (pricing/discount), Observer (inventory), Decorator (cart add-ons) |
| **Chess / Tic-Tac-Toe** | Reported as lower-ceiling (L4 scope) but still shows up; move-validation and board modeling are the real test | Strategy (player types/AI), Factory (piece creation for Chess) |
| **Notification Service (OOD version)** | Already exists in this repo as a full implemented mini-project, distinct from the distributed/fan-out HLD version — see [`../../LLD/03_Questions/NotificationSystem/`](../../LLD/03_Questions/NotificationSystem/) | Observer, Strategy, Decorator |
| **File System / Logging Framework** | Shows up as an "easy" warm-up prompt on Amazon-specific banks | Composite (files/directories), Singleton (logger) |
| **Car Rental System** | Shows up on general LLD banks; similar shape to Parking Lot with a reservation window added | Strategy (pricing), State (vehicle availability) |

---

## How Each Note in This Folder Is Structured

Every `NN_Problem_Name.md` follows the same shape, adapted from the HLD folder's
format for OOD specifics:

1. **Clarifying Questions** — what to ask the interviewer before designing anything,
   since scope ambiguity is deliberate at L5+
2. **Requirements** — functional and (lighter-weight than HLD) non-functional
3. **Core Objects/Actors** — the nouns of the system
4. **Class Diagram — built incrementally.** Same philosophy as the HLD folder:
   diagram 1 is deliberately too simple, each step asks "what breaks or what's
   missing?", and the *same* diagram grows by one capability at a time
5. **Design Patterns Applied** — Q&A on which pattern fits and, just as important,
   why a simpler approach wouldn't
6. **Core Code Skeleton** — Java class skeletons for the classes that matter, not a
   full working project
7. **Concurrency Deep Dive** — the thread-safety follow-up Amazon interviewers
   reliably chase once the single-threaded design works
8. **Common Follow-Up Questions** — the extension requests interviewers use to probe
   how the design flexes (add a feature, doesn't break the rest)
9. **Trade-offs Considered** — table of decision vs. benefit vs. cost
10. **Interview-Ready Summary** + **Key Concepts to Master**

---

## Sources

- [Top 20 Low Level Design Interview Questions (2026 Guide) — Low Level Design Mastery](https://www.lowleveldesignmastery.com/blog/low-level-design-interview-questions/)
- [Amazon LLD Questions — Low Level Design Mastery](https://www.lowleveldesignmastery.com/playground/company/amazon/)
- [Amazon Low Level Design Interview Round Questions — CodeZym](https://codezym.com/lld/amazon)
- [awesome-low-level-design — ashishps1 (GitHub)](https://github.com/ashishps1/awesome-low-level-design)
- [Amazon HLD LLD DSA Questions — LeetCode Discuss](https://leetcode.com/discuss/post/6906753/amazon-hld-lld-dsa-questions-by-anonymou-sepk/)
- [Any tips on how to ace the low level design for Amazon interview loop? — Blind](https://www.teamblind.com/post/lld-tips-for-interview-jbz6qnbn)
- [Object Oriented Design Interview: An Insider's Guide — ByteByteGo](https://bytebytego.com/courses/object-oriented-design-interview/what-is-an-object-oriented-design-interview)
- [10 OOAD Questions That Actually Prepare You for Real Interviews — Java Revisited](https://javarevisited.wordpress.com/2026/08/01/10-ooad-questions-that-actually-prepare-you-for-real-interviews/)
