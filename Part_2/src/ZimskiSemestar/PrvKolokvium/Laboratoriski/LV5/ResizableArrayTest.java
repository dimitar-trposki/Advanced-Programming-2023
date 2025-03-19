package ZimskiSemestar.PrvKolokvium.Laboratoriski.LV5;

import java.util.*;
import java.util.stream.Collectors;

class ResizableArray<T> {
    private T[] array;
    int n;
    static final int arrayCapacity = 10;

    @SuppressWarnings("unchecked")
    public ResizableArray() {
        array = (T[]) new Object[arrayCapacity];
        n = 0;
    }

    public void addElement(T element) {
        if (array.length == n) {
            resize(array.length + arrayCapacity);
            // arrayCapacity *= 2;
        }
        //array = Arrays.copyOf(array, arrayCapacity);
        array[n++] = element;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        array = Arrays.copyOf(array, newCapacity);
    }

    public boolean removeElement(T element) {
        for (int i = 0; i < n; i++) {
            if (array[i].equals(element)) {
                array[i] = array[--n];
                array[n] = null;
                if (n > 0 && n == array.length / 4) {
                    resize(array.length / 2);
                }
                return true;
            }
        }
        return false;
    }

    public boolean contains(T element) {
        return Arrays.asList(array).contains(element);
//        for (int i = 0; i < n; i++) {
//            if (array[i].equals(element)) {
//                return true;
//            }
//        }
//        return false;
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, n);
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int count() {
        return n;
    }

    public T elementAt(int idx) {
        if (idx < 0 || idx >= count()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return array[idx];
    }

    @SuppressWarnings("unchecked")
    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        int srcLength = src.count();
        int destLength = dest.count();

        // Ensure capacity in dest array
        if (dest.array.length < destLength + srcLength) {
            dest.resize(destLength + srcLength);
        }

        System.arraycopy(src.array, 0, dest.array, destLength, srcLength);
        dest.n = destLength + srcLength;
    }
}

class IntegerArray extends ResizableArray<Integer> {
    public double sum() {
        return Arrays.stream(toArray()).mapToInt(i -> (int) i).sum();
//        int sum = 0;
//        for (int i = 0; i < count(); i++) {
//            sum += elementAt(i);
//        }
//        return sum;
    }

    public double mean() {
        return sum() / (double) count();
    }

    public int countNonZero() {
        return (int) Arrays.stream(toArray()).filter(i -> (int) i != 0).count();
//        int count = 0;
//        for (int i = 0; i < count(); i++) {
//            if (elementAt(i) != 0) {
//                count++;
//            }
//        }
//        return count;
    }

    public IntegerArray distinct() {
        //return (IntegerArray) Arrays.stream(toArray()).mapToInt(i -> (int) i).distinct();
        IntegerArray result = new IntegerArray();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < count(); i++) {
            if (!set.contains(elementAt(i))) {
                result.addElement(elementAt(i));
                set.add(elementAt(i));
            }
        }
        return result;
    }

    public IntegerArray increment(int offset) {
        IntegerArray result = new IntegerArray();
        for (int i = 0; i < count(); i++) {
            result.addElement(elementAt(i) + offset);
        }
        return result;
    }
}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if (test == 0) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while (jin.hasNextInt()) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if (test == 1) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for (int i = 0; i < 4; ++i) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if (test == 2) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while (jin.hasNextInt()) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if (a.sum() > 100)
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if (test == 3) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for (int w = 0; w < 500; ++w) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k = 2000;
                int t = 1000;
                for (int i = 0; i < k; ++i) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for (int i = 0; i < t; ++i) {
                    a.removeElement(k - i - 1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
