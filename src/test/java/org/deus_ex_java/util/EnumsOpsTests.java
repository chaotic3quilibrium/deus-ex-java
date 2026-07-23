package org.deus_ex_java.util;

import org.deus_ex_java.lang.ParametersValidationException;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@NullMarked
public class EnumsOpsTests {

  private enum TrafficLight {
    SGREEN,
    SYELLOW,
    SRED
  }

  private final EnumsOps<TrafficLight> ENUMS_OPS_TRAFFIC_LIGHT = EnumsOps.from(TrafficLight.class);

  private enum TrafficLightBased {
    GREEN,
    YELLOW,
    RED;

    private static final EnumsOps<TrafficLightBased> ENUM_OPS = EnumsOps.from(
        TrafficLightBased.class);

    public static EnumsOps<TrafficLightBased> enumOps() {
      return ENUM_OPS;
    }

    public static List<TrafficLightBased> toList() {
      return enumOps().toList();
    }

    public static Stream<TrafficLightBased> stream() {
      return enumOps().stream();
    }

    public static Optional<TrafficLightBased> valueOfIgnoreCase(String search) {
      return enumOps().valueOf(search);
    }
  }

  private enum TrafficLightToStringLowerCaseConflictX1 {
    GREEN,
    YELLOW,
    RED,
    Red
  }

  private enum TrafficLightToStringLowerCaseConflictX2 {
    GREEN,
    YELLOW,
    RED,
    Red,
    YelloW
  }

  @Test
  public void testConstructor() {
    assertEquals(
        "SGREEN, SYELLOW, SRED",
        ENUMS_OPS_TRAFFIC_LIGHT.getFormatBuilder().join());
    var parametersValidationExceptionX1 =
        assertThrows(
            ParametersValidationException.class,
            () -> EnumsOps.from(TrafficLightToStringLowerCaseConflictX1.class));
    assertEquals(
        "EnumsOps invalid parameter(s) - Parameter Validation Failures: [invalid state for enum [TrafficLightToStringLowerCaseConflictX1] where name().toLowerCase() is not unique across all the enums values - erred values: keyLowerCase: red -> enumValueName: RED, keyLowerCase: red -> enumValueName: Red]",
        parametersValidationExceptionX1.getMessage());
    var parametersValidationExceptionX2 =
        assertThrows(
            ParametersValidationException.class,
            () -> EnumsOps.from(TrafficLightToStringLowerCaseConflictX2.class));
    assertEquals(
        "EnumsOps invalid parameter(s) - Parameter Validation Failures: [invalid state for enum [TrafficLightToStringLowerCaseConflictX2] where name().toLowerCase() is not unique across all the enums values - erred values: keyLowerCase: yellow -> enumValueName: YELLOW, keyLowerCase: red -> enumValueName: RED, keyLowerCase: red -> enumValueName: Red, keyLowerCase: yellow -> enumValueName: YelloW]",
        parametersValidationExceptionX2.getMessage());
  }

  @Test
  public void testCache() {
    assertSame(ENUMS_OPS_TRAFFIC_LIGHT, EnumsOps.from(TrafficLight.class));
    assertSame(ENUMS_OPS_TRAFFIC_LIGHT, EnumsOps.from(ENUMS_OPS_TRAFFIC_LIGHT.getClassE()));
    //noinspection AssertBetweenInconvertibleTypes
    assertNotSame(ENUMS_OPS_TRAFFIC_LIGHT, EnumsOps.from(TrafficLightBased.class));
    assertEquals(
        List.of(
            TrafficLight.SGREEN,
            TrafficLight.SYELLOW,
            TrafficLight.SRED),
        ENUMS_OPS_TRAFFIC_LIGHT.toList());
    var tlbOps = TrafficLightBased.enumOps();
    assertSame(tlbOps, EnumsOps.from(TrafficLightBased.class));
    //noinspection AssertBetweenInconvertibleTypes
    assertNotSame(ENUMS_OPS_TRAFFIC_LIGHT, tlbOps);
    assertEquals(
        List.of(
            TrafficLightBased.GREEN,
            TrafficLightBased.YELLOW,
            TrafficLightBased.RED),
        tlbOps.toList());
  }

