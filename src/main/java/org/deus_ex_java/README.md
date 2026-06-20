<a href="https://github.com/chaotic3quilibrium/deus-ex-java" target="_blank"><span style="font-family:default; font-size:2.35em; color:#5FA845">
deus-ex-java</span></a>

- Copyright (C) 2026 [Jim O'Flaherty, Jr.](jim.oflaherty.jr+dejrmh1@gmail.com)

- [`v1.9.0`](#v190)

---

# Table of Contents <!-- omit in toc -->

<!-- TOC -->
* [Table of Contents <!-- omit in toc -->](#table-of-contents----omit-in-toc---)
* [Welcome](#welcome)
  * [Installation](#installation)
    * [Maven Coordinates:](#maven-coordinates)
    * [QuickStart Introduction](#quickstart-introduction)
* [Philosophy](#philosophy)
  * [Why Use deus-ex-java?](#why-use-deus-ex-java)
* [Support](#support)
* [Legal](#legal)
  * [License](#license)
    * [GNU AFFERO GENERAL PUBLIC LICENSE](#gnu-affero-general-public-license)
    * [REALLY HATE the GNU AFFERO GENERAL PUBLIC LICENSE, a.k.a. AGPLv3?](#really-hate-the-gnu-affero-general-public-license-aka-agplv3)
    * [FYI, I'd prefer to move deus-ex-java to an Apache 2.0 license](#fyi-id-prefer-to-move-deus-ex-java-to-an-apache-20-license)
* [Version History](#version-history)
  * [v1.9.0](#v190)
  * [v1.8.0](#v180)
  * [v1.7.0](#v170)
  * [v1.6.0](#v160)
  * [v1.5.0](#v150)
  * [v1.4.0](#v140)
  * [v1.3.0](#v130)
  * [v1.2.0](#v120)
  * [v1.1.0](#v110)
  * [v1.0.0](#v100)
<!-- TOC -->

---

# Welcome

As a library targeting Java 17, deus-ex-java is an augmentation to ease the incremental modernization of legacy Enterprise IT systems via a series of continuous improvements via micro-transformations

- [Java Janitor Jim](https://javajanitorjim.substack.com) [Guiding Motivations](https://javajanitorjim.substack.com/p/java-janitor-jim-the-rewarding-journey)

## Installation

### Maven Coordinates:

```
  <dependency>
    <groupId>io.github.chaotic3quilibrium</groupId>
    <artifactId>deus-ex-java</artifactId>
    <version>1.9.0</version>
  </dependency>
```

### QuickStart Introduction

TODO: Fill this in

# Philosophy

## Why Use deus-ex-java?

As detailed in this [Java Janitor Jim Substack](https://javajanitorjim.substack.com) [article](https://javajanitorjim.substack.com/p/java-janitor-jim-the-rewarding-journey), the purpose is to help take legacy enterprise IT Java codebases, and gently refactor them towards later versions of Java and better software engineering practices.

> "Java is a blue collar language. It's not PhD thesis material, but a language for a job."
> - James Gosling at the 1996 OOPSLA

If one of the compelling motivations of Java is to design and biased towards "Integrity by Default" (excellent JavaOne 2025 [talk/video](https://www.youtube.com/watch?v=uTPRTkny7kQ)), deus-ex-java builds atop that with "integrity by design".

The fundamental idea is to move towards complex systems composed emergently of simpler systems. Hence, the deep bias on preventing invalid states from being instantiate-able, or even representable.

High level overview:

- Strategically simplify Java code by preferring/biasing...
    - Incrementally reducing technical debt over larger/riskier large scale refactor or complete rewrites
    - Pragmatism over purity
    - (mathematical) composability over (linguistic) reusability
    - readability over extensive boilerplate or clever terse code
    - compile time errors over runtime errors
    - immutability over mutability
    - YAGNI (You Aren't Going to Need It) over premature abstraction
- Tactically improve Java code by preferring/biasing...
    - `Optional<T>` over `null`
    - Referential transparency over allowing side effects (including throwing an `Exception`)
    - Type inference (Ex: `var`) over explicitly specifying types
    - Instantiating only valid states over ambiguous and non-deterministic valid+invalid states
    - DRY (Don't Repeat Yourself) over copy/pasting (a.k.a. copy-pasta)
    - Expressions (functional) over statements (imperative)
    - Entirely avoiding implementing insecure and fragile Java Serialization, prefer any of the other [well-designed alternatives](https://www.baeldung.com/java-serialization-approaches)
    - Unmodifiable collections over mutable collections
    - Error-by-Returned-Value (Ex: `Either<RuntimeException, T>`) over Error-by-Thrown-Exception
        - With explicit default coverage for Fatal Exceptions
        - [Exception Flow Chart Diagram](https://mermaid.live/edit#pako:eNqlVV1P2zAU_SuWkShIIaPNB22Y2AMUCWnbQ2GaNrIHk9w0Fokd2TcCVvW_zzFJS6N0D5An-_rec-7J8ceKJjIFGtGskE9JzhSSu6tYxYKY7xbN_Og-pjeiqpFUTLESEJSOPj-oTxe3dVUVHNRhgeeLwyWeOzZ8lyv5xB4K0K7rxvTPMTk5uSDzZ0hqhFVM29EOhLsEPDqO6bqh7ujbRFNORgp0XeCIcE0qJdM6gbSB7VJtt3WSgNYLvsxt0wvAWgky55gbAmXDHc5x09cAz6Z3MjJkCatNUZ_nMofk8ZohKzbZqxttC5ggXGhkIgEiM6twN_HLjsABpKaJX6D7nHORzpWSajf56N5obCbExo2QBCrkUmy17aH4LgdVba1bXXOlkZQMk9xIseq46Pk7oGa72NBcy1qkZGRRRnv8-gqZtUvt2FU00bZwx6wBlh8iszxDiha1QF7C5s90TnU2tS710waU9VMGnWr96Odujeqv9IQNkfS9suJ_KlZVkNoiSN8Q2VXCyJ4EogGRiyXhqJv9rYEw-0deG-lYNL6YjWLPP8l4UUQH2SxzNCr5CNGB53nt-OSJp5hHk-rZSWQhVXTg-34P5M2xbLFm2exDWM2W-SjU8InaiH0n6h7_Pwr7P8vfj00dulQ8pRGqGhxagipZM6WrZj2m5iCWENPIDFPImLk3YxqLtSmrmPgtZdlVKmnuSRplrNBmVlcpQ7jibGmei01UgUhBXZqDijTyPYtBoxV9ptF07Ib-eDLzz_zZNPTC0KEvNPImbjA5DcLp6dQLx96Zt3boX0t66s6CaTA20bNgHASh71BIOUr17fUpsy_a-h_Cw0jV)
        ```mermaid
        flowchart TD
        Start(["Input parameters:<br/>Supplier&lt;R&gt;,<br/>Throwables..."]) --> Execute{"Execute<br/>Supplier.get()"}
        
            Execute -- 'result' is produced --> SuccessRight(["Return Either.right('result')"])
            Execute -- Throwable 't' caught --> CheckFatalThrowable{Is 't' an instance of<br/>FatalThrowable?}
        
            CheckFatalThrowable -- Yes --> EndErrorFatalThrowable([Rethrow Fatal Exception])
            CheckFatalThrowable -- No --> CheckThrowables{First match of 't' in<br/>Throwables?}
        
            CheckThrowables -- Found 'match' --> SuccessLeft(["return Either.left('match')"])
            CheckThrowables -- Unfound --> CheckRuntimeException{Is 't' instanceof<br/>RuntimeException?}
        
            CheckRuntimeException -- Yes --> RethrowRuntimeException([Rethrow RuntimeException])
            CheckRuntimeException -- No --> ThrowWrappedCheckedException([Throw a WrappedCheckedException setting its cause as 't'])
        
            style Start fill:#f9f,stroke:#333,stroke-width:2px,color:#444
            style SuccessRight fill:#9f9,stroke:#333,stroke-width:2px,color:#444
            style SuccessLeft fill:#9f9,stroke:#333,stroke-width:2px,color:#444
            style EndErrorFatalThrowable fill:#f99,stroke:#333,stroke-width:2px,color:#444
            style RethrowRuntimeException fill:#f99,stroke:#333,stroke-width:2px,color:#444
            style ThrowWrappedCheckedException fill:#f99,stroke:#333,stroke-width:2px,color:#444
        ```
- This is NOT an FP library
    - please use https://vavr.io if aiming more closely for FP purity
    - it IS a library which could allow an easier transition towards FP purity
    - it is a library that anticipates the long-term intentions and directions of the Java architects
- This library is highly biased towards...
    - "preventing invalid states from being instantiatable" or "transitioned into"
        - a constructor's preconditions are also the instance's invariant
        - always attempts to produce deeply immutable instances
        - preferring the use of a tuple or a builder for moving through "partial" states prior to instantiation
    - reducing the use of `null`
        - no other meaning for `null` is allowed other than a reference has not-yet-initialized; i.e. it never means "default"
        - `null` is filtered out (typically as a `flatMap`) everywhere it is encountered
        - `null` is considered a compile-time type hole, and is biased towards producing run-time errors, as opposed to the more desirable compile-time errors
        - use `Optional`, `Either`, or `FunctionOps.ifThenElse*` to handle alternatives at compile time
        - to prefer the use of a permissive perimeter where null is transformed and then eliminated from the internal system
    - encapsulating checked exceptions with a runtime exception
        - biases to error-by-returned-value (via an `Either` or `Optional`), as opposed to error-by-thrown-exception
        - all error functionality is assumed to be `RuntimeException`; i.e. no checked exception signatures, or must wrap those outside of one's control
        - use `TryCatchesOps.wrapCheckedException` to easily wrap all checked exceptions with a well-known runtime exception
    - using expressions, as opposed to statements
        - encapsulates iteration statements into `Stream` patterns
        - encapsulates exception handling `try`/`catch` statements into the `TryCatchesOps.wrap*` patterns
        - encapsulates resource handling with `try()` statements into the `Using.apply` patterns
        - encapsulates `if` statements into ADTs, `Optional`, `Either`, `FunctionOps.ifThenElse*`, etc.
    - offering two types of immutable "instantiation" factory methods
        - `unsafeFrom` which throws an exception if its preconditions fail
        - `from` which returns an `Either` where the right returns the successful obtaining of the value, otherwise the left contains the `Exception` that would have been thrown when the precondition fails

---

# Support

**Website:** <https://github.com/chaotic3quilibrium/deus-ex-java>

**Email:** [jim.oflaherty.jr@gmail.com](mailto:jim.oflaherty.jr+dejrms@gmail.com)

---

# Legal

**Ownership:** deus-ex-java - Copyright © 2026 by Jim O'Flaherty Jr. - All rights reserved.

---

## License

### [GNU AFFERO GENERAL PUBLIC LICENSE](https://github.com/chaotic3quilibrium/deus-ex-java/blob/main/LICENSE.md)

The deus-ex-java files are free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

You should have received a copy of the [GNU Affero General Public License](https://www.gnu.org/licenses/agpl-3.0.en.html) along with this program. If not, see <https://www.gnu.org/licenses/>.

---

### REALLY HATE the GNU AFFERO GENERAL PUBLIC LICENSE, a.k.a. AGPLv3?

- It was chosen entirely because of Amazon's/AWS's (and many other wealthy corporations) historic abuses and exploitation of FOSS (Free Open Source Software)
- No Worries, I'd love to work with you directly to negotiate an ad-hoc license.

So, if the AGPLv3 doesn't work for you, we would LOVE to work with you to generate a **custom/different/commercial/non-profit/government license** for deus-ex-java.

Please email: <jim.oflaherty.jr+dejrml@gmail.com>, letting us know what license you would preferm, as I am happy to discuss this with you.

### FYI, I'd prefer to move deus-ex-java to an Apache 2.0 license

---

# Version History

## v1.9.0

- 2026.05.10
- Fixed various small annoyances
- Added remove* methods to ListsOps, SetsOps, and MapsOps, using Gemini Pro 3.1 to produce the tests and Javadocs

## v1.8.0

- 2026.05.01
- Fixed latent issue in Either with the .flatMapLeft and .flatMapRight methods, and added test coverage specifically for the changes
- Added parse* methods to IntegersOps
- Made ListsOps.toDistinctSortedListInteger generic as toDistinctSortedList
- Enhanced the CollectionsOps.isUnmodifiable implementations
- Added filter/filterNot by class methods to StreamsOps
- Added static factories returning the appropriate interface type for class specific implementations of ArrayList, LinkedList, HashSet, LinkedHashSet, HashMap, and LinkedHashMap to enable use in functions where the interface is returned, not the implementation class type
- Refactored the SetsOps.SetPair implementation off of a Java record and into a final class, and remove the deprecated method contrastSetPair
- Refactored the refined NonEmpty* collection types to gracefully handle null, even though the class is using the @NullMarked annotation
- Expanded test coverage

## v1.7.0

- 2026.01.17
- Added TernaryOps, a utility class reifying the if statement and ?: (a.k.a. ternary) operator
- Refactored EnumAndIdsOps to surface caching status, and expanded tests to cover additional surface area
- Added to StringsOps the String.regionMatches versions of ignore case for indexOf, lastIndexOf, and equals to better handle Unicode comparison issues, especially around other languages like the Turkish
- Refactored Enum*sOps to use ClassValue as caching, and entirely removed the Memoizer dependency

## v1.6.0

- 2025.12.25
- deprecated SetsOps.contrastSetPair, and added ADT record SetPair<T> with toMap method
- replaced Enum*sOps.FormatBuilder's filter parameter to Predicate<*> from Function<Stream<*>, Stream<*>> and added two collection/stream helpers

## v1.5.0

- 2025.12.14
- Removed vestigial FatalThrowable
- Transitioned Enum*Ops to a FormatBuilder and off of the join method permutations
- Refactored TryCachesOps to more consistently implement all the various "sneaky" checked exception throwing pathways
- Completed full test coverage for TryCatchesOps, including for new ControlBreakThrowable
- Added ControlBreakThrowable as "fatal error tunnel" for future implementation of Break mode based on Scala's pattern of the same name
- Updated SetsOps.toDistinctAndDupes* to be threadsafe
- Added collection/stream elements comparisons for both left-to-right and right-to-left scanning

## v1.4.0

- 2025.11.30
- Added SetsOps x4 methods, toDistinctAndDupes() and toDistinctAndDupesOrdered()
- Completely refactored away FatalThrowable, and replaced with ForcedFatalThrowable to ensure fatal exceptions (including checked exceptions, like InterruptedException) flow by unhindered by library encapsulation and guards
- Refactored remaining try/catch statements to use TryCatchesOps to ensure correct handling using ForcedFatalThrowable
- Tidied up Javadocs for ForcedFatalThrowable and WrappedCheckedException

## v1.3.0

- 2025.11.27
- Transitioned to Jspecify and off of JetBrains nullability library
- Enhanced the refined collection classes
- Expanded the testing coverage for the Collection helpers
- Added SetsOps.containsAny
- Enhanced multiple places allowing for a Collection to be passed, and it is the forwards to the method overload taking a Stream
- Added a RuntimeException of FatalThrowable to differentiate from WrappedCheckedException to enable local encapsulation of all checked exceptions, and allowing simple catch near the thread root to re-throw the wrapped fatal exception

## v1.2.0

- 2025.11.08
- Added convenient Collection twins for the Stream methods in ListsOps, SetsOps, and MapsOps
- Expanded more tests
- Tweaked Javadocs

## v1.1.0

- 2025.11.01
- Added tons of tests
    - Remaining tests needed to be marked by "TODO:" comments
- Fixes and tweaks related to defects exposed by expanded testing surface

## v1.0.0

- 2025.10.19
- Initial release
