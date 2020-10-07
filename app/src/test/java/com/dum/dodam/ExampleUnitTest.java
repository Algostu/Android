package com.dum.dodam;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * <p>
 * assertArrayEquals(a, b);
 * - 배열 A와 B가 일치함을 확인한다.
 * <p>
 * assertEquals(a, b);
 * - 객체 A와 B가 일치함을 확인한다.
 * <p>
 * assertEquals(a, b, c);
 * - 객체 A와 B가 일치함을 확인한다.
 * - a: 예상값, b:결과값, c: 오차범위
 * <p>
 * assertSame(a, b);
 * - 객체 A와 B가 같은 객임을 확인한다.
 * <p>
 * assertTrue(a);
 * - 조건 A가 참인가를 확인한다.
 * <p>
 * assertNotNull(a);
 * - 갹채 A가 null이 아님을 확인한다.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}