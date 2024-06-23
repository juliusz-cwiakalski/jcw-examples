# Full Spectrum Software Testing: Mastering Modern Development with BDD, Modularity, Mutation Testing and Beyond

## Quick Start Guide (TL-TR)

Want a quick start?
Copy-paste below into terminal, review the table of content, and read other sections with **TL-TR** (Too Long To Read)
in name.

**Prerequisite**: make sure you use Java 21.

```bash
git clone https://github.com/juliusz-cwiakalski/jcw-examples.git
cd ./jcw-examples
git checkout 3-bdd-and-mutation-testing-example
cd ./bdd-mutation-testing
./gradlew b

echo "review spock reports (business features): ./build/spock-reports/index.html"
open ./build/spock-reports/index.html

echo "review pitest reports (mutational tests): ./build/reports/pitest/index.html"
open ./build/reports/pitest/index.html

echo "review jacoco reports (test coverage): ./build/reports/jacoco/test/html/index.html"
open ./build/reports/jacoco/test/html/index.html
```

----

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
* [Full Spectrum Software Testing: Mastering Modern Development with BDD, Modularity, Mutation Testing and Beyond](#full-spectrum-software-testing-mastering-modern-development-with-bdd-modularity-mutation-testing-and-beyond)
  * [Quick Start Guide (TL-TR)](#quick-start-guide-tl-tr)
  * [Introduction](#introduction)
  * [Problems Addressed by Modern Software Development Techniques](#problems-addressed-by-modern-software-development-techniques)
    * [Communication Gaps Between Developers, Testers, and Stakeholders](#communication-gaps-between-developers-testers-and-stakeholders)
    * [Missing or Insufficient Business Requirements](#missing-or-insufficient-business-requirements)
    * [Late Changes to Business Requirements](#late-changes-to-business-requirements)
    * [Poor Quality of Tests](#poor-quality-of-tests)
    * [Difficulty in Refactoring](#difficulty-in-refactoring)
    * [Long Build Times](#long-build-times)
    * [Big Ball of Mud](#big-ball-of-mud)
    * [Cognitive Overload](#cognitive-overload)
  * [Building a Modular Architecture](#building-a-modular-architecture)
    * [Characteristics of a Good Module](#characteristics-of-a-good-module)
    * [Structuring a Project for Modular Architecture](#structuring-a-project-for-modular-architecture)
      * [Example project structure](#example-project-structure)
    * [Transitioning from Modular Monolith to Microservices](#transitioning-from-modular-monolith-to-microservices)
  * [The Role of Behavior Driven Development (BDD)](#the-role-of-behavior-driven-development-bdd)
    * [Advantages of BDD](#advantages-of-bdd)
    * [Tips for Creating Effective BDD Scenarios](#tips-for-creating-effective-bdd-scenarios)
  * [Comprehensive Testing: From Unit to Integration](#comprehensive-testing-from-unit-to-integration)
  * [Enhancing Test Reliability with Mutation Testing](#enhancing-test-reliability-with-mutation-testing)
  * [Automating Code Quality](#automating-code-quality)
  * [[TL-TR] Delivery Process Steps Summary](#tl-tr-delivery-process-steps-summary)
  * [Example Project Overview](#example-project-overview)
  * [[TL-TR] Project Structure and Execution](#tl-tr-project-structure-and-execution)
    * [Build the Project and Review Test Reports](#build-the-project-and-review-test-reports)
    * [Key Setup Requirements for Your Project](#key-setup-requirements-for-your-project)
  * [Key Lessons and Evolving Strategies](#key-lessons-and-evolving-strategies)
  * [How to Advocate for This Approach](#how-to-advocate-for-this-approach)
  * [[TL-TR] Final Thoughts and Future Directions](#tl-tr-final-thoughts-and-future-directions)
  * [Appendix and Additional Resources](#appendix-and-additional-resources)
  * [TODO](#todo)
<!-- TOC -->

----

## Problems Addressed by Modern Software Development Techniques

### Communication Gaps Between Developers, Testers, and Stakeholders

**Problem:** Causes misunderstandings and poor information flow.

**Consequences:**

- Unclear understanding of requirements leads to inaccurate implementations.
- Undefined acceptance criteria result in ineffective testing.
- Misalignment of project goals with stakeholder expectations causes rework and delays.
- Lack of visibility into the development process results in unmet business needs.

### Missing or Insufficient Business Requirements

**Problem:** Causes waste and delays due to changes in requirements, rework, discussions, and clarifications.

**Consequences:**

- Increased time and effort spent on rework and clarifications.
- Delays in project timelines due to constant requirement changes.
- Reduced developer productivity and morale.
- Difficulty in planning and estimating project scope.
- Increased risk of delivering a product that does not meet business needs.
- Higher chances of miscommunication between team members and stakeholders.

### Late Changes to Business Requirements

**Problem:** Causes delay and waste due to rework required.

**Consequences:**

- Increased project costs due to additional development and testing cycles.
- Delays in project delivery as teams adjust to new requirements.
- Frustration among team members due to shifting priorities and rework.
- Reduced focus on quality as teams rush to implement changes.
- Difficulty in maintaining consistent project progress.

### Poor Quality of Tests

**Problem:** Despite high coverage, tests are not detecting bugs.

**Consequences:**

- False sense of security leading to undetected bugs in production.
- Increased debugging and fixing efforts post-deployment.
- Higher maintenance costs due to frequent issues.
- Erosion of stakeholder trust in the development process.
- Inefficiencies in the development cycle as poor tests provide misleading results.

### Difficulty in Refactoring

**Problem:** Caused by tests focusing on the implementation details instead of the requirements.

**Consequences:**

- Increased complexity and risk during refactoring.
- Hesitancy to improve or refactor code due to fear of breaking existing functionality.
- Accumulation of technical debt over time.
- Slower development pace as code becomes harder to maintain and extend.
- Difficulty in onboarding new developers due to a complex and rigid codebase.

### Long Build Times

**Problem:** Too much integration or end-to-end (e2e) testing increases the code-test cycle, slowing down the process
and discouraging the use of TDD/BDD.

**Consequences:**

- Decreased developer productivity due to waiting for builds and tests to complete.
- Reduced frequency of code commits and integrations, leading to larger and riskier changes.
- Discouragement of best practices like TDD and BDD.
- Longer feedback loop, making it harder to detect and fix issues early.
- Increased frustration and decreased morale among developers.

### Big Ball of Mud

**Problem:** No real or clear structure or architecture in the project, making it hard to maintain and extend.

**Consequences:**

- Difficulty in understanding and navigating the codebase.
- Increased time and effort required to implement new features or fix bugs.
- Higher likelihood of introducing defects due to lack of structure.
- Difficulty in scaling the project as it grows.
- Increased onboarding time for new developers.

### Cognitive Overload

**Problem:** Large modules with wide responsibilities and numerous dependencies and connections, leading to cognitive
overload.

**Consequences:**

- Developers spend more time understanding complex modules, leading to slower progress and higher error rates.
- The mental effort required to navigate and comprehend large, interconnected modules contributes to higher stress
  levels and burnout among team members.
- Large modules with extensive intra- and inter-connections make it challenging for developers to stay focused and clear
  on their tasks.
- The complexity of understanding how different parts of the module interact hinders quick decision-making and efficient
  problem-solving.
- The cognitive load from dealing with complex, large modules stifles creativity and innovation, leading to lower
  quality solutions and less effective problem-solving.

----

## Building a Modular Architecture

Modular architecture greatly improves the delivery process and software in many fields.
It reduces complexity and cognitive overload, and increases testability and maintainability.

### Characteristics of a Good Module

- **Single Responsibility:** Ideally focused around an action or step in a process (verbs) rather than entities (nouns).
- **Well-Defined Public API:** Exposes the functionality provided by the module to the outside world.
- **Encapsulation:** Internal implementation details are hidden behind the public API.
- **Loosely Coupled:** Depends only on the public APIs of other modules.
- **High Cohesion:** Bundles together related aspects.
- **Independent Data Storage:** Persisted data is independent of other modules and is accessible only via the module's
  public API.

### Structuring a Project for Modular Architecture

There are many possible structures for a project that embraces modular architecture. One effective approach is using a
single package when working with Java (and this can be applied to other languages as well).

The following are the building blocks of a module in this approach:

- **Public API:** Public Java access
    - **Facade:** Exposes the methods to the outside world of the module.
      Facade resides in the main module package, so it can access all module package private classes
    - **DTOs:** Used in the facade to expose data to the outside world.
      Extracted to a separate subpackage called `api` so it can be easily distinguished from implementation
- **Internal Implementation:** Package-private Java access—all resides in the main module package
    - **Module Configuration:** A factory class that constructs the module facade and the module's internal
      dependencies (repositories, etc.). It also shows the external dependencies required for the module.
    - **Internal Dependencies Interfaces:** For example, the API of repositories, message/event publishers, etc.
    - **Internal Implementation Classes:**
        - Entities
        - Repositories implementation
        - Services (if required, a small module can have all the "service" logic directly in the facade)
        - ...
- **Exposing Public API via Required Protocols:** Separate subpackage packages with all classes in package-private scope.
    - **Message/Event Consumers:** Consume the event and call the module's public API via facade (for example, `listeners` subpackage).
    - **gRPC/GraphQL/REST Controllers (etc.):** Convert external synchronous calls into the public API (facade) calls (for example `web` subpackage).


#### Example project structure

This example demonstrates the structure of a module
that is receiving the `OrderCompletedEvent` and generates the invoice.
Once invoice is generated it publishes the `InvoiceIssuedEvent`.
It also provides a web API to fetch the invoices for a customer.

```plaintext
src
└── main
    └── java
        └── com
            └── example
                └── issueinvoice
                    ├── IssueInvoiceFacade.java [public]
                    ├── api
                    │   ├── InvoiceDTO.java [public]
                    │   └── InvoiceIssuedEvent.java [public]
                    ├── IssueInvoiceConfiguration.java
                    ├── InvoiceRepository.java
                    ├── Invoice.java
                    ├── InvoiceRepositoryImpl.java
                    ├── IssueInvoiceService.java
                    ├── listeners
                    │   └── OrderCompletedEventListener.java
                    └── web
                        ├── InvoiceController.java
                        └── InvoiceGraphQLResolver.java
```

Having all classes related to a module in one package may feel odd at first. We've been taught at universities and
courses to have separate packages for entities, repositories, services, etc. You might also think that you'll end up
with too many classes in a single package and that it will be hard to navigate.

However, when modules are focused around behavior/process steps/verbs (for example, `user-registration`) instead of
entities/nouns (`user`), you'll discover that the scope of a module is usually narrow and contains a manageable number
of classes (typically 1–3 dozen). If you create a module that has significantly more classes, then it's probably taking
on too much responsibility and should be split into several modules.

### Transitioning from Modular Monolith to Microservices

Embracing modular architecture has one more benefit. If you find a good reason to extract some functionality as a
separate microservice, it's easier than extracting it from a big ball of mud.

In theory, you cut the module and paste it into a new repository. However, it's not always straightforward due to
differences between local modules and microservices:

1. Local calls are much faster.
2. Local calls are free from network issues.
3. Local calls share transactions.

----

## The Role of Behavior Driven Development (BDD)

Behavior Driven Development (BDD) focuses on software behaviors (features) rather than implementation details. The
perspective to take is: what is the desired outcome? BDD tests do not concern themselves with how the behavior is
implemented. This aligns closely with business goals and the business mindset. In the end, users want to make their
lives easier and get the job done. They don't care about the technical details of how the software works.

The consequence of this perspective is that tests focus on verifying the correct outcomes (what happens) rather than the
implementation details (how it happens).

### Advantages of BDD

- **Readability:** BDD test code is more readable and may serve as a living documentation or specification of
  functionality.
- **Refactoring-Proof:** Tests focus on the outcome, not the implementation details, allowing developers to change the
  internal logic without altering the test code.
- **Encourages Better Design:** Easy refactoring encourages better internal structure and code quality, leading to
  greater maintainability and reduced delivery times.
- **Improved Communication:** BDD promotes better communication between developers, testers, and business stakeholders,
  ensuring everyone has a clear understanding of the requirements and expected outcomes.
- **Early Bug Detection:** By focusing on expected behaviors, BDD helps in catching discrepancies and bugs early in the
  development process.
- **Enhanced Collaboration:** BDD frameworks often use natural language for test descriptions, making it easier for
  non-technical stakeholders to understand and contribute to the development process.
- **User-Centric Approach:** By prioritizing user-focused outcomes, BDD ensures that the software delivers value to
  end-users, aligning development efforts with business objectives.

BDD shifts the focus from how features are implemented to what the features should achieve, aligning development efforts
with business goals and user needs.
This approach not only improves code quality and maintainability but also fosters
better communication and collaboration among all stakeholders involved in the software development process.

### Tips for Creating Effective BDD Scenarios

1. **Focus on User Behavior:**
    - Write scenarios from the user's perspective, describing how they interact with the system and what they expect as
      outcomes.

2. **Use Clear and Simple Language:**
    - Avoid technical jargon and complex sentences. Use simple and clear language that can be easily understood by all
      stakeholders.

3. **Follow the Given-When-Then Structure:**
    - Use the Given-When-Then format to structure scenarios:
        - **Given:** the initial context or setup.
        - **When:** the action or event that triggers the behavior.
        - **Then:** the expected outcome or result.

4. **Be Specific and Concrete:**
    - Provide specific details and examples to avoid ambiguity. Clearly define inputs, actions, and expected results.

5. **Keep Scenarios Focused:**
    - Each scenario should test one specific behavior or feature. Avoid combining multiple behaviors into a single
      scenario.

6. **Avoid Implementation Details:**
    - Focus on what the system should do, not how it should do it. Keep scenarios high-level and outcome-focused.

7. **Show Relevant Data:**
    - In the `Given` section, show relevant data and hide irrelevant data. Use templates with reasonable defaults and
      only set values that alter the behavior under test.

8. **Collaborate with Stakeholders:**
    - Involve developers, testers, business analysts, and other stakeholders in writing and reviewing scenarios to
      ensure they capture the correct requirements and expectations.

9. **Prioritize User Value:**
    - Write scenarios that reflect the most valuable and critical user interactions first. Focus on behaviors that
      deliver the most significant impact to users.

10. **Review and Refine:**
    - Regularly review and refine scenarios to ensure they remain relevant and accurate as the project evolves.

11. **Use Consistent Terminology:**
    - Use consistent terminology across scenarios to avoid confusion and ensure clarity. Define key terms and use them
      consistently.

12. **Leverage Examples:**
    - Use concrete examples to illustrate abstract concepts.
      Examples help in clarifying expectations and reducing misunderstandings.

13. **Automate Scenarios:**
    - Automate BDD scenarios using appropriate tools and frameworks to ensure they are regularly executed and validated
      as part of the development process.

14. **Gherkin is Not a Requirement for BDD:**
    - You can write BDD scenarios using plain xUnit or other testing frameworks by following the above tips
      and extracting test steps into well-named methods
      that represent high-level concepts and hide implementation details.
      Spock is an excellent choice for this approach, as it enhances readability and simplifies development.

----

## Comprehensive Testing: From Unit to Integration

----

## Enhancing Test Reliability with Mutation Testing

----

## Automating Code Quality

----

## [TL-TR] Delivery Process Steps Summary

1. Start with business need definition and clarification
    - Describe the problem the software should tackle with
    - Define key objectives
    - Define high-level features or stories that software should support
    - Identify and document non-functional requirements (NFRs)
    - Conduct initial feasibility analysis (technical and financial)
    - Develop prototypes for key components or the entire system to validate assumptions and gather early feedback.

2. Design what modules must be modified and what changes are required
    - List of components and their responsibilities
    - Diagram explaining the dependencies
    - Main processes description (and diagrams if required)
    - Ensure design addresses non-functional requirements

3. Select functionality subset for iteration, and:
    1. Extend the design with process(es) description to make sure that process aspects relevant for iteration are clear
       and complete.
    2. Double check if UI mockups are available and confirmed by all stakeholders.
    3. Describe in BDD terms the expected behaviors of the components that have to be changed.
       Document any additional acceptance criteria that are not described in BDD scenarios.
    4. Detailed review of BDD scenarios done by developers / QAs / product owner and other stakeholders
    5. Correct structure of BDD and map scenarios to modules. Run scenarios and review the spock report readability.
    6. Implement module unit BDD scenarios (design the module facade API).
    7. Implement the module facade that fulfills all the module unit BDD specifications.
    8. Implement the repositories' unit/integration tests
    9. Implement BDD integration tests for key scenarios
    10. Verify coverage and test strength + improve/extend tests to cover mutations (CI Quality gate)
    11. Integrate and deploy using fully automated CI/CD
    12. Collect user feedback, refine requirements and improve the system based on user experiences
    13. Conduct retrospective to evaluate what went well, and what could be improved for future iterations. Capture
        lessons learned and plan improvement actions

----

## Example Project Overview

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

**Note**: Make sure all below commands are executed in [bdd-mutation-testing](../bdd-mutation-testing) directory.

**TODO**: Correct tags after the process steps numbers changed.

- **Step 1 example: Definition of a business problem and main features of loyalty program solution**
    - Checkout tag: `git checkout bdd-step1`
    - Review [step1-business-problem.md](step1-business-problem.md)
- **Step 2 example: Components design of loyalty program solution**
    - Checkout tag: `git checkout bdd-step2`
    - Review [step2-design.md](step2-design.md)
- **Iteration 1 - implementing `accumulate-points`**
    - **Step 3.1 example: earning points process described in design**
        - Checkout tag: `git checkout bdd-iteration1-step3.1`
        - Review [step2-design.md](step2-design.md)
    - **Step 3.2 example: BDD specification of `accumulate-points`**
        - Checkout tag: `git checkout bdd-iteration1-step3.2`
        - Build with `./gradlew b` and review [Spock reports](build/spock-reports/index.html) considering business
          perspective
    - **Step 3.3 + 3.4 example: review of BDD scenarios + correct mapping of scenarios to modules**
        - done in step 3.2 -> `git checkout bdd-iteration1-step3.2`
    - **Step 3.5 example: Implement module unit BDD scenarios and design module facade API**
        - Checkout tag: `git checkout bdd-iteration1-step3.5`
        -
        See [AccumulatePointsSpec](src/test/groovy/pl/jcw/example/bddmutation/accumulatepoints/AccumulatePointsSpec.groovy)
    - **Step 3.6 example: Implement the module facade that fulfills all the module unit BDD specifications**
        - Checkout tag `git checkout bdd-iteration1-step3.6`
          See [classes in `pl.jcw.example.bddmutation.accumulatepoints` package](src/main/java/pl/jcw/example/bddmutation/accumulatepoints)
    - **Step 3.7 example: Implement the repositories unit/integration tests**
        - Checkout tag `git checkout bdd-iteration1-step3.7`
        -
        See [AccumulatedPointsRepositorySpec](src/test/groovy/pl/jcw/example/bddmutation/accumulatepoints/AccumulatedPointsRepositorySpec.groovy)
    - **Step 3.8 example: Implement BDD integration tests for key scenarios**
        - Checkout tag `git checkout bdd-iteration1-step3.8`
        -
        See [AccumulatePointsIntegrationSpec](src/test/groovy/pl/jcw/example/bddmutation/accumulatepoints/AccumulatePointsIntegrationSpec.groovy)

----

## [TL-TR] Project Structure and Execution

### Build the Project and Review Test Reports

Run `./gradlew b` and review the test reports:

- [Spock report](./build/spock-reports/index.html) - human-friendly report that is a functional requirements
  specification
  and can be reviewed by business stakeholders
- [Pitest report](./build/reports/pitest/index.html) - mutational tests report that shows the weak spots in BDD module
  unit tests

### Key Setup Requirements for Your Project

TODO:

- [ ] spock parallelization
- [ ] pitest
- [ ] spock report + pitest hack

----

## Key Lessons and Evolving Strategies

TODO describe what I've tried over the years and comment on conclusions and lessons learned, for example:

- mutation testing does not work with integration testing
- class unit testing + mocking brings little value + cements the code (hard refactoring)
- test pyramid -> test diamond -> back to test pyramid—when to use what?
- integration tests fits CRUDs, unit tests when there's logic
- there are two types of tests—fast and slow
- unit and integration tests alone are not enough -> e2e / contract testing / manual testing still needed
- code coverage alone is dangerous!
- test execution and time matters! (time budget etc.)
- some tests can be shared in unit and integration tests (spock traits)
- components should be responsible for logic (verbs) not for things (nouns)
- observability is important
- testing on production is also worth considering
- integration testing via facade vs REST/message broker etc?
- enable visibility of public/package private scope in your IDE project explorer (so you can spot the facade
  immediately)
- use tmpfs in test containers to speedup tests
- for calculations or algorithms create excel examples that demonstrate the logic and how it should work - confirm with domain expert that it works as expected and use it as base for future tests
- ...

----

## How to Advocate for This Approach

TODO: describe benefits from different perspectives
(developer, tester, product owner, project manager)

----

## [TL-TR] Final Thoughts and Future Directions

----

## Appendix and Additional Resources


----

## TODO

This section summarizes steps that are required to prepare the example project and article.

- [x] Initial project setup
- [x] Prepare ToC
- [x] Describe a development process (TL-TR) high level
- [x] Document high level requirements for the example project
- [x] Describe design of the example solution
- [x] Extend design with processes description, that will be extended in the iteration
- [x] Rewrite high level requirements as BDD Spock specification
- [x] Implement modules unit tests (and design the modules API)
- [x] Implement example REST API
- [ ] Implement example Kafka integration (in/out)
- [ ] Implement example external system integration via REST
- [x] Implement integration tests for key functionalities
- [ ] Add more details to development process steps
- [ ] Describe project structure
- [ ] Describe basic terms: BDD, mutational testing
- [ ] Describe some history and evolution of approaches I take
- [ ] List of useful resources
- [ ] Should I always use all the tools?
- [ ] Describe what problems they solve, and what are benefits from different stakeholders perspective (dev/QA/product
  owner, project manager)
- [ ] General hints
- [ ] Comment on alternative tools (Gherkin, other mutation testing frameworks)
- [ ] Ask friends for review and feedback ;)
- [ ] Prepare tags / keywords
- [ ] Prepare Maven setup
- [ ] Describe CI/CD setup that assures test strength (mutational tests) is on configured level on branch