  @Test
  public void testToListThroughenumOps() {
    assertEquals(
        List.of(
            TrafficLightBased.GREEN,
            TrafficLightBased.YELLOW,
            TrafficLightBased.RED),
        TrafficLightBased.enumOps().toList());
  }

  @Test
  public void testToListDirectly() {
    assertEquals(
        List.of(
            TrafficLightBased.GREEN,
            TrafficLightBased.YELLOW,
            TrafficLightBased.RED),
        TrafficLightBased.toList());
  }

  @Test
  public void testStreamThroughenumOps() {
    assertEquals(
        List.of(
            TrafficLightBased.GREEN,
            TrafficLightBased.YELLOW,
            TrafficLightBased.RED),
        TrafficLightBased.enumOps().stream().toList());
  }

  @Test
  public void testStreamDirectly() {
    assertEquals(
        List.of(
            TrafficLightBased.GREEN,
            TrafficLightBased.YELLOW,
            TrafficLightBased.RED),
        TrafficLightBased.stream().toList());
  }

  @Test
  public void testToOrderedSetThroughenumOps() {
    assertEquals(
        List.of(
            TrafficLightBased.GREEN,
            TrafficLightBased.YELLOW,
            TrafficLightBased.RED),
        TrafficLightBased.enumOps().toOrderedSet().stream().toList());
  }

  @Test
  public void testToOrderedMapThroughenumOps() {
    var orderedMapExpected =
        MapsOps.toMapOrdered(
            Arrays.stream(TrafficLightBased.values())
                .map(trafficLightBased ->
                    Map.entry(
                        trafficLightBased.toString(),
                        trafficLightBased)));
    assertEquals(
        orderedMapExpected.keySet().stream().toList(),
        TrafficLightBased.enumOps().toOrderedMapByName().keySet().stream().toList());
    assertEquals(
        orderedMapExpected,
        TrafficLightBased.enumOps().toOrderedMapByName());
  }

  @Test
  public void forEachPlus() {
    var counter = new int[]{0};
    TrafficLightBased.enumOps()
        .forEach(trafficLightBased ->
            ++counter[0]);
    assertEquals(3, counter[0]);
    TrafficLightBased.enumOps()
        .forEachOrdered(trafficLightBased ->
            ++counter[0]);
    assertEquals(6, counter[0]);
  }

  //As it is fairly difficult, decided to forgo testing of forEach and forEachOrdered, as both are forward to java.util.Stream's methods

  @SuppressWarnings("deprecation")
  @Test
  public void testValueOfThroughenumOps() {
    assertEquals(TrafficLightBased.YELLOW,
        TrafficLightBased.enumOps().valueOfOrDefaultToFirst("yElLoW"));
    assertEquals(TrafficLightBased.GREEN,
        TrafficLightBased.enumOps().valueOfOrDefaultToFirst("yElLoWx"));
    assertEquals(TrafficLightBased.YELLOW,
        TrafficLightBased.enumOps().valueOf("yElLoW", TrafficLightBased.RED));
    assertEquals(TrafficLightBased.RED,
        TrafficLightBased.enumOps()
            .valueOf("yElLoWx", TrafficLightBased.RED));
    assertEquals(TrafficLightBased.YELLOW,
        TrafficLightBased.enumOps().valueOf("yElLoW").orElse(TrafficLightBased.RED));
    assertEquals(TrafficLightBased.RED,
        TrafficLightBased.enumOps()
            .valueOf("yElLoWx").orElse(TrafficLightBased.RED));
    assertEquals(Optional.of(TrafficLightBased.YELLOW),
        TrafficLightBased.enumOps().valueOf("yElLoW"));
    assertEquals(Optional.empty(),
        TrafficLightBased.enumOps().valueOf("yElLoWx"));
    var voo1 = TrafficLightBased.enumOps().valueOf("yElLoW");
    assertTrue(voo1.isPresent());
    assertEquals(TrafficLightBased.YELLOW, voo1.get());
  }

