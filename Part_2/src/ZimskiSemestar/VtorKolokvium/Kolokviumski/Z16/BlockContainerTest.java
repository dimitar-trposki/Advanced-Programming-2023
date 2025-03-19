package ZimskiSemestar.VtorKolokvium.Kolokviumski.Z16;

import java.util.*;

class BlockContainer<T extends Comparable<T>> {
    private final int n;
    private final List<TreeSet<T>> blocks;

    public BlockContainer(int n) {
        this.n = n;
        this.blocks = new ArrayList<>();
    }

    public void add(T a) {
        if (blocks.isEmpty() || blocks.get(blocks.size() - 1).size() == n) {
            blocks.add(new TreeSet<>());
        }
        blocks.get(blocks.size() - 1).add(a);
    }

    public boolean remove(T a) {
        if (blocks.isEmpty()) {
            return false;
        }
        TreeSet<T> lastBlock = blocks.get(blocks.size() - 1);
        boolean removed = lastBlock.remove(a);
        if (lastBlock.isEmpty()) {
            blocks.remove(blocks.size() - 1);
        }
        return removed;
    }

    public void sort() {
        List<T> allElements = new ArrayList<>();
        for (TreeSet<T> block : blocks) {
            allElements.addAll(block);
        }
        Collections.sort(allElements);

        blocks.clear();
        for (int i = 0; i < allElements.size(); i++) {
            if (i % n == 0) {
                blocks.add(new TreeSet<>());
            }
            blocks.get(blocks.size() - 1).add(allElements.get(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        TreeSet<T> lastBlock = blocks.get(blocks.size() - 1);
        for (TreeSet<T> block : blocks) {
            if (block.equals(lastBlock)) {
                sb.append(block.toString());
            } else {
                sb.append(block.toString()).append(",");
            }
        }
        return sb.toString();
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
