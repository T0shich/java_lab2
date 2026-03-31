package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSolution {
    private static final String INVALID = "Ошибка: некорректное выражение";
    private static final String DIVISION_BY_ZERO = "Ошибка: деление на ноль";

    @Test
    void simpleExpression() {
        assertEquals("11", Solution.calculate("2 + 3 * (4 - 1)"));
    }

    @Test
    void unaryMinusAndBrackets() {
        assertEquals("-5", Solution.calculate("-(2+3)"));
    }

    @Test
    void decimalNumbers() {
        assertEquals("0.75", Solution.calculate(".5 + .25"));
    }

    @Test
    void nullInputIsInvalid() {
        assertEquals(INVALID, Solution.calculate(null));
    }

    @Test
    void emptyInputIsInvalid() {
        assertEquals(INVALID, Solution.calculate(""));
    }

    @Test
    void invalidExpression() {
        assertEquals(INVALID, Solution.calculate("2 +"));
    }

    @Test
    void invalidCharacters() {
        assertEquals(INVALID, Solution.calculate("2a+3"));
    }

    @Test
    void divisionByZero() {
        assertEquals(DIVISION_BY_ZERO, Solution.calculate("5/0"));
    }
}