  @Test
  public void testValueOfDirectly() {
    assertEquals(Optional.of(TrafficLightBased.YELLOW),
        TrafficLightBased.valueOfIgnoreCase("yElLoW"));
    assertEquals(Optional.empty(),
        TrafficLightBased.valueOfIgnoreCase("yElLoWx"));
  }

  @Test
  public void testInstanceValueOf() {
    assertEquals(Optional.of(TrafficLight.SYELLOW),
        ENUMS_OPS_TRAFFIC_LIGHT.valueOf("SyElLoW"));
    assertEquals(Optional.empty(),
        ENUMS_OPS_TRAFFIC_LIGHT.valueOf("SyElLoWx"));
  }

  @SuppressWarnings("NonAsciiCharacters")
  private enum RegionMatch {
    ıGNORED,
    QUİT
  }

  @SuppressWarnings("NonAsciiCharacters")
  @Test
  public void testInstanceValueOfRegionMatches() {
    assertEquals(Optional.of(RegionMatch.QUİT),
        EnumsOps.from(RegionMatch.class).valueOfByRegionMatches("QUİT"));
    assertEquals(Optional.of(RegionMatch.QUİT),
        EnumsOps.from(RegionMatch.class).valueOfByRegionMatches("QUIT"));
    assertEquals(Optional.of(RegionMatch.QUİT),
        EnumsOps.from(RegionMatch.class).valueOfByRegionMatches("quit"));
    assertEquals(Optional.of(RegionMatch.QUİT),
        EnumsOps.from(RegionMatch.class).valueOfByRegionMatches("quıt"));
    assertEquals(Optional.of(RegionMatch.ıGNORED),
        EnumsOps.from(RegionMatch.class).valueOfByRegionMatches("İGNORED"));
    assertEquals(Optional.of(RegionMatch.ıGNORED),
        EnumsOps.from(RegionMatch.class).valueOfByRegionMatches("IGNORED"));
    assertEquals(Optional.of(RegionMatch.ıGNORED),
        EnumsOps.from(RegionMatch.class).valueOfByRegionMatches("ignored"));
    assertEquals(Optional.of(RegionMatch.ıGNORED),
        EnumsOps.from(RegionMatch.class).valueOfByRegionMatches("ıgnored"));
  }

  @Test
  public void testFormatBuilder() {
    var formatBuilderDefaults = TrafficLightBased
        .enumOps().getFormatBuilder();
    assertSame(formatBuilderDefaults, formatBuilderDefaults.setSeparator(formatBuilderDefaults.getSeparator()));
    //the four defaults
    assertEquals(
        "GREEN, YELLOW, RED",
        formatBuilderDefaults
            .join());
    //filtering the enum set - predicate
    assertEquals(
        "GREEN, RED",
        formatBuilderDefaults
            .setFilter(trafficLightBased ->
                trafficLightBased.toString().contains("R"))
            .join());
    //filtering the enum set - collection
    assertEquals(
        "YELLOW, RED",
        formatBuilderDefaults
            .setFilter(List.of(TrafficLightBased.YELLOW, TrafficLightBased.RED))
            .join());
    //filtering the enum set - stream
    assertEquals(
        "GREEN, YELLOW",
        formatBuilderDefaults
            .setFilter(Stream.of(TrafficLightBased.GREEN, TrafficLightBased.YELLOW))
            .join());
    //sorting the enum set on the default (by ordinal) in reverse
    assertEquals(
        "RED, YELLOW, GREEN",
        formatBuilderDefaults
            .setSortStrategy(formatBuilderDefaults.getSortStrategy().reversed())
            .join());
    //reformatting the String
    assertEquals(
        "GREEN(0), YELLOW(1), RED(2)",
        formatBuilderDefaults
            .setReformat(trafficLightBased ->
                "%s(%d)".formatted(
                    trafficLightBased.toString(),
                    trafficLightBased.ordinal()))
            .join());
    //ensure same instance returned when the separator passed is equal to the one already present
    assertSame(formatBuilderDefaults, formatBuilderDefaults.setSeparator(formatBuilderDefaults.getSeparator()));
    //changing the separator
    assertEquals(
        "GREEN,YELLOW,RED",
        formatBuilderDefaults
            .setSeparator(",")
            .join());
    //changing all four simultaneously
    assertEquals(
        "1 -> YELLOW|0 -> GREEN",
        formatBuilderDefaults
            .setFilter(trafficLightBased ->
                trafficLightBased.ordinal() < 2)
            .setSortStrategy(formatBuilderDefaults.getSortStrategy().reversed())
            .setReformat(trafficLightBased ->
                "%d -> %s".formatted(
                    trafficLightBased.ordinal(),
                    trafficLightBased.toString()))
            .setSeparator("|")
            .join());
  }

