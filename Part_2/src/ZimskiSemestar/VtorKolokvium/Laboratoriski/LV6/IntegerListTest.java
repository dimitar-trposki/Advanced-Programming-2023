package ZimskiSemestar.VtorKolokvium.Laboratoriski.LV6;

import java.util.*;
import java.util.stream.Collectors;

class IntegerList {
    ArrayList<Integer> integers;

    public IntegerList() {
        this.integers = new ArrayList<>();
    }

    public IntegerList(Integer... array) {
        this.integers = new ArrayList<>();
        Collections.addAll(this.integers, array);
        //this.integers = new ArrayList<>(List.of(array));
    }

    public void add(int el, int idx) {
        if (idx < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (idx > integers.size() - 1) {
            while ((integers.size() < idx)) {
                integers.add(0);
            }
            integers.add(el);
        } else {
            integers.add(idx, el);
        }
    }

    public int remove(int idx) {
        if (idx < 0 || idx >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return integers.remove(idx);
    }

    public void set(int el, int idx) {
        if (idx < 0 || idx >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        integers.set(idx, el);
    }

    public int get(int idx) {
        if (idx < 0 || idx >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return integers.get(idx);
    }

    public int size() {
        return integers.size();
    }

    public int count(int el) {
        return (int) integers.stream().filter(i -> i == el).count();
    }

    public void removeDuplicates() {
        HashSet<Integer> seen = new HashSet<>();
        for (int i = integers.size() - 1; i >= 0; i--) {
            int num = integers.get(i);
            if (seen.contains(num)) {
                integers.remove(i);
            } else {
                seen.add(num);
            }
        }
    }

    public int sumFirst(int k) {
        return integers.stream().limit(k).mapToInt(i -> i).sum();
    }

    public int sumLast(int k) {
//        int sum = 0;
//        for (int i = k; i < integers.size(); i++) {
//            sum += integers.get(i);
//        }
//        return sum;
        return integers.stream().skip(integers.size() - k).mapToInt(i -> i).sum();
    }

    public void shiftRight(int idx, int k) {
        if (idx < 0 || idx >= integers.size()) {
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");
        }
        int n = integers.size();
        int newPos = (idx + k) % n;
        int value = integers.remove(idx);
        integers.add(newPos, value);
    }

    public void shiftLeft(int idx, int k) {
        if (idx < 0 || idx >= integers.size()) {
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");
        }
        int n = integers.size();
        int newPos = (idx - k % n + n) % n;
        int value = integers.remove(idx);
        integers.add(newPos, value);
    }

    public IntegerList addValue(int value) {
        IntegerList newList = new IntegerList();
        for (int num : this.integers) {
            newList.add(num + value, newList.size());
        }
        return newList;
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