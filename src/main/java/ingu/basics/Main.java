package ingu.basics;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        testLambda();
    }
    /**
     * Lambda expressions: List -> Stream -> use Lambda expressions
     */
    public static void testLambda() {
        List asList = Arrays.asList("Tokyo", "Chiba", "Kumamoto");
        List listOf = List.of("Kyoto", "Fukuoka", "Sendai");
        asList.stream()
                .map(v -> "Hello " + v)
                .forEach(v -> System.out.println(v)
                );
        listOf.stream()
                .map(v -> "Hello " + v)
                .filter(v -> v.toString().endsWith("o") )
                .forEach(v -> System.out.println(v)
                );
    }
}
