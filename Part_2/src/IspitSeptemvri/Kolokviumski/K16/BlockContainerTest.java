package IspitSeptemvri.Kolokviumski.K16;

import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<T extends Comparable<T>> {

    List<Set<T>> blocks;
    int maxElementsPerBlock;

    public BlockContainer(int maxElementsPerBlock) {
        this.maxElementsPerBlock = maxElementsPerBlock;
        this.blocks = new ArrayList<>();
        this.blocks.add(new TreeSet<>());
    }

    public void add(T a) {
        if (blocks.get(blocks.size() - 1).size() == maxElementsPerBlock) {
            blocks.add(new TreeSet<>());
        }
        blocks.get(blocks.size() - 1).add(a);
    }

    public boolean remove(T a) {
        boolean removed = blocks.get(blocks.size() - 1).remove(a);
        if (blocks.get(blocks.size() - 1).isEmpty()) {
            blocks.remove(blocks.size() - 1);
        }
        return removed;
    }

    public void sort() {
        List<T> sortedElements = blocks.stream().flatMap(Collection::stream)
                .sorted()
                .collect(Collectors.toList());

        blocks.clear();
        this.blocks.add(new TreeSet<>());
        for (T sortedElement : sortedElements) {
            if (blocks.get(blocks.size() - 1).size() == maxElementsPerBlock) {
                blocks.add(new TreeSet<>());
            }
            blocks.get(blocks.size() - 1).add(sortedElement);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Set<T> block : blocks) {
            stringBuilder.append("[");
            for (T t : block) {
                stringBuilder.append(t).append(", ");
            }
            stringBuilder.replace(stringBuilder.lastIndexOf(","), stringBuilder.lastIndexOf(" ") + 1, "");
            stringBuilder.append("],");
        }

        stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
        return stringBuilder.toString();
    }

}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for (int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for (int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}