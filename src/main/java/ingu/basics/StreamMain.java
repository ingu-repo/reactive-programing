package ingu.basics;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamMain {
    public static List<String> list = Arrays.asList("Canada", "US", "Argentina", "Mexico", "Panama");
    public static String findChar = "a";
    public static List<Integer> nums = Arrays.asList(3,1,9,4,5);

    public static void main(String[] args) {
        testBasics();
    }
    public static void testBasics() {
        /** Traditional ways
         */
        for (int i=0; i<list.size(); i++) {
            if (list.get(i).contains(findChar)) {
                System.out.println(i + ":" + list.get(i));
                break;
            }
        }
        /** Using stream
         * List -> Stream -> Stream methods such as filter, map, sorted etc
         */
        list.stream()                               // get stream
            .filter(v -> v.contains(findChar))   // return stream with conditions
            .map(v -> v.toUpperCase())              // convert to Upper
            .sorted()                               // sort
            .limit(2)                       // top 2
            .forEach(System.out::println);          // final process

        /** Doing same thing with one by one
         */
        System.out.println("");
        Stream<String> stream = list.stream();
        Stream<String> filtered = stream.filter(v -> v.contains(findChar));
        filtered.forEach(v -> System.out.println(v));

        /** Sample for sorted
         */
        System.out.println("org:" + nums);
        System.out.print("sorted: ");
        nums.stream()
            .sorted((a, b) -> Integer.compare(a, b))
            .forEach(v -> System.out.print(v + ", "));
    }
}
