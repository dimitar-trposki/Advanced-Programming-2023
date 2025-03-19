package Septemvri.Laboratoriski.LV6;

import java.util.*;
import java.util.stream.Collectors;

class SuperString {

    private List<String> strings;
    private List<String> stringsOrdered;

    public SuperString() {
        strings = new LinkedList<>();
        stringsOrdered = new LinkedList<>();
    }

    public void append(String s) {
        strings.add(s);
        stringsOrdered.add(s);
    }

    public void insert(String s) {
        strings.add(0, s);
        stringsOrdered.add(s);
    }

    public boolean contains(String s) {
        return toString().contains(s);
    }

    private String reversedString(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    public void reverse() {
        Collections.reverse(strings);
        //strings.replaceAll(this::reversedString);
        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, reversedString(strings.get(i)));
        }
    }

    public void removeLast(int k) {
        for (int i = 0; i < k; i++) {
            String lastAdded = stringsOrdered.remove(stringsOrdered.size() - 1);
//            strings.remove(lastAdded);
            int lastIndex = strings.lastIndexOf(lastAdded);
            if (lastIndex != -1) {
                strings.remove(lastIndex);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        strings.forEach(stringBuilder::append);
        stringBuilder.append("\n");
        return stringBuilder.toString();
//        return String.join("", strings);
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            SuperString s = new SuperString();
            while (true) {
                int command = jin.nextInt();
                if (command == 0) {//append(String s)
                    s.append(jin.next());
                }
                if (command == 1) {//insert(String s)
                    s.insert(jin.next());
                }
                if (command == 2) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if (command == 3) {//reverse()
                    s.reverse();
                }
                if (command == 4) {//toString()
                    System.out.println(s);
                }
                if (command == 5) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if (command == 6) {//end
                    break;
                }
            }
        }
    }

}