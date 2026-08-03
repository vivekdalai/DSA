# Nailing HLD at Amazon — Question List

Generated: 2026-08-03 · Scope: **in-person onsite loop, HLD/System Design round(s)**

This is the companion to [`Amazon Interview Prep/Nailing_DSA/DP_Question_List.md`](../Nailing_DSA/DP_Question_List.md),
but for the High-Level Design round. Each problem below gets its own note in this
folder, written to build the design up **incrementally** — start with the simplest
possible system, then add one capability (and one diagram) at a time, in a
question-and-answer format so it's easy to rehearse out loud.

---

## 🎯 What Amazon's HLD round actually looks like (2025–2026 reports)

- System design shows up in the **onsite loop**, generally for **SDE2 (L5) and
  above**. A typical SDE2 loop is OA → R1 (DSA) → R2 (LLD) → R3 (Bar Raiser: LPs +
  DSA + LLD) → R4 (HLD). SDE3+ candidates often see **two** design-flavored rounds.
- It's explicitly **conversational, not a silent whiteboard exercise** — interviewers
  say they want to "brainstorm with you and challenge your choices," and evaluate
  clarity, trade-off reasoning, and how you respond to pushback, more than whether you
  recite a memorized architecture.
- Leadership Principles are **woven directly into the technical round** at Amazon,
  unlike some companies that keep design and behavioral fully separate — be ready to
  justify a decision in terms of ownership, customer impact, or operating at scale,
  not just correctness.
- **Level shapes scope**: SDE2 is expected to design a scalable, maintainable solution
  *within a single service or team boundary*. SDE3+ is expected to own more ambiguous
  problems and reason about system evolution beyond one team's domain — and a
  reported recent trend is that **SDE3+ rounds now consistently include a follow-up
  about deployment topology and on-call/operational ownership**, not just the diagram.

---

## 📊 Most-Reported System Design Prompts at Amazon (2026, ranked)

Based on an aggregation of 4,577 real interview reports, these five are the most
frequently reported system design prompts, in order:

| Rank | Problem | Status | Note |
|---|---|---|---|
| 1 | **URL Shortener (TinyURL)** | ✅ [01_URL_Shortener.md](01_URL_Shortener.md) | Most frequently asked — classic, but Amazon interviewers push hard on the analytics/redirect-scale follow-ups |
| 2 | **Recommendation System** | ✅ [04_Recommendation_System.md](04_Recommendation_System.md) | On-brand for Amazon (product recs); ML-system-flavored, less "pure infra" than the others |
| 3 | **Distributed Rate Limiter** | ✅ [02_Rate_Limiter.md](02_Rate_Limiter.md) | Classic infra problem, very follow-up-friendly (algorithms, distributed state, multi-region) |
| 4 | **Notification Service** | ✅ [03_Notification_System.md](03_Notification_System.md) | Distributed/fan-out flavor — distinct from the [existing LLD Observer/Strategy notification code](../../LLD/03_Questions/NotificationSystem/) which is single-process OOD, not this |
| 5 | Parking Lot (OOD) | — not covered here | This is really an LLD/OOD problem (classes, not distributed systems) — see `LLD/` folder instead |

## Also reported (broader net, 2025–2026 sources)

- **Distributed Job Scheduler** — ✅ [05_Distributed_Job_Scheduler.md](05_Distributed_Job_Scheduler.md) — reported directly ("design a system to schedule jobs in a distributed environment"); strong Amazon-fulfillment-style framing
- **Typeahead / Autocomplete Search Suggestions** — ✅ [06_Typeahead_Autocomplete.md](06_Typeahead_Autocomplete.md) — reported directly ("typeahead box for a search engine")
- Design Amazon Prime Video (streaming) — not yet covered, ask if wanted
- Design a distributed logging system — not yet covered, ask if wanted
- Design a system that delivers firmware updates to devices (IoT fleet) — not yet covered
- Design Amazon Locker location service (available lockers on map/list at checkout) — Amazon-specific, not yet covered, fun one to add later
- Design a system to track review abuse on Amazon.com — not yet covered
- Design a leaderboard (e.g. gaming/fantasy sports style, 100k+ teams) — not yet covered
- Design Instagram / Design Airbnb search / Design Slack — generic FAANG staples, lower Amazon-specific priority
- Reservation + payment system for a parking garage — hybrid OOD/HLD, lower priority here

The first six above (ranked #1–4 plus the two "also reported" picks) are fully written
up in this folder now. The rest are listed so nothing gets lost — say the word and any
of them can get the same treatment.

---

## How each note in this folder is structured

Every `NN_Problem_Name.md` file follows the same shape, in Q&A format for easy
rehearsal:

1. **Functional Requirements** — what the system must do
2. **Non-Functional Requirements** — scale, latency, availability, consistency asks
3. **Core Entities** — the nouns of the system
4. **API Design** — the verbs, as concrete endpoints
5. **HLD Walkthrough — built incrementally.** Diagram 1 is deliberately too simple;
   each subsequent step asks "what breaks?", answers it, and grows the *same*
   diagram by one capability. No single diagram tries to show the whole final system
   at once — you watch it get built, the way you'd actually explain it out loud.
6. **Deep Dives** — the harder follow-up questions interviewers actually chase, Q&A
7. **Common Questions Asked** — quick-fire interviewer follow-ups worth having a
   one-liner answer ready for
8. **Trade-offs Considered** — table of decision vs. benefit vs. cost
9. **Alternative Approaches / Technologies** — what else could work and when you'd
   reach for it instead

---

## Sources

- [Top Amazon System Design Interview Questions in 2026 — Interview Kickstart](https://interviewkickstart.com/blogs/interview-questions/amazon-system-design-interview-questions)
- [Amazon System Design Interview (2026 Guide) — Exponent](https://www.tryexponent.com/blog/amazon-system-design-interview)
- [Amazon System Design Interview Questions & Answers — System Design Handbook](https://www.systemdesignhandbook.com/blog/amazon-system-design-interview-questions/)
- [Amazon SDE Interview Questions 2026: data from 4,577 real reports](https://gist.github.com/techreign/23503909ee1ae6860b12bd5c7b0dd4de)
- [Difference Between SDE2 and SDE3 System Design Interviews at Amazon — System Design Handbook](https://www.systemdesignhandbook.com/answers/difference-between-sde2-and-sde3-system-design-interviews-at-amazon/)
- [SDE II Interview Prep — amazon.jobs](https://amazon.jobs/content/en/how-we-hire/sde-ii-interview-prep)
- [Cracking Amazon System Design Interview: Top Questions and Answers — DEV Community](https://dev.to/fahimulhaq/cracking-amazon-system-design-interview-top-questions-and-answer-45i1)
- [Amazon System Design Interview (questions, process, prep) — IGotAnOffer](https://igotanoffer.com/en/advice/amazon-system-design-interview)
- [system design round at amazon interview — Blind](https://www.teamblind.com/post/system-design-round-at-amazon-interview-w0vgwcg0)
- [Amazon System Design Questions — Exponent question bank](https://www.tryexponent.com/questions?company=amazon&type=system-design)
