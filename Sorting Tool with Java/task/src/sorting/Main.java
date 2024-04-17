package sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static Scanner scanner;
    static String dataType = "word";
    static String sortingType = "natural";
    static String inputFile;
    static String outputFile;
    static boolean readFromFile = false, writeToFile = false;

    public static void main(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-dataType" -> {
                    if (i == args.length - 1 || !args[i + 1].equals("long") && !args[i + 1].equals("word") && !args[i + 1].equals("line")) {
                        System.out.println("No data type defined!");
                        System.exit(1);
                    }
                    dataType = args[++i];
                }
                case "-sortingType" -> {
                    if (i == args.length - 1 || !args[i + 1].equals("natural") && !args[i + 1].equals("byCount")) {
                        System.out.println("No sorting type defined!");
                        System.exit(1);
                    }
                    sortingType = args[++i];
                }
                case "-inputFile" -> {
                    inputFile = args[++i];
                    readFromFile = true;
                }
                case "-outputFile" -> {
                    outputFile = args[++i];
                    writeToFile = true;
                }
                default -> System.out.println("\"" + args[i] + "\"" + " is not a valid parameter. It will be skipped.");
            }
        }

        if ("long".equals(dataType)) {
            processLong(readFromFile);
        } else if ("word".equals(dataType) || "line".equals(dataType)) {
            processString(readFromFile, dataType);
        }
    }

    private static void processLong(boolean readFromFile) {
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<String> discarded = new ArrayList<>();
        LinkedHashMap<Long, Integer> sortedMap = new LinkedHashMap<>();

        if (readFromFile) {
            try {
                scanner = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                e.getMessage();
            } finally {
                scanner.close();
            }
        } else {
            scanner = new Scanner(System.in);
        }

        while (scanner.hasNext()) {
            if (scanner.hasNextLong()) {
                long number = scanner.nextLong();
                sortedMap.put(number, sortedMap.getOrDefault(number, 0) + 1);
                list.add((int) number);
            } else {
                discarded.add(scanner.next());
            }
        }
        discarded.forEach(v -> System.out.println("\"" + v + "\"" + " is not a long. It will be skipped."));
        displayInfoLongs(sortedMap, list, sortingType, writeToFile);
    }

    private static void displayInfoLongs(LinkedHashMap<Long, Integer> sortedMap, List<Integer> list, String sortingType, boolean writeToFile) {
        sortedMap = sortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new
                ));
        list.sort(Comparator.naturalOrder());
        double size = sortedMap.values().stream().mapToInt(Integer::intValue).sum();

        if (writeToFile) {
            try {
                PrintWriter writer = new PrintWriter(outputFile);
                if ("natural".equals(sortingType)) {
                    writer.println("Total numbers " + list.size());
                    writer.print("Sorted data: ");
                    list.forEach(v -> writer.print(v + " "));
                } else if ("byCount".equals(sortingType)) {
                    writer.println("Total numbers " + list.size() + ".");
                    sortedMap.forEach((k, v) -> writer.println(k + ": " + v + " time(s), " + (int) Math.round(v * 100 / size) + "%"));
                }
                writer.close();
            } catch (IOException e) {
                e.getMessage();
            }
        } else {
            if ("natural".equals(sortingType)) {
                System.out.println("Total numbers " + list.size());
                System.out.print("Sorted data: ");
                list.forEach(v -> System.out.print(v + " "));
            } else if ("byCount".equals(sortingType)) {
                System.out.println("Total numbers " + list.size() + ".");
                sortedMap.forEach((k, v) -> System.out.println(k + ": " + v + " time(s), " + (int) Math.round(v * 100 / size) + "%"));
            }
        }
    }

    private static void processString(boolean readFromFile, String dataType) {
        List<String> wordsList = new ArrayList<>();
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        if (readFromFile) {
            try {
                scanner = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                e.getMessage();
            }
        } else {
            scanner = new Scanner(System.in);
        }

        while ("word".equals(dataType) ? scanner.hasNext() : scanner.hasNextLine()) {
            String word = "word".equals(dataType) ? scanner.next() : scanner.nextLine();
            wordsList.add(word);
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        scanner.close();
        displayInfoStrings(wordsList, map, writeToFile);
    }

    private static void displayInfoStrings(List<String> wordsList, LinkedHashMap<String, Integer> map, boolean writeToFile) {
        map = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new
                ));
        wordsList.sort(String::compareTo);

        if (writeToFile) {
            try {
                PrintWriter writer = new PrintWriter(outputFile);
                if ("natural".equals(sortingType)) {
                    wordsList.sort("word".equals(dataType) ? String::compareTo : Comparator.comparingInt(String::length).reversed());
                    writer.print("Sorted data: ");
                    wordsList.forEach(v -> System.out.println(v + " "));
                } else {
                    writer.println("Total " + ("word".equals(dataType) ? "words: " : "lines: ") + wordsList.size());
                    double size = map.values().stream().mapToInt(Integer::intValue).sum();
                    map.forEach((k, v) -> writer.println(k + ": " + v + " time(s), "
                            + (int) Math.round(v * 100 / size) + "%"));
                }
                writer.close();
            } catch (IOException e) {
                e.getMessage();
            }
        } else {
            if ("natural".equals(sortingType)) {
                wordsList.sort("word".equals(dataType) ? String::compareTo : Comparator.comparingInt(String::length).reversed());
                System.out.print("Sorted data: ");
                wordsList.forEach(v -> System.out.println(v + " "));
            } else {
                System.out.println("Total " + ("word".equals(dataType) ? "words: " : "lines: ") + wordsList.size());
                double size = map.values().stream().mapToInt(Integer::intValue).sum();
                map.forEach((k, v) -> System.out.println(k + ": " + v + " time(s), "
                        + (int) Math.round(v * 100 / size) + "%"));
            }
        }
    }
}
