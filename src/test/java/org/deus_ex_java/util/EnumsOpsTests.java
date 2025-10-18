package org.deus_ex_java.util;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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

    public static EnumsOps<TrafficLightBased> ops() {
      return ENUM_OPS;
    }

    public static List<TrafficLightBased> toList() {
      return ENUM_OPS.toList();
    }

    public static Stream<TrafficLightBased> stream() {
      return ENUM_OPS.stream();
    }

    public static Optional<TrafficLightBased> valueOfIgnoreCase(String search) {
      return ENUM_OPS.valueOf(search);
    }

    public static String join() {
      return ENUM_OPS.join();
    }

    public static String join(Stream<TrafficLightBased> es) {
      return ENUM_OPS.join(es);
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
        ENUMS_OPS_TRAFFIC_LIGHT.join());
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
    var tlbOps = TrafficLightBased.ops();
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
  public void testToListThroughOps() {
    assertEquals(
        List.of(
            TrafficLightBased.GREEN,
            TrafficLightBased.YELLOW,
            TrafficLightBased.RED),
        TrafficLightBased.ops().toList());
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
  public void testStreamThroughOps() {
    assertEquals(
        List.of(
            TrafficLightBased.GREEN,
            TrafficLightBased.YELLOW,
            TrafficLightBased.RED),
        TrafficLightBased.ops().stream().toList());
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
  public void testToOrderedSetThroughOps() {
    assertEquals(
        List.of(
            TrafficLightBased.GREEN,
            TrafficLightBased.YELLOW,
            TrafficLightBased.RED),
        TrafficLightBased.ops().toOrderedSet().stream().toList());
  }

  @Test
  public void testToOrderedMapThroughOps() {
    var orderedMapExpected =
        MapsOps.toMapOrderedUnmodifiable(
            Arrays.stream(TrafficLightBased.values())
                .map(trafficLightBased ->
                    Map.entry(
                        trafficLightBased.toString(),
                        trafficLightBased)));
    assertEquals(
        orderedMapExpected.keySet().stream().toList(),
        TrafficLightBased.ops().toOrderedMapByName().keySet().stream().toList());
    assertEquals(
        orderedMapExpected,
        TrafficLightBased.ops().toOrderedMapByName());
  }

  @Test
  public void forEachPlus() {
    var counter = new int[]{0};
    TrafficLightBased.ops()
        .forEach(trafficLightBased ->
            ++counter[0]);
    assertEquals(3, counter[0]);
    TrafficLightBased.ops()
        .forEachOrdered(trafficLightBased ->
            ++counter[0]);
    assertEquals(6, counter[0]);
  }

  //As it is fairly difficult, decided to forgo testing of forEach and forEachOrdered, as both are forward to java.util.Stream's methods

  @Test
  public void testValueOfThroughOps() {
    assertEquals(TrafficLightBased.YELLOW,
        TrafficLightBased.ops().valueOfOrDefaultToFirst("yElLoW"));
    assertEquals(TrafficLightBased.GREEN,
        TrafficLightBased.ops().valueOfOrDefaultToFirst("yElLoWx"));
    assertEquals(TrafficLightBased.YELLOW,
        TrafficLightBased.ops().valueOf("yElLoW", TrafficLightBased.RED));
    assertEquals(TrafficLightBased.RED,
        TrafficLightBased.ops()
            .valueOf("yElLoWx", TrafficLightBased.RED));
    assertEquals(Optional.of(TrafficLightBased.YELLOW),
        TrafficLightBased.ops().valueOf("yElLoW"));
    assertEquals(Optional.empty(),
        TrafficLightBased.ops().valueOf("yElLoWx"));
    var voo1 = TrafficLightBased.ops().valueOf("yElLoW");
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

  @Test
  public void testJoinThroughOps() {
    assertEquals(
        "GREEN, YELLOW, RED",
        TrafficLightBased
            .ops()
            .join());
    assertEquals(
        "GREEN,YELLOW,RED",
        TrafficLightBased.ops().join(","));
    assertEquals(
        "GREEN(0), YELLOW(1), RED(2)",
        TrafficLightBased
            .ops()
            .join(
                trafficLightBased -> "%s(%d)".formatted(
                    trafficLightBased.toString(),
                    trafficLightBased.ordinal())));
    assertEquals(
        "GREEN(0),YELLOW(1),RED(2)",
        TrafficLightBased
            .ops()
            .join(
                trafficLightBased -> "%s(%d)".formatted(
                    trafficLightBased.toString(),
                    trafficLightBased.ordinal()),
                ","));
    assertEquals(
        "GREEN, RED",
        TrafficLightBased
            .ops()
            .join(
                TrafficLightBased
                    .ops()
                    .stream()
                    .filter(trafficLightBased ->
                        trafficLightBased.toString().contains("R"))));
    assertEquals(
        "GREEN(0), RED(2)",
        TrafficLightBased
            .ops()
            .join(
                TrafficLightBased
                    .ops()
                    .stream()
                    .filter(trafficLightBased ->
                        trafficLightBased.toString().contains("R")),
                trafficLightBased -> "%s(%d)".formatted(
                    trafficLightBased.toString(),
                    trafficLightBased.ordinal())));
    assertEquals(
        "GREEN,RED",
        TrafficLightBased
            .ops()
            .join(
                TrafficLightBased
                    .ops()
                    .stream()
                    .filter(trafficLightBased ->
                        trafficLightBased.toString().contains("R")),
                ","));
    assertEquals(
        "GREEN(0),RED(2)",
        TrafficLightBased
            .ops()
            .join(
                TrafficLightBased
                    .ops()
                    .stream()
                    .filter(trafficLightBased ->
                        trafficLightBased.toString().contains("R")),
                trafficLightBased -> "%s(%d)".formatted(
                    trafficLightBased.toString(),
                    trafficLightBased.ordinal()),
                ","));
  }

  @Test
  public void testJoinDirectly() {
    assertEquals(
        "GREEN, YELLOW, RED",
        TrafficLightBased
            .join());
    assertEquals(
        "GREEN, RED",
        TrafficLightBased
            .join(
                TrafficLightBased
                    .ops()
                    .stream()
                    .filter(trafficLightBased ->
                        trafficLightBased.toString().contains("R"))));
  }
}