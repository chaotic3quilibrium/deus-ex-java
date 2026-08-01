This is an abbreviated version of
the [README.md](https://github.com/chaotic3quilibrium/deus-ex-java/tree/main/src/main/java/org/deus_ex_java#readme)
located here:

- https://github.com/chaotic3quilibrium/deus-ex-java/tree/main/src/main/java/org/deus_ex_java#readme

---

<a href="https://github.com/chaotic3quilibrium/deus-ex-java" target="_blank"><span style="font-family:default; font-size:2.35em; color:#5FA845">
deus-ex-java</span></a>

- Version: 1.10.0 - 2026.06.20

---

# Table of Contents <!-- omit in toc -->

<!-- TOC -->
* [Table of Contents <!-- omit in toc -->](#table-of-contents----omit-in-toc---)
* [Welcome](#welcome)
  * [Installation](#installation)
    * [Maven Coordinates](#maven-coordinates)
* [Building & Publishing](#building--publishing)
  * [Everyday development](#everyday-development)
  * [Publishing a release to Maven Central](#publishing-a-release-to-maven-central)
* [Support](#support)
  * [License](#license)
    * [GNU AFFERO GENERAL PUBLIC LICENSE](#gnu-affero-general-public-license)
    * [REALLY HATE the GNU AFFERO GENERAL PUBLIC LICENSE, a.k.a. AGPLv3?](#really-hate-the-gnu-affero-general-public-license-aka-agplv3)
<!-- TOC -->

---

# Welcome

As a library targeting Java 17, deus-ex-java is a Java augmentation library to ease the incremental modernization of
legacy Enterprise IT systems via a series of continuous improvements via micro-transformations

- [Java Janitor Jim](https://javajanitorjim.substack.com) [Guiding Motivations](https://javajanitorjim.substack.com/p/java-janitor-jim-the-rewarding-journey)

## Installation

### Maven Coordinates

```
  <dependency>
    <groupId>io.github.chaotic3quilibrium</groupId>
    <artifactId>deus-ex-java</artifactId>
    <version>1.10.0</version>
  </dependency>
```

# Building & Publishing

deus-ex-java is a JPMS module targeting Java 17. The Maven tooling itself runs on the
latest LTS JDK (Java 25), while compilation and tests are executed on a real Java 17 JDK
via the `maven-toolchains-plugin` (see `docs/toolchain-and-maven-setup.md`).

## Everyday development

```sh
mvn clean verify     # compile + run all tests (also what IntelliJ "Rebuild Project" runs)
mvn clean install    # + install to the local ~/.m2 repository
mvn test -Dtest=EnumAndIdsOpsTests#testTrafficLightWithIdX2   # run a single test/method
```

These builds need no signing keys or publishing credentials.

### Running tests in IntelliJ

Because this is a JPMS module with white-box tests, IntelliJ's native JUnit runner needs
the module read/open edges to JUnit that it does not add itself. Add these once to the
JUnit run-configuration template (**Run → Edit Configurations… → Edit configuration
templates… → JUnit → VM options**) and every gutter/editor test run works:

```
--add-modules org.junit.jupiter.api,org.junit.jupiter.params
--add-reads org.deus.ex.java=org.junit.jupiter.api
--add-reads org.deus.ex.java=org.junit.jupiter.params
--add-opens org.deus.ex.java/org.deus_ex_java.lang=org.junit.platform.commons
--add-opens org.deus.ex.java/org.deus_ex_java.lang.refined=org.junit.platform.commons
--add-opens org.deus.ex.java/org.deus_ex_java.util=org.junit.platform.commons
--add-opens org.deus.ex.java/org.deus_ex_java.util.function=org.junit.platform.commons
--add-opens org.deus.ex.java/org.deus_ex_java.util.refined=org.junit.platform.commons
--add-opens org.deus.ex.java/org.deus_ex_java.util.stream=org.junit.platform.commons
--add-opens org.deus.ex.java/org.deus_ex_java.util.tuple=org.junit.platform.commons
```

These are per-machine (stored in `.idea/workspace.xml`); re-paste them on a fresh checkout,
and add an `--add-opens` line for any new test package. As a committable fallback, Maven
runs tests without any of this — e.g. `mvn test -Dtest=EnumAndIdsOpsTests` — and shared
`.run/` configurations are provided.

## Publishing a release to Maven Central

Publishing-only plugins (GPG signing, source/javadoc jars, and the central publisher) live
in a `release` profile, so the `-Prelease` flag is **required** — a plain `mvn deploy` will
not sign or publish:

```sh
mvn clean deploy -Prelease
```

This additionally requires the `gpg` binary with a configured signing key and Maven Central
credentials (server id `central`) in `~/.m2/settings.xml`. See
`docs/release-profile-and-publishing.md` for details.

# Support

**Website:** <https://github.com/chaotic3quilibrium/deus-ex-java>

**Email:** [jim.oflaherty.jr@gmail.com](mailto:jim.oflaherty.jr+dejrms@gmail.com)

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

### REALLY HATE the GNU AFFERO GENERAL PUBLIC LICENSE, a.k.a. AGPLv3?

- It was chosen entirely because of Amazon's/AWS's (and many other wealthy corporations) historic abuses and
  exploitation of FOSS (Free Open Source Software)
- No Worries, I'd Love to Work with You

If the AGPLv3 doesn't work for you, I would LOVE to work with you to generate a *
*custom/different/commercial/non-profit/government license** for deus-ex-java.

Please email: <jim.oflaherty.jr+dejrml@gmail.com>, letting us know what license you would prefer. I am happy to discuss
this with you.

