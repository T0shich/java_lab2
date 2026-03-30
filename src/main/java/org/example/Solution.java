package org.example;

/**
 * Вычисляет арифметические выражения в строке.
 */
public class Solution {
    private static final String INVALID_EXPRESSION = "Ошибка: некорректное выражение";
    private static final String DIVISION_BY_ZERO = "Ошибка: деление на ноль";

    /**
     * Считает выражение и возвращает результат или сообщение об ошибке.
     *
     * @param expression строка с выражением
     * @return результат вычисления или текст ошибки
     */
    public static String calculate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return INVALID_EXPRESSION;
        }

        try {
            Parser parser = new Parser(expression);
            double result = parser.parseExpression();
            parser.skipSpaces();

            if (!parser.isEnd()) {
                return INVALID_EXPRESSION;
            }

            return formatResult(result);
        } catch (ArithmeticException e) {
            return DIVISION_BY_ZERO;
        } catch (RuntimeException e) {
            return INVALID_EXPRESSION;
        }
    }
    /**
     * Форматирует число в строку.
     *
     * @param value вычисленное значение
     * @return строка с числом или сообщение об ошибке
     */
    private static String formatResult(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            return INVALID_EXPRESSION;
        }

        long asLong = (long) value;
        if (Math.abs(value - asLong) < 1e-10) {
            return String.valueOf(asLong);
        }
        return String.valueOf(value);
    }

    /**
     * Внутренний парсер выражения.
     */
    private static final class Parser {
        private final String input;
        private int position;

        /**
         * Создает парсер для входной строки.
         */
        private Parser(String input) {
            this.input = input;
            this.position = 0;
        }

        /**
         * Разбирает сумму и разность.
         *
         * @return вычисленное значение
         */
        private double parseExpression() {
            double value = parseTerm();

            while (true) {
                skipSpaces();
                if (match('+')) {
                    value += parseTerm();
                } else if (match('-')) {
                    value -= parseTerm();
                } else {
                    return value;
                }
            }
        }

        /**
         * Разбирает умножение и деление.
         *
         * @return вычисленное значение
         */
        private double parseTerm() {
            double value = parseFactor();

            while (true) {
                skipSpaces();
                if (match('*')) {
                    value *= parseFactor();
                } else if (match('/')) {
                    double divisor = parseFactor();
                    if (Math.abs(divisor) < 1e-12) {
                        throw new ArithmeticException(DIVISION_BY_ZERO);
                    }
                    value /= divisor;
                } else {
                    return value;
                }
            }
        }

        /**
         * Разбирает число, унарный знак или выражение в скобках.
         *
         * @return вычисленное значение
         */
        private double parseFactor() {
            skipSpaces();

            if (match('+')) {
                return parseFactor();
            }
            if (match('-')) {
                return -parseFactor();
            }

            if (match('(')) {
                double value = parseExpression();
                skipSpaces();
                if (!match(')')) {
                    throw new IllegalArgumentException(INVALID_EXPRESSION);
                }
                return value;
            }

            return parseNumber();
        }

        /**
         * Считывает число из строки.
         *
         * @return найденное число
         */
        private double parseNumber() {
            skipSpaces();
            int start = position;
            boolean hasDigit = false;
            boolean hasDot = false;

            while (!isEnd()) {
                char current = input.charAt(position);
                if (Character.isDigit(current)) {
                    hasDigit = true;
                    position++;
                } else if (current == '.' && !hasDot) {
                    hasDot = true;
                    position++;
                } else {
                    break;
                }
            }

            if (!hasDigit) {
                throw new IllegalArgumentException(INVALID_EXPRESSION);
            }

            return Double.parseDouble(input.substring(start, position));
        }

        /**
         * Проверяет текущий символ и сдвигает позицию при совпадении.
         */
        private boolean match(char expected) {
            if (isEnd() || input.charAt(position) != expected) {
                return false;
            }
            position++;
            return true;
        }

        /**
         * Пропускает пробелы.
         */
        private void skipSpaces() {
            while (!isEnd() && Character.isWhitespace(input.charAt(position))) {
                position++;
            }
        }

        /**
         * Проверяет, что строка закончилась.
         */
        private boolean isEnd() {
            return position >= input.length();
        }
    }
}