  @Test
  public void testFormatConfigNullInvariants() {
    java.util.function.Predicate<TrafficLightBased> filter = e -> true;
    java.util.Comparator<TrafficLightBased> sort = java.util.Comparator.comparingInt(Enum::ordinal);
    java.util.function.Function<TrafficLightBased, String> reformat = Enum::name;
    String sep = ", ";

    assertThrows(NullPointerException.class, () -> new EnumsOps.FormatConfig<>(null, sort, reformat, sep));
    assertThrows(NullPointerException.class, () -> new EnumsOps.FormatConfig<>(filter, null, reformat, sep));
    assertThrows(NullPointerException.class, () -> new EnumsOps.FormatConfig<>(filter, sort, null, sep));
    assertThrows(NullPointerException.class, () -> new EnumsOps.FormatConfig<>(filter, sort, reformat, null));

    var config = new EnumsOps.FormatConfig<>(filter, sort, reformat, sep);
    assertThrows(NullPointerException.class, () -> config.withFilter(null));
    assertThrows(NullPointerException.class, () -> config.withSortStrategy(null));
    assertThrows(NullPointerException.class, () -> config.withReformat(null));
    assertThrows(NullPointerException.class, () -> config.withSeparator(null));
  }

  @Test
  public void testFormatConfigWithers() {
    java.util.function.Predicate<TrafficLightBased> filter1 = e -> true;
    java.util.function.Predicate<TrafficLightBased> filter2 = e -> e.name().startsWith("G");
    java.util.Comparator<TrafficLightBased> sort1 = java.util.Comparator.comparingInt(Enum::ordinal);
    java.util.Comparator<TrafficLightBased> sort2 = sort1.reversed();
    java.util.function.Function<TrafficLightBased, String> reformat1 = Enum::name;
    java.util.function.Function<TrafficLightBased, String> reformat2 = TrafficLightBased::toString;
    String sep1 = ", ";
    String sep2 = "|";

    var original = new EnumsOps.FormatConfig<>(filter1, sort1, reformat1, sep1);

    var withFilter = original.withFilter(filter2);
    assertNotEquals(original, withFilter);
    assertEquals(filter2, withFilter.filter());
    assertEquals(filter1, original.filter());

    var withSort = original.withSortStrategy(sort2);
    assertNotEquals(original, withSort);
    assertEquals(sort2, withSort.sortStrategy());
    assertEquals(sort1, original.sortStrategy());

    var withReformat = original.withReformat(reformat2);
    assertNotEquals(original, withReformat);
    assertEquals(reformat2, withReformat.reformat());
    assertEquals(reformat1, original.reformat());

    var withSep = original.withSeparator(sep2);
    assertNotEquals(original, withSep);
    assertEquals(sep2, withSep.separator());
    assertEquals(sep1, original.separator());

    assertSame(original, original.withSeparator(", "));
  }

  @Test
  public void testFormatBuilderNullInvariants() {
    var builder = TrafficLightBased.enumOps().getFormatBuilder();
    assertThrows(NullPointerException.class, () -> builder.setFilter((java.util.function.Predicate<TrafficLightBased>) null));
    assertThrows(NullPointerException.class, () -> builder.setFilter((List<TrafficLightBased>) null));
    assertThrows(NullPointerException.class, () -> builder.setFilter((Stream<TrafficLightBased>) null));
    assertThrows(NullPointerException.class, () -> builder.setSortStrategy(null));
    assertThrows(NullPointerException.class, () -> builder.setReformat(null));
    assertThrows(NullPointerException.class, () -> builder.setSeparator(null));
  }
}