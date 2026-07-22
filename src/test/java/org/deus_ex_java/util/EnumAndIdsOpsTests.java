package org.deus_ex_java.util;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.lang.refined.NonEmptyLowerCaseString;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

public class EnumAndIdsOpsTests {
  public interface EquivalentInt {
    @SuppressWarnings("unused")
    int getEquivalent();
  }

  private enum TrafficLightWithIdA implements EquivalentInt {
    SGREEN(1),
    SYELLOW(2),
    SRED(5);

    private final int equivalent;

    TrafficLightWithIdA(int equivalent) {
      this.equivalent = equivalent;
    }

    public int getEquivalent() {
      return this.equivalent;
    }
  }

  @Test
  public void testOrdinal() {
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdA.class).isEmpty());
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdA.class, Integer.class).isEmpty());
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdA.class, Number.class).isEmpty());
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdA.class, String.class).isEmpty());
    var enumAndIdsOpsAndIsCachingA = EnumAndIdsOps.fromAndIsCaching(TrafficLightWithIdA.class);
    assertTrue(enumAndIdsOpsAndIsCachingA.isCaching());
    var enumAndIdsOpsAndIsCachingB = EnumAndIdsOps.fromAndIsCaching(TrafficLightWithIdA.class);
    assertFalse(enumAndIdsOpsAndIsCachingB.isCaching());
    assertSame(enumAndIdsOpsAndIsCachingA.enumAndIdsOps(), enumAndIdsOpsAndIsCachingB.enumAndIdsOps());
    var tlwixoo = EnumAndIdsOps.from(TrafficLightWithIdA.class);
    assertSame(enumAndIdsOpsAndIsCachingA.enumAndIdsOps(), tlwixoo);
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdA.class).isPresent());
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdA.class, Integer.class).isPresent());
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdA.class, Number.class).isEmpty());
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdA.class, String.class).isEmpty());
    assertEquals("SGREEN(0), SYELLOW(1), SRED(2)", tlwixoo.getFormatBuilder().join());
    assertEquals(Optional.of(TrafficLightWithIdA.SGREEN), tlwixoo.get(0));
    assertEquals(Optional.of(entry(TrafficLightWithIdA.SGREEN, 0)), tlwixoo.valueOf("0"));
    assertEquals(Optional.of(entry(TrafficLightWithIdA.SGREEN, 0)), tlwixoo.valueOf("sgreen"));
    assertEquals(Optional.of(TrafficLightWithIdA.SYELLOW), tlwixoo.get(1));
    assertEquals(Optional.of(entry(TrafficLightWithIdA.SYELLOW, 1)), tlwixoo.valueOf("1"));
    assertEquals(Optional.of(entry(TrafficLightWithIdA.SYELLOW, 1)), tlwixoo.valueOf("sYeLlOw"));
    assertEquals(Optional.of(TrafficLightWithIdA.SRED), tlwixoo.get(2));
    assertEquals(Optional.of(entry(TrafficLightWithIdA.SRED, 2)), tlwixoo.valueOf("2"));
    assertEquals(Optional.of(entry(TrafficLightWithIdA.SRED, 2)), tlwixoo.valueOf("SREd"));
  }

  private enum TrafficLightWithIdB implements EquivalentInt {
    SGREEN(1),
    SYELLOW(2),
    SRED(5);

    private final int equivalent;

    TrafficLightWithIdB(int equivalent) {
      this.equivalent = equivalent;
    }

    public int getEquivalent() {
      return this.equivalent;
    }
  }

  @Test
  public void testOrdinalOffset() {
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdB.class).isEmpty());
    var enumAndIdsOpsAndIsCachingA = EnumAndIdsOps.fromAndIsCaching(TrafficLightWithIdB.class, 1);
    assertTrue(enumAndIdsOpsAndIsCachingA.isCaching());
    var enumAndIdsOpsAndIsCachingB = EnumAndIdsOps.fromAndIsCaching(TrafficLightWithIdB.class, 1);
    assertFalse(enumAndIdsOpsAndIsCachingB.isCaching());
    assertSame(enumAndIdsOpsAndIsCachingA.enumAndIdsOps(), enumAndIdsOpsAndIsCachingB.enumAndIdsOps());
    var tlwixoo = EnumAndIdsOps.from(TrafficLightWithIdB.class, 1);
    assertSame(enumAndIdsOpsAndIsCachingA.enumAndIdsOps(), tlwixoo);
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdB.class).isPresent());
    assertEquals("SGREEN(1), SYELLOW(2), SRED(3)", tlwixoo.getFormatBuilder().join());
    assertEquals(Optional.of(TrafficLightWithIdB.SGREEN), tlwixoo.get(1));
    assertEquals(Optional.of(entry(TrafficLightWithIdB.SGREEN, 1)), tlwixoo.valueOf("1"));
    assertEquals(Optional.of(entry(TrafficLightWithIdB.SGREEN, 1)), tlwixoo.valueOf("sgreen"));
    assertEquals(Optional.of(TrafficLightWithIdB.SYELLOW), tlwixoo.get(2));
    assertEquals(Optional.of(entry(TrafficLightWithIdB.SYELLOW, 2)), tlwixoo.valueOf("2"));
    assertEquals(Optional.of(entry(TrafficLightWithIdB.SYELLOW, 2)), tlwixoo.valueOf("sYeLlOw"));
    assertEquals(Optional.of(TrafficLightWithIdB.SRED), tlwixoo.get(3));
    assertEquals(Optional.of(entry(TrafficLightWithIdB.SRED, 3)), tlwixoo.valueOf("3"));
    assertEquals(Optional.of(entry(TrafficLightWithIdB.SRED, 3)), tlwixoo.valueOf("SREd"));
  }

  private enum TrafficLightWithIdC implements EquivalentInt {
    SGREEN(1),
    SYELLOW(2),
    SRED(5);

    private final int equivalent;

    TrafficLightWithIdC(int equivalent) {
      this.equivalent = equivalent;
    }

    public int getEquivalent() {
      return this.equivalent;
    }

    public Entry<TrafficLightWithIdC, Integer> asEntry() {
      return entry(this, this.getEquivalent());
    }
  }

  @Test
  public void testTrafficLightWithIdX2() {
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdC.class).isEmpty());
    var enumAndIdsOpsAndIsCachingA = EnumAndIdsOps.fromAndIsCaching(
        TrafficLightWithIdC.class,
        TrafficLightWithIdC::getEquivalent);
    assertTrue(enumAndIdsOpsAndIsCachingA.isCaching());
    var enumAndIdsOpsAndIsCachingB = EnumAndIdsOps.fromAndIsCaching(
        TrafficLightWithIdC.class,
        TrafficLightWithIdC::getEquivalent);
    assertFalse(enumAndIdsOpsAndIsCachingB.isCaching());
    assertSame(enumAndIdsOpsAndIsCachingA.enumAndIdsOps(), enumAndIdsOpsAndIsCachingB.enumAndIdsOps());
    var tlwix = EnumAndIdsOps.from(
        TrafficLightWithIdC.class,
        TrafficLightWithIdC::getEquivalent);
    assertSame(enumAndIdsOpsAndIsCachingA.enumAndIdsOps(), tlwix);
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdC.class).isPresent());
    assertEquals("SGREEN(1), SYELLOW(2), SRED(5)", tlwix.getFormatBuilder().join());
    assertEquals(Optional.of(TrafficLightWithIdC.SGREEN), tlwix.get(1));
    assertEquals(Optional.of(TrafficLightWithIdC.SGREEN.asEntry()), tlwix.valueOf("1"));
    assertEquals(Optional.of(TrafficLightWithIdC.SGREEN.asEntry()), tlwix.valueOf("sgreen"));
    assertEquals(Optional.of(TrafficLightWithIdC.SYELLOW), tlwix.get(2));
    assertEquals(Optional.of(TrafficLightWithIdC.SYELLOW.asEntry()), tlwix.valueOf("2"));
    assertEquals(Optional.of(TrafficLightWithIdC.SYELLOW.asEntry()), tlwix.valueOf("sYeLlOw"));
    assertEquals(Optional.of(TrafficLightWithIdC.SRED), tlwix.get(5));
    assertEquals(Optional.of(TrafficLightWithIdC.SRED.asEntry()), tlwix.valueOf("5"));
    assertEquals(Optional.of(TrafficLightWithIdC.SRED.asEntry()), tlwix.valueOf("SREd"));
  }

  private enum TrafficLightWithIdD implements EquivalentInt {
    SGREEN(1),
    SYELLOW(2),
    SRED(5);

    private final int equivalent;

    TrafficLightWithIdD(int equivalent) {
      this.equivalent = equivalent;
    }

    public int getEquivalent() {
      return this.equivalent;
    }
  }

  @Test
  public void testTrafficLightWithIdX3() {
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdD.class).isEmpty());
    var enumAndIdsOpsAndIsCachingA = EnumAndIdsOps.fromAndIsCaching(
        TrafficLightWithIdD.class,
        TrafficLightWithIdD::getEquivalent,
        enumValueAndId ->
            new NonEmptyLowerCaseString(("C" + enumValueAndId.getValue() + "!").toLowerCase()));
    assertTrue(enumAndIdsOpsAndIsCachingA.isCaching());
    var enumAndIdsOpsAndIsCachingB = EnumAndIdsOps.fromAndIsCaching(
        TrafficLightWithIdD.class,
        TrafficLightWithIdD::getEquivalent,
        enumValueAndId ->
            new NonEmptyLowerCaseString(("C" + enumValueAndId.getValue() + "!").toLowerCase()));
    assertFalse(enumAndIdsOpsAndIsCachingB.isCaching());
    assertSame(enumAndIdsOpsAndIsCachingA.enumAndIdsOps(), enumAndIdsOpsAndIsCachingB.enumAndIdsOps());
    var tlwix = EnumAndIdsOps.from(
        TrafficLightWithIdD.class,
        TrafficLightWithIdD::getEquivalent,
        enumValueAndId ->
            new NonEmptyLowerCaseString(("C" + enumValueAndId.getValue() + "!").toLowerCase()));
    assertSame(enumAndIdsOpsAndIsCachingA.enumAndIdsOps(), tlwix);
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdD.class).isPresent());
    assertEquals("SGREEN(1), SYELLOW(2), SRED(5)", tlwix.getFormatBuilder().join());
    assertEquals(Optional.of(TrafficLightWithIdD.SGREEN), tlwix.get(1));
    assertEquals(Optional.of(entry(TrafficLightWithIdD.SGREEN, 1)), tlwix.valueOf("c1!"));
    assertEquals(Optional.of(entry(TrafficLightWithIdD.SGREEN, 1)), tlwix.valueOf("sgreen"));
    assertEquals(Optional.of(TrafficLightWithIdD.SYELLOW), tlwix.get(2));
    assertEquals(Optional.of(entry(TrafficLightWithIdD.SYELLOW, 2)), tlwix.valueOf("c2!"));
    assertEquals(Optional.of(entry(TrafficLightWithIdD.SYELLOW, 2)), tlwix.valueOf("sYeLlOw"));
    assertEquals(Optional.of(TrafficLightWithIdD.SRED), tlwix.get(5));
    assertEquals(Optional.of(entry(TrafficLightWithIdD.SRED, 5)), tlwix.valueOf("c5!"));
    assertEquals(Optional.of(entry(TrafficLightWithIdD.SRED, 5)), tlwix.valueOf("SREd"));
  }

  private enum TrafficLightWithIdE implements EquivalentInt {
    SGREEN(1),
    SYELLOW(2),
    SRED(5);

    private final int equivalent;

    TrafficLightWithIdE(int equivalent) {
      this.equivalent = equivalent;
    }

    public int getEquivalent() {
      return this.equivalent;
    }
  }

  @Test
  public void testTrafficLightWithIdX4() {
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdE.class).isEmpty());
    var enumAndIdsOpsAndIsCachingA = EnumAndIdsOps.fromAndIsCaching(
        TrafficLightWithIdE.class,
        TrafficLightWithIdE::getEquivalent,
        enumValueAndId ->
            new NonEmptyLowerCaseString(enumValueAndId.getValue().toString()),
        enumValueAndId ->
            Set.of(new NonEmptyLowerCaseString("X%d".formatted(enumValueAndId.getKey().name().length()).toLowerCase())));
    assertTrue(enumAndIdsOpsAndIsCachingA.isCaching());
    var enumAndIdsOpsAndIsCachingB = EnumAndIdsOps.fromAndIsCaching(
        TrafficLightWithIdE.class,
        TrafficLightWithIdE::getEquivalent,
        enumValueAndId ->
            new NonEmptyLowerCaseString(enumValueAndId.getValue().toString()),
        enumValueAndId ->
            Set.of(new NonEmptyLowerCaseString("X%d".formatted(enumValueAndId.getKey().name().length()).toLowerCase())));
    assertFalse(enumAndIdsOpsAndIsCachingB.isCaching());
    assertSame(enumAndIdsOpsAndIsCachingA.enumAndIdsOps(), enumAndIdsOpsAndIsCachingB.enumAndIdsOps());
    var tlwix = EnumAndIdsOps.from(
        TrafficLightWithIdE.class,
        TrafficLightWithIdE::getEquivalent,
        enumValueAndId ->
            new NonEmptyLowerCaseString(enumValueAndId.getValue().toString()),
        enumValueAndId ->
            Set.of(new NonEmptyLowerCaseString("X%d".formatted(enumValueAndId.getKey().name().length()).toLowerCase())));
    assertSame(enumAndIdsOpsAndIsCachingA.enumAndIdsOps(), tlwix);
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdE.class).isPresent());
    assertEquals(TrafficLightWithIdE.class, tlwix.getEnumsOps().getClassE());
    assertEquals(Integer.class, tlwix.getClassId());
    assertEquals(
        List.of(
            entry(TrafficLightWithIdE.SGREEN, 1),
            entry(TrafficLightWithIdE.SYELLOW, 2),
            entry(TrafficLightWithIdE.SRED, 5)),
        tlwix.toList());
    assertEquals(
        List.of(
            entry(TrafficLightWithIdE.SGREEN, 1),
            entry(TrafficLightWithIdE.SYELLOW, 2),
            entry(TrafficLightWithIdE.SRED, 5)),
        tlwix.stream().toList());
    assertEquals(
        Set.of("1", "2", "5", "sred", "x4", "x6", "syellow", "sgreen", "x7"),
        tlwix.valueOfLookupKeys());
    assertEquals("SGREEN(1), SYELLOW(2), SRED(5)", tlwix.getFormatBuilder().join());
    assertEquals(1, tlwix.get(TrafficLightWithIdE.SGREEN));
    assertEquals(Optional.of(TrafficLightWithIdE.SGREEN), tlwix.get(1));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SGREEN, 1)), tlwix.valueOf("1"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SGREEN, 1)), tlwix.valueOf("SGREEN"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SGREEN, 1)), tlwix.valueOf("sgreen"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SGREEN, 1)), tlwix.valueOf("sgrEEn"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SGREEN, 1)), tlwix.valueOf("X6"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SGREEN, 1)), tlwix.valueOf("x6"));
    assertEquals(2, tlwix.get(TrafficLightWithIdE.SYELLOW));
    assertEquals(Optional.of(TrafficLightWithIdE.SYELLOW), tlwix.get(2));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SYELLOW, 2)), tlwix.valueOf("2"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SYELLOW, 2)), tlwix.valueOf("SYELLOW"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SYELLOW, 2)), tlwix.valueOf("syellow"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SYELLOW, 2)), tlwix.valueOf("sYelLoW"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SYELLOW, 2)), tlwix.valueOf("X7"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SYELLOW, 2)), tlwix.valueOf("x7"));
    assertEquals(5, tlwix.get(TrafficLightWithIdE.SRED));
    assertEquals(Optional.of(TrafficLightWithIdE.SRED), tlwix.get(5));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SRED, 5)), tlwix.valueOf("5"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SRED, 5)), tlwix.valueOf("SRED"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SRED, 5)), tlwix.valueOf("sred"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SRED, 5)), tlwix.valueOf("sReD"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SRED, 5)), tlwix.valueOf("X4"));
    assertEquals(Optional.of(entry(TrafficLightWithIdE.SRED, 5)), tlwix.valueOf("x4"));
    assertEquals(Optional.empty(), tlwix.get(3));
    assertEquals(Optional.empty(), tlwix.valueOf("SBLUE"));
    assertEquals(Optional.empty(), tlwix.valueOf("3"));
    assertEquals(Optional.empty(), tlwix.valueOf("X5"));
    assertEquals(entry(TrafficLightWithIdE.SGREEN, 1), tlwix.valueOfOrDefaultToFirst("1"));
    assertEquals(entry(TrafficLightWithIdE.SGREEN, 1), tlwix.valueOfOrDefaultToFirst("1a"));
    assertEquals(entry(TrafficLightWithIdE.SYELLOW, 2), tlwix.valueOfOrDefaultToFirst("2"));
    assertEquals(entry(TrafficLightWithIdE.SGREEN, 1), tlwix.valueOfOrDefaultToFirst("2a"));
    assertEquals(entry(TrafficLightWithIdE.SRED, 5), tlwix.valueOfOrDefaultToFirst("5"));
    assertEquals(entry(TrafficLightWithIdE.SGREEN, 1), tlwix.valueOfOrDefaultToFirst("5a"));
    assertEquals(entry(TrafficLightWithIdE.SGREEN, 1), tlwix.valueOf("1").orElse(entry(TrafficLightWithIdE.SYELLOW, 2)));
    assertEquals(entry(TrafficLightWithIdE.SYELLOW, 2), tlwix.valueOf("1a").orElse(entry(TrafficLightWithIdE.SYELLOW, 2)));
    assertEquals(entry(TrafficLightWithIdE.SYELLOW, 2), tlwix.valueOf("2").orElse(entry(TrafficLightWithIdE.SYELLOW, 2)));
    assertEquals(entry(TrafficLightWithIdE.SYELLOW, 2), tlwix.valueOf("2a").orElse(entry(TrafficLightWithIdE.SYELLOW, 2)));
    assertEquals(entry(TrafficLightWithIdE.SRED, 5), tlwix.valueOf("5").orElse(entry(TrafficLightWithIdE.SYELLOW, 2)));
    assertEquals(entry(TrafficLightWithIdE.SYELLOW, 2), tlwix.valueOf("5a").orElse(entry(TrafficLightWithIdE.SYELLOW, 2)));
  }

  private enum TrafficLightWithIdF implements EquivalentInt {
    SGREEN(1),
    SYELLOW(3),
    SRED(3);

    private final int equivalent;

    TrafficLightWithIdF(int equivalent) {
      this.equivalent = equivalent;
    }

    public int getEquivalent() {
      return this.equivalent;
    }
  }

  @Test
  public void testTrafficLightWithIdX4Duplicates() {
    var parametersValidationException = assertThrows(
        ParametersValidationException.class,
        () ->
            EnumAndIdsOps.from(
                TrafficLightWithIdF.class,
                TrafficLightWithIdF::getEquivalent,
                enumValueAndId ->
                    new NonEmptyLowerCaseString(enumValueAndId.getValue().toString()),
                enumValueAndId ->
                    Set.of(new NonEmptyLowerCaseString("X%d".formatted(enumValueAndId.getKey().getEquivalent()).toLowerCase()))));
    assertEquals(
        "EnumAndIdsOps invalid parameter(s) - Parameter Validation Failures: [invalid state for enum [TrafficLightWithIdF] where the .toLowerCase() of enumValue.toString(), fEAndIdToNonEmptyLowerCaseString.apply(enumValue, id), and optionalFEAndIdToNonEmptyLowerCaseStrings.map(fEAndIdToNonEmptyLowerCaseStrings -> fEAndIdToNonEmptyLowerCaseStrings.apply(enumValue, id)).get() is not unique across all the enums values - erred values: " +
            "keyLowerCase: 3 -> enumValueName: SYELLOW -> collisionSource: ID_VALUE, " +
            "keyLowerCase: x3 -> enumValueName: SYELLOW -> collisionSource: ALTERNATE_STRING_VALUE, " +
            "keyLowerCase: 3 -> enumValueName: SRED -> collisionSource: ID_VALUE, " +
            "keyLowerCase: x3 -> enumValueName: SRED -> collisionSource: ALTERNATE_STRING_VALUE]",
        parametersValidationException.getMessage());
    assertEquals(1, parametersValidationException.getParametersValidationFailureMessages().size());
  }

  public interface EquivalentString {
    @SuppressWarnings("unused")
    String getEquivalent();
  }

  private enum TrafficLightWithIdG implements EquivalentString {
    SGREEN("SgreeN"),
    SYELLOW("sYELLOw"),
    SRED("SRed");

    private final String equivalent;

    TrafficLightWithIdG(String equivalent) {
      this.equivalent = equivalent;
    }

    public String getEquivalent() {
      return this.equivalent;
    }
  }

  @Test
  public void testTrafficLightWithIdAndAcceptableNameAndIdAndAltNameDuplicates() {
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdG.class).isEmpty());
    var tlwix = EnumAndIdsOps.from(
        TrafficLightWithIdG.class,
        TrafficLightWithIdG::getEquivalent,
        enumValueAndId ->
            new NonEmptyLowerCaseString(enumValueAndId.getValue().toLowerCase()),
        enumValueAndId ->
            Set.of(new NonEmptyLowerCaseString(enumValueAndId.getKey().name().toLowerCase())));
    assertTrue(EnumAndIdsOps.fromCacheOnly(TrafficLightWithIdG.class).isPresent());
    assertEquals(TrafficLightWithIdG.class, tlwix.getEnumsOps().getClassE());
    assertEquals(String.class, tlwix.getClassId());
    assertEquals(
        List.of(
            entry(TrafficLightWithIdG.SGREEN, "SgreeN"),
            entry(TrafficLightWithIdG.SYELLOW, "sYELLOw"),
            entry(TrafficLightWithIdG.SRED, "SRed")),
        tlwix.toList());
    assertEquals(
        List.of(
            entry(TrafficLightWithIdG.SGREEN, "SgreeN"),
            entry(TrafficLightWithIdG.SYELLOW, "sYELLOw"),
            entry(TrafficLightWithIdG.SRED, "SRed")),
        tlwix.stream().toList());
  }

  private enum TrafficLightWithIdFb implements EquivalentInt {
    SGREEN(1),
    SYELLOW(2),
    SRED(5);

    private final int equivalent;

    TrafficLightWithIdFb(int equivalent) {
      this.equivalent = equivalent;
    }

    public int getEquivalent() {
      return this.equivalent;
    }

    public Entry<TrafficLightWithIdFb, Integer> asEntry() {
      return entry(this, this.getEquivalent());
    }
  }

  @Test
  public void testFormatBuilder() {
    var enumAndIdsOps = EnumAndIdsOps.from(
        TrafficLightWithIdFb.class,
        TrafficLightWithIdFb::getEquivalent);
    var formatBuilderDefaults = enumAndIdsOps.getFormatBuilder();
    //the four defaults
    assertEquals(
        "SGREEN(1), SYELLOW(2), SRED(5)",
        formatBuilderDefaults
            .join());
    //filtering the enum set
    assertEquals(
        "SGREEN(1), SRED(5)",
        formatBuilderDefaults
            .setFilter(trafficLightWithIdAAndId ->
                trafficLightWithIdAAndId.getKey().toString().contains("R"))
            .join());
    //filtering the enum set - collection
    assertEquals(
        "SYELLOW(2), SRED(5)",
        formatBuilderDefaults
            .setFilter(
                List.of(
                    TrafficLightWithIdFb.SYELLOW.asEntry(),
                    TrafficLightWithIdFb.SRED.asEntry()))
            .join());
    //filtering the enum set - stream
    assertEquals(
        "SGREEN(1), SYELLOW(2)",
        formatBuilderDefaults
            .setFilter(
                Stream.of(
                    TrafficLightWithIdFb.SGREEN.asEntry(),
                    TrafficLightWithIdFb.SYELLOW.asEntry()))
            .join());
    //sorting the enum set on the default (by ordinal) in reverse
    assertEquals(
        "SRED(5), SYELLOW(2), SGREEN(1)",
        formatBuilderDefaults
            .setSortStrategy(formatBuilderDefaults.getSortStrategy().reversed())
            .join());
    //reformatting the String
    assertEquals(
        "SGREEN[1], SYELLOW[2], SRED[5]",
        formatBuilderDefaults
            .setReformat(trafficLightWithIdAAndId ->
                "%s[%d]".formatted(
                    trafficLightWithIdAAndId.getKey().toString(),
                    trafficLightWithIdAAndId.getValue()))
            .join());
    //ensure same instance returned when the separator passed is equal to the one already present
    assertSame(formatBuilderDefaults, formatBuilderDefaults.setSeparator(formatBuilderDefaults.getSeparator()));
    //changing the separator
    assertEquals(
        "SGREEN(1),SYELLOW(2),SRED(5)",
        formatBuilderDefaults
            .setSeparator(",")
            .join());
    //changing all four simultaneously
    assertEquals(
        "2 -> SYELLOW|1 -> SGREEN",
        formatBuilderDefaults
            .setFilter(trafficLightWithIdAAndId ->
                trafficLightWithIdAAndId.getValue() < 3)
            .setSortStrategy(formatBuilderDefaults.getSortStrategy().reversed())
            .setReformat(trafficLightWithIdAAndId ->
                "%d -> %s".formatted(
                    trafficLightWithIdAAndId.getValue(),
                    trafficLightWithIdAAndId.getKey().toString()))
            .setSeparator("|")
            .join());
  }
}