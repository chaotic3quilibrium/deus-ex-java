package org.deus_ex_java.util.tuple;

import org.deus_ex_java.util.stream.StreamsOps;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TupleAllTests {
  @Test
  public void testFullSurfaceArea() {
    var t10a = new Tuple10<>(
        true,
        Byte.valueOf("2"),
        Short.valueOf("4"),
        8,
        16L,
        BigInteger.valueOf(32L),
        3.14f,
        29.2d,
        'c',
        "a");
    assertEquals(10, t10a.arity());
    assertEquals(true, t10a._1());
    assertEquals(Byte.valueOf("2"), t10a._2());
    assertEquals(Short.valueOf("4"), t10a._3());
    assertEquals(8, t10a._4());
    assertEquals(16L, t10a._5());
    assertEquals(BigInteger.valueOf(32L), t10a._6());
    assertEquals(3.14f, t10a._7());
    assertEquals(29.2d, t10a._8());
    assertEquals('c', t10a._9());
    assertEquals("a", t10a._10());
    var t10b = new Tuple10<>(
        true,
        Byte.valueOf("2"),
        Short.valueOf("4"),
        8,
        16L,
        BigInteger.valueOf(32L),
        3.14f,
        29.2d,
        'c',
        "a");
    assertEquals(t10a, t10b);
    var t9 = new Tuple9<>(
        t10a._2(),
        t10a._3(),
        t10a._4(),
        t10a._5(),
        t10a._6(),
        t10a._7(),
        t10a._8(),
        t10a._9(),
        t10a._10());
    assertEquals(9, t9.arity());
    StreamsOps.zip(t10a.stream(), Stream.concat(Stream.of(true), t9.stream()))
        .forEach(entry ->
            assertEquals(
                entry.getKey(),
                entry.getValue()));
    var t8 = new Tuple8<>(
        t10a._3(),
        t10a._4(),
        t10a._5(),
        t10a._6(),
        t10a._7(),
        t10a._8(),
        t10a._9(),
        t10a._10());
    assertEquals(8, t8.arity());
    StreamsOps.zip(t9.stream(), Stream.concat(Stream.of(Byte.valueOf("2")), t8.stream()))
        .forEach(entry ->
            assertEquals(
                entry.getKey(),
                entry.getValue()));
    var t7 = new Tuple7<>(
        t10a._4(),
        t10a._5(),
        t10a._6(),
        t10a._7(),
        t10a._8(),
        t10a._9(),
        t10a._10());
    assertEquals(7, t7.arity());
    StreamsOps.zip(t8.stream(), Stream.concat(Stream.of(Short.valueOf("4")), t7.stream()))
        .forEach(entry ->
            assertEquals(
                entry.getKey(),
                entry.getValue()));
    var t6 = new Tuple6<>(
        t10a._5(),
        t10a._6(),
        t10a._7(),
        t10a._8(),
        t10a._9(),
        t10a._10());
    assertEquals(6, t6.arity());
    StreamsOps.zip(t7.stream(), Stream.concat(Stream.of(8), t6.stream()))
        .forEach(entry ->
            assertEquals(
                entry.getKey(),
                entry.getValue()));
    var t5 = new Tuple5<>(
        t10a._6(),
        t10a._7(),
        t10a._8(),
        t10a._9(),
        t10a._10());
    assertEquals(5, t5.arity());
    StreamsOps.zip(t6.stream(), Stream.concat(Stream.of(16L), t5.stream()))
        .forEach(entry ->
            assertEquals(
                entry.getKey(),
                entry.getValue()));
    var t4 = new Tuple4<>(
        t10a._7(),
        t10a._8(),
        t10a._9(),
        t10a._10());
    assertEquals(4, t4.arity());
    StreamsOps.zip(t5.stream(), Stream.concat(Stream.of(BigInteger.valueOf(32L)), t4.stream()))
        .forEach(entry ->
            assertEquals(
                entry.getKey(),
                entry.getValue()));
    var t3 = new Tuple3<>(
        t10a._8(),
        t10a._9(),
        t10a._10());
    assertEquals(3, t3.arity());
    StreamsOps.zip(t4.stream(), Stream.concat(Stream.of(3.14f), t3.stream()))
        .forEach(entry ->
            assertEquals(
                entry.getKey(),
                entry.getValue()));
    var t2 = new Tuple2<>(
        t10a._9(),
        t10a._10());
    assertEquals(2, t2.arity());
    StreamsOps.zip(t3.stream(), Stream.concat(Stream.of(29.2d), t2.stream()))
        .forEach(entry ->
            assertEquals(
                entry.getKey(),
                entry.getValue()));
    var t1 = new Tuple1<>(
        t10a._10());
    assertEquals(1, t1.arity());
    StreamsOps.zip(t2.stream(), Stream.concat(Stream.of('c'), t1.stream()))
        .forEach(entry ->
            assertEquals(
                entry.getKey(),
                entry.getValue()));
    assertEquals(Tuple.MIN_ARITY, t1.arity());
    assertEquals(Tuple.MAX_ARITY, t10a.arity());
  }
}
