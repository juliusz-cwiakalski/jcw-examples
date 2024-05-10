# Full Spectrum Software Testing: Mastering Modern Development with BDD, Modularity, and Beyond

## Introduction

**Note**: Short on time? Read **TL-TR** sections to get straight to the point. ;-)

In the rapidly evolving world of software development, mastering a spectrum of testing and development methodologies is
crucial for delivering high-quality software.
This article dives into an integrated approach that leverages cutting-edge techniques to enhance the development
lifecycle:

- **Behaviour Driven Development (BDD)** using [Spock](https://spockframework.org/), which bridges the communication gap
  between developers, testers, and non-technical stakeholders, ensuring that all parties understand the system's
  behavior.
- **Modular architecture**, which simplifies development, testing and maintenance by dividing systems into manageable,
  easy to understand modules.
- **Unit testing of modules** to ensure each component functions correctly in isolation, enhancing the robustness of the
  software.
- **Integration testing with [Testcontainers](https://testcontainers.com/)**, which offers a reliable environment for
  testing how modules interact with each other and with external services.
- **Mutation testing using [Pitest](https://pitest.org/)**, which challenges your tests by introducing changes to the
  codebase to verify that the tests can detect errors effectively.
- **Automated code formatting**, which maintains consistency and readability across the codebase, reducing the cognitive
  load on developers.

The goal of this project is to demonstrate a holistic and efficient software development process that not only focuses
on technical execution but also emphasizes the importance of communication and understanding across different teams.
By integrating these techniques,
the article aims to provide a blueprint for a streamlined, effective development pipeline
suitable for projects of any size in today’s fast-paced tech environment.

**Table of contents**

<!-- TOC -->
* [Full Spectrum Software Testing: Mastering Modern Development with BDD, Modularity, and Beyond](#full-spectrum-software-testing-mastering-modern-development-with-bdd-modularity-and-beyond)
  * [Introduction](#introduction)
  * [The Role of Behavior Driven Development (BDD)](#the-role-of-behavior-driven-development-bdd)
  * [Building a Modular Architecture](#building-a-modular-architecture)
  * [Comprehensive Testing: Unit to Integration](#comprehensive-testing-unit-to-integration)
  * [Enhancing Reliability with Mutation Testing](#enhancing-reliability-with-mutation-testing)
  * [Automating Code Quality](#automating-code-quality)
  * [[TL-TR] Delivery Process Steps Summary](#tl-tr-delivery-process-steps-summary)
  * [Example Project Introduction](#example-project-introduction)
  * [[TL-TR] Project Structure and Execution](#tl-tr-project-structure-and-execution)
  * [Key Lessons and Evolving Strategies](#key-lessons-and-evolving-strategies)
  * [[TL-TR] Final Thoughts and Future Directions](#tl-tr-final-thoughts-and-future-directions)
  * [Appendix and Additional Resources](#appendix-and-additional-resources)
  * [TODO](#todo)
<!-- TOC -->

## The Role of Behavior Driven Development (BDD)

## Building a Modular Architecture

## Comprehensive Testing: Unit to Integration

## Enhancing Reliability with Mutation Testing

## Automating Code Quality

## [TL-TR] Delivery Process Steps Summary

1. Start with business need definition
    - Describe the problem the software should tackle with
    - Define key objectives
    - Define high-level features or stories that software should support
2. Design what modules must be modified and what changes are required
    - List of components and their responsibilities
    - Diagram explaining the dependencies
    - Main processes description (and diagrams if required)
3. Select functionality subset for iteration, and:
    1. Extend the design with process(es) description to make sure that process aspects relevant for iteration are clear
       and complete
    2. Describe in BDD terms the expected behaviors of the components that have to be changed
    3. Detailed review of BDD scenarios done by developers / QAs / business stakeholders
    4. Correct structure of BDD and map scenarios to modules. Run scenarios and review the spock report readability.
    5. Implement module unit BDD scenarios (design the module facade API).
    6. Implement the module facade
    7. Implement BDD integration tests for key scenarios
    8. Verify coverage and test strength (CI Quality gate)

## Example Project Introduction

Usually, tutorials and articles demonstrating some technique use simple examples
not to distract the reader from the main point.
The Goal of this article is to provide the full working solution of the delivery process which is very complex in
nature.
I decided to build a more complex example following the process described above,
so it not only presents the techniques like BDD or mutation testing,
but it also presents the proposed process in practice.
The project's Git history may be reviewed to see what the steps are.
Please note that implementation will not provide full features scope but it
(and Git history) will demonstrate how such a project could be developed.

- **Step 1 example: Definition of a business problem and main features of loyalty program solution**
  - review [step1-business-problem.md](step1-business-problem.md)
- **Step 2 example: Components design of loyalty program solution**
  - Checkout git tag `bdd-step2`
  - review [step2-design.md](step2-design.md)
- **Iteration 1 - implementing `accumulate-points`**
  - **Step 3.1 example: earning points process described in design**
    - Checkout git tag `bdd-iteration1-step3.1`
    - review [step2-design.md](step2-design.md) 
  - **Step 3.2 example: BDD specification of `accumulate-points`**
    - Checkout git tag `bdd-iteration1-step3.2`
    - build with `./gradlew b` and review [Spock reports](build/spock-reports/index.html) considering business perspective
  - **Step 3.4 example: Implement module unit BDD scenarios and design module facade API**
    - Checkout git tag `XYZ`
  - **Step 3.5 example: Implement the module facade**
    - Checkout git tag `XYZ`
  - **Step 3.6 example: Implement the module facade**
    - Checkout git tag `XYZ`

## [TL-TR] Project Structure and Execution

## Key Lessons and Evolving Strategies

TODO describe what I've tried over the years and comment on conclusions and lessons learned, for example:

- mutation testing does not work with integration testing
- class unit testing + mocking brings little value + cements the code (hard refactoring)
- test pyramid -> test diamond -> back to test pyramid - when to use what?
- integration tests fits CRUDs, unit tests when there's logic
- there are two types of tests - fast and slow
- unit and integration tests alone are not enough -> e2e / contract testing / manual testing still needed
- code coverage alone is dangerous!
- test execution and time matters! (time budget etc)
- some tests can be shared in unit and integation tests (spock traits)
- components should be responsible for logic (verbs) not for things (nouns)
- observability is important
- testing on production is also worth considering
- integration testing via facade vs REST/message broker etc?
- ...

## [TL-TR] Final Thoughts and Future Directions

## Appendix and Additional Resources

## TODO

This section summarizes steps that are required to prepare the example project and article.

- [x] Initial project setup
- [x] Prepare ToC
- [x] Describe a development process (TL-TR) high level
- [x] Document high level requirements for the example project
- [x] Describe design of the example solution
- [x] Extend design with processes description, that will be extended in the iteration
- [ ] Rewrite high level requirements as BDD Spock specification
- [ ] Implement modules unit tests (and design the modules API)
- [ ] Implement example REST API
- [ ] Implement example Kafka integration (in/out)
- [ ] Implement example external system integration via REST
- [ ] Implement integration tests for key functionalities
- [ ] Add more details to development process steps
- [ ] Describe project structure
- [ ] Describe basic terms: BDD, mutational testing
- [ ] Describe some history and evolution of approaches I take
- [ ] List of useful resources
- [ ] Should I always use all the tools?
- [ ] General hints
- [ ] Comment on alternative tools (Gherkin, other mutation testing frameworks)
- [ ] Ask friends for review and feedback ;)
- [ ] Prepare tags / keywords
- [ ] Prepare Maven setup 
