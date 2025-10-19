package org.deus_ex_java.util;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnumAndIdsOpsTests {
  public interface EquivalentInt {
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
    var tlwixoo = EnumAndIdsOps.from(TrafficLightWithIdA.class);
    assertEquals("SGREEN(0), SYELLOW(1), SRED(2)", tlwixoo.join());
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
    var tlwixoo = EnumAndIdsOps.from(TrafficLightWithIdB.class, 1);
    assertEquals("SGREEN(1), SYELLOW(2), SRED(3)", tlwixoo.join());
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
  }

  @Test
  public void testTrafficLightWithIdX2() {
    var tlwix = EnumAndIdsOps.from(
        TrafficLightWithIdC.class,
        TrafficLightWithIdC::getEquivalent);
    assertEquals("SGREEN(1), SYELLOW(2), SRED(5)", tlwix.join());
    assertEquals(Optional.of(TrafficLightWithIdC.SGREEN), tlwix.get(1));
    assertEquals(Optional.of(entry(TrafficLightWithIdC.SGREEN, 1)), tlwix.valueOf("1"));
    assertEquals(Optional.of(entry(TrafficLightWithIdC.SGREEN, 1)), tlwix.valueOf("sgreen"));
    assertEquals(Optional.of(TrafficLightWithIdC.SYELLOW), tlwix.get(2));
    assertEquals(Optional.of(entry(TrafficLightWithIdC.SYELLOW, 2)), tlwix.valueOf("2"));
    assertEquals(Optional.of(entry(TrafficLightWithIdC.SYELLOW, 2)), tlwix.valueOf("sYeLlOw"));
    assertEquals(Optional.of(TrafficLightWithIdC.SRED), tlwix.get(5));
    assertEquals(Optional.of(entry(TrafficLightWithIdC.SRED, 5)), tlwix.valueOf("5"));
    assertEquals(Optional.of(entry(TrafficLightWithIdC.SRED, 5)), tlwix.valueOf("SREd"));
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
    var tlwix = EnumAndIdsOps.from(
        TrafficLightWithIdD.class,
        TrafficLightWithIdD::getEquivalent,
        id -> "C" + id.toString() + "!");
    assertEquals("SGREEN(1), SYELLOW(2), SRED(5)", tlwix.join());
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
    var tlwix = EnumAndIdsOps.from(
        TrafficLightWithIdE.class,
        TrafficLightWithIdE::getEquivalent,
        Object::toString,
        enumValue ->
            "X%d".formatted(enumValue.name().length()));
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
    assertEquals("SGREEN(1), SYELLOW(2), SRED(5)", tlwix.join());
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
    assertEquals(entry(TrafficLightWithIdE.SGREEN, 1), tlwix.valueOf("1", entry(TrafficLightWithIdE.SYELLOW, 2)));
    assertEquals(entry(TrafficLightWithIdE.SYELLOW, 2), tlwix.valueOf("1a", entry(TrafficLightWithIdE.SYELLOW, 2)));
    assertEquals(entry(TrafficLightWithIdE.SYELLOW, 2), tlwix.valueOf("2", entry(TrafficLightWithIdE.SYELLOW, 2)));
    assertEquals(entry(TrafficLightWithIdE.SYELLOW, 2), tlwix.valueOf("2a", entry(TrafficLightWithIdE.SYELLOW, 2)));
    assertEquals(entry(TrafficLightWithIdE.SRED, 5), tlwix.valueOf("5", entry(TrafficLightWithIdE.SYELLOW, 2)));
    assertEquals(entry(TrafficLightWithIdE.SYELLOW, 2), tlwix.valueOf("5a", entry(TrafficLightWithIdE.SYELLOW, 2)));
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
                Object::toString,
                enumValue ->
                    "X%d".formatted(enumValue.getEquivalent())));
    assertEquals(
        "EnumsIdOps invalid parameter(s) - Parameter Validation Failures: [invalid state for enum [TrafficLightWithIdF] where the .toLowerCase() of enumValue.toString(), fIdToString.apply(fEToId.apply(enumValue)), and optionalFEToAltString.map(fEToAltString -> fEToAltString.apply(enumValue)).get() is not unique across all the enums values - erred values: " +
            "keyLowerCase: 3 -> enumValueName: SYELLOW -> collisionSource: ID_VALUE, " +
            "keyLowerCase: x3 -> enumValueName: SYELLOW -> collisionSource: ALT_STRING_VALUE, " +
            "keyLowerCase: 3 -> enumValueName: SRED -> collisionSource: ID_VALUE, " +
            "keyLowerCase: x3 -> enumValueName: SRED -> collisionSource: ALT_STRING_VALUE]",
        parametersValidationException.getMessage());
    assertEquals(1, parametersValidationException.getParametersValidationFailureMessages().size());
  }

  public interface EquivalentString {
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
    var tlwix = EnumAndIdsOps.from(
        TrafficLightWithIdG.class,
        TrafficLightWithIdG::getEquivalent,
        Object::toString,
        enumValue ->
            enumValue.name().toLowerCase());
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

  @Test
  public void testRemainingUnimplemented() {
    //throw new MissingImplementationException("missing x7 join() and x8 joinOnIds() tests");
  }
}