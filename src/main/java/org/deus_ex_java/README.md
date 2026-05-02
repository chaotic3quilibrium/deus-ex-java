<a href="https://github.com/chaotic3quilibrium/deus-ex-java" target="_blank"><span style="font-family:default; font-size:2.35em; color:#5FA845">
deus-ex-java</span></a>

- Copyright (C) 2026 [Jim O'Flaherty, Jr.](jim.oflaherty.jr+dejrmh1@gmail.com)

- [`v1.8.0`](#v180)

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
        * [REALLY HATE the GNU AFFERO GENERAL PUBLIC LICENSE, a.k.a AGPLv3?](#really-hate-the-gnu-affero-general-public-license-aka-agplv3)
* [Version History](#version-history)
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

As a library targeting Java 17, deus-ex-java is a Java augmentation library to ease the incremental modernization of
legacy Enterprise IT systems via a series of continuous improvements via micro-transformations

- [Java Janitor Jim](https://javajanitorjim.substack.com) [Guiding Motivations](https://javajanitorjim.substack.com/p/java-janitor-jim-the-rewarding-journey)

## Installation

### Maven Coordinates:

```
  <dependency>
    <groupId>io.github.chaotic3quilibrium</groupId>
    <artifactId>deus-ex-java</artifactId>
    <version>1.8.0</version>
  </dependency>
```

### QuickStart Introduction

TODO: Fill this in

# Philosophy

## Why Use deus-ex-java?

As detailed in
this [Java Janitor Jim Substack](https://javajanitorjim.substack.com) [article](https://javajanitorjim.substack.com/p/java-janitor-jim-the-rewarding-journey),
the purpose is to help take legacy enterprise IT Java codebases, and gently refactor them towards later versions of Java
and better software engineering practices.

> "Java is a blue collar language. It's not PhD thesis material, but a language for a job."
> - James Gosling at the 1996 OOPSLA

If one of the compelling motivations of Java is to design and biased towards "Integrity by Default" (excellent JavaOne
2025 [talk/video](https://www.youtube.com/watch?v=uTPRTkny7kQ)), deus-ex-java builds atop that with "integrity by
design".

The fundamental idea is to move towards complex systems composed emergently of simpler systems. Hence, the deep bias on
preventing invalid states from being instantiate-able, or even representable.

High level overview:

- Not a single line of this codebase is AI generated, including all tests
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
    - Referential transparency over allowing side-effects (including throwing an `Exception`)
    - Error-by-Value (Ex: `Either<RuntimeException, T>`) over Error-by-Thrown-Exception
    - Type inference (Ex: `var`) over explicitly specifying types
    - Instantiating only valid states over ambiguous and non-deterministic valid+invalid states
    - DRY (Don't Repeat Yourself) over copy/pasting (a.k.a. copy-pasta)
    - Expressions (functional) over statements (imperative)
    - Entirely avoiding implementing insecure and fragile Java Serialization, prefer any of the
      other [well-designed alternatives](https://www.baeldung.com/java-serialization-approaches)
    - Unmodifiable collections over mutable collections
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
        - no other meaning for `null` is allowed other than a reference has not-yet-initialized; i.e. it never means "
          default"
        - `null` is filtered out (typically as a `flatMap`) everywhere it is encountered
        - `null` is considered a compile-time type hole, and is biased towards producing run-time errors, as opposed to
          the more desirable compile-time errors
        - use `Optional`, `Either`, or `FunctionOps.ifThenElse*` to handle alternatives at compile time
        - to prefer the use of a permissive perimeter where null is transformed and then eliminated from the internal
          system
    - encapsulating checked exceptions with a runtime exception
        - biases to error-by-value (via an `Either` or `Optional`), as opposed to error-by-throw-exception
        - all error functionality is assumed to be `RuntimeException`; i.e. no checked exception signatures, or must
          wrap those outside of one's control
        - use `TryCatchesOps.wrapCheckedException` to easily wrap all checked exceptions with a well-known runtime
          exception
    - using expressions, as opposed to statements
        - encapsulates iteration statements into `Stream` patterns
        - encapsulates exception handling `try`/`catch` statements into the `TryCatchesOps.wrap*` patterns
        - encapsulates resource handling with `try()` statements into the `Using.apply` patterns
        - encapsulates `if` statements into ADTs, `Optional`, `Either`, `FunctionOps.ifThenElse*`, etc.
    - offering two types of immutable "instantiation" factory methods
        - `unsafeFrom` which throws an exception if its preconditions fail
        - `from` which returns an `Either` where the right returns the successful obtaining of the value, otherwise the
          left contains the `Exception` that would have been thrown when the precondition fails

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

The deus-ex-java files are free software: you can redistribute it and/or modify it under the terms of the GNU Affero
General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
details.

You should have received a copy of
the [GNU Affero General Public License](https://www.gnu.org/licenses/agpl-3.0.en.html) along with this program. If not,
see <https://www.gnu.org/licenses/>.

---

### REALLY HATE the GNU AFFERO GENERAL PUBLIC LICENSE, a.k.a AGPLv3?

- No Worries, I'd Love to Work with You

If the AGPLv3 doesn't work for you, I would LOVE to work with you to generate a *
*custom/different/commercial/non-profit/government license** for deus-ex-java.

Please email: <jim.oflaherty.jr+dejrml@gmail.com>, letting us know what license you would prefer. I am happyto discuss
this with you.

---

# Version History

## v1.8.0

- 2026.05.01
- Fixed latent issue in Either with the .flatMapLeft and .flatMapRight methods, and added test coverage specifically for
  the changes
- Added parse* methods to IntegersOps
- Made ListsOps.toDistinctSortedListInteger generic as toDistinctSortedList
- Enhanced the CollectionsOps.isUnmodifiable implementations
- Added filter/filterNot by class methods to StreamsOps
- Added static factories returning the appropriate interface type for class specific implementations of ArrayList,
  LinkedList, HashSet, LinkedHashSet, HashMap, and LinkedHashMap to enable use in functions where the interface is
  returned, not the implementation class type
- Refactored the SetsOps.SetPair implementation off of a Java record and into a final class, and remove the deprecated
  method contrastSetPair
- Refactored the refined NonEmpty* collection types to gracefully handle null, even though the class is using the
  @NullMarked annotation
- Expanded test coverage

## v1.7.0

- 2026.01.17
- Added TernaryOps, a utility class reifying the if statement and ?: (a.k.a. ternary) operator
- Refactored EnumAndIdsOps to surface caching status, and expanded tests to cover additional surface area
- Added to StringsOps the String.regionMatches versions of ignore case for indexOf, lastIndexof, and equals to better
  handle Unicode comparison issues, especially around other languages like the Turkish
- Refactored Enum*sOps to use ClassValue as caching, and entirely removed the Memoizer dependency

## v1.6.0

- 2025.12.25
- deprecated SetsOps.contrastSetPair, and added ADT record SetPair<T> with toMap method
- replaced Enum*sOps.FormatBuilder's filter parameter to Predicate<*> from Function<Stream<*>, Stream<*>> and added two
  collection/stream helpers

## v1.5.0

- 2025.12.14
- Removed vestigial FatalThrowable
- Transitioned Enum*Ops to a FormatBuilder and off of the join method permutations
- Refactored TryCachesOps to more consistently implement all the various "sneaky" checked exception throwing pathways
- Completed full test coverage for TryCatchesOps, including for new ControlBreakThrowable
- Added ControlBreakThrowable as "fatal error tunnel" for future implementation of Break mode based on Scala's pattern
  of the same name
- Updated SetsOps.toDistinctAndDupes* to be threadsafe
- Added collection/stream elements comparisons for both left-to-right and right-to-left scanning

## v1.4.0

- 2025.11.30
- Added SetsOps x4 methods, toDistinctAndDupes() and toDistinctAndDupesOrdered()
- Completely refactored away FatalThrowable, and replaced with ForcedFatalThrowable to ensure fatal exceptions (
  including checked exceptions, like InterruptedException) flow by unhindered by library encapsulaters and guards
- Refactored remaining try/catch statements to use TryCatchesOps to ensure correct handling using ForcedFatalThrowable
- Tidied up javadocs for ForcedFatalThrowable and WrappedCheckedException

## v1.3.0

- 2025.11.27
- Transitioned to JSpecify and off of JetBrains nullability library
- Enhanced the refined collection classes
- Expanded the testing coverage for the Collection helpers
- Added SetsOps.containsAny
- Enhanced multiple places allowing for a Collection to be passed, and it is the forwards to the method overload taking
  a Stream
- Added a RuntimeException of FatalThrowable to differentiate from WrappedCheckedException to enable local encapsulation
  of all checked exceptions, and allowing simple catch near the thread root to re-throw the wrapped fatal exception

## v1.2.0

- 2025.11.08
- Added convenient Collection twins for the Stream methods in ListsOps, SetsOps, and MapsOps
- Expanded more tests
- Tweaked javadocs

## v1.1.0

- 2025.11.01
- Added tons of tests
    - Remaining tests needed marked by "TODO:" comments
- Fixes and tweaks related to defects exposed by expanded testing surface

## v1.0.0

- 2025.10.19
- Initial release
