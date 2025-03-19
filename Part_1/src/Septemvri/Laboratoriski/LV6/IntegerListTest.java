package Septemvri.Laboratoriski.LV6;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class IntegerList {

    private List<Integer> integers;

    public IntegerList() {
        integers = new ArrayList<>();
    }

    public IntegerList(Integer... numbers) {
        integers = new ArrayList<>();
        Collections.addAll(this.integers, numbers);
    }

    private void throwException(int idx) {
        if (idx < 0 || idx >= integers.size()) {
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");
        }
    }

    public void add(int el, int idx) {
        if (idx >= integers.size()) {
            for (int i = integers.size(); i < idx; i++) {
                integers.add(0);
            }
            integers.add(el);
        } else {
            integers.add(idx, el);
        }
    }

    public int remove(int idx) {
        throwException(idx);
        return integers.remove(idx);
    }

    public void set(int el, int idx) {
        throwException(idx);
        integers.set(idx, el);
    }

    public int get(int idx) {
        throwException(idx);
        return integers.get(idx);
    }

    public int size() {
        return integers.size();
    }

    public int count(int el) {
        return (int) integers.stream().filter(i -> i == el).count();
    }

    public void removeDuplicates() {
        for (int i = integers.size() - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (Objects.equals(integers.get(i), integers.get(j))) {
                    integers.remove(j);
                    i--;
                }
            }
        }
    }

    public int sumFirst(int k) {
        return integers.stream().limit(k).mapToInt(i -> i).sum();
    }

    public int sumLast(int k) {
        return integers.stream().skip(integers.size() - k).mapToInt(i -> i).sum();
    }

    public void shiftRight(int idx, int k) {
        throwException(idx);
        int value = integers.remove(idx);
        integers.add((idx + k) % (integers.size() + 1), value);
    }

    public void shiftLeft(int idx, int k) {
        throwException(idx);
        int newIndex = (idx - k % integers.size() + integers.size()) % integers.size();
        int value = integers.remove(idx);
        integers.add(newIndex, value);
    }

    public IntegerList addValue(int value) {
        IntegerList integerList = new IntegerList();
        for (int i = 0; i < integers.size(); i++) {
            integerList.add(integers.get(i) + value, i);
        }
        return integerList;
    }

}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}