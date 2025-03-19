package IspitSeptemvri.Kolokviumski.K13;

import java.util.*;
import java.util.stream.Collectors;

class WordVectors {

    String[] words;
    List<List<Integer>> vectors;
    Map<String, List<Integer>> wordVectorPairs;
    List<String> readWords;

    public WordVectors(String[] words, List<List<Integer>> vectors) {
        this.words = words;
        this.vectors = vectors;
        this.wordVectorPairs = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            wordVectorPairs.put(words[i], vectors.get(i));
        }
        this.readWords = new ArrayList<>();
    }

    public void readWords(List<String> words) {
        for (String word : words) {
            this.readWords.add(word);
        }
    }

    public List<Integer> slidingWindow(int n) {
        List<Integer> result = new ArrayList<>();

//        for (int i = 0; i <= readWords.size() - n; i++) {
//            List<Integer> windowSum = Arrays.asList(0, 0, 0, 0, 0);
//
//            for (int j = 0; j < n; j++) {
//                String word = readWords.get(i + j);
//                List<Integer> vector = wordVectorPairs.getOrDefault(word, Arrays.asList(5, 5, 5, 5, 5));
//
//                for (int k = 0; k < 5; k++) {
//                    windowSum.set(k, windowSum.get(k) + vector.get(k));
//                }
//            }
//
//            int maxInWindow = windowSum.stream().max(Integer::compare).orElse(0);
//            result.add(maxInWindow);
//        }
        for (int i = 0; i <= readWords.size() - n; i++) {
            List<Integer> window = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                window.add(0);
            }

            for (int j = 0; j < n; j++) {
                List<Integer> vector = wordVectorPairs.getOrDefault(readWords.get(i + j), Arrays.asList(5, 5, 5, 5, 5));
                window.set(0, window.get(0) + vector.get(0));
                window.set(1, window.get(1) + vector.get(1));
                window.set(2, window.get(2) + vector.get(2));
                window.set(3, window.get(3) + vector.get(3));
                window.set(4, window.get(4) + vector.get(4));
            }

            result.add(window.stream().max(Integer::compare).get());
        }

        return result;
    }

}

public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}