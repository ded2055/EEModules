package Module1;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class CompareEffectiveCollection {
    static int K10 = 10000;
    static int K100 = 100000;
    static int K1000 = 1000000;

    public static void main(String[] args) {


        List<String[]> printedList10K = new ArrayList<>(3);

        ArrayList<Long> aL10K = new ArrayList<>(K10);
        LinkedList<Long> lL10K = new LinkedList<>();
        HashSet<Long> hS10K = new HashSet<>(K10);
        TreeSet<Long> tS10K = new TreeSet<>();

        printedList10K.add(calculateEfficiency("ArrayList", aL10K, K10));
        printedList10K.add(calculateEfficiency("LinkedList", lL10K, K10));
        printedList10K.add(calculateEfficiency("HashSet", hS10K, K10));
        printedList10K.add(calculateEfficiency("TreeSet", tS10K, K10));

        new ShowTable("Size Data 10K", printedList10K);

        List<String[]> printedList100K = new ArrayList<>(3);

        ArrayList<Long> aL100K = new ArrayList<>(K100);
        LinkedList<Long> lL100K = new LinkedList<>();
        HashSet<Long> hS100K = new HashSet<>(K100);
        TreeSet<Long> tS100K = new TreeSet<>();

        printedList100K.add(calculateEfficiency("ArrayList", aL100K, K100));
        printedList100K.add(calculateEfficiency("LinkedList", lL100K, K100));
        printedList100K.add(calculateEfficiency("HashSet", hS100K, K100));
        printedList100K.add(calculateEfficiency("TreeSet", tS100K, K100));

        new ShowTable("Size Data 100K", printedList100K);

        List<String[]> printedList1000K = new ArrayList<>(3);

        ArrayList<Long> aL1000K = new ArrayList<>(K1000);
        LinkedList<Long> lL1000K = new LinkedList<>();
        HashSet<Long> hS1000K = new HashSet<>(K1000);
        TreeSet<Long> tS1000K = new TreeSet<>();

        printedList1000K.add(calculateEfficiency("ArrayList", aL1000K, K1000));
        printedList1000K.add(calculateEfficiency("LinkedList", lL1000K, K1000));
        printedList1000K.add(calculateEfficiency("HashSet", hS1000K, K1000));
        printedList1000K.add(calculateEfficiency("TreeSet", tS1000K, K1000));

        new ShowTable("Size Data 1000K", printedList1000K);
    }

    static String[] calculateEfficiency(String aName, List item, int size) {
        long count = 100;
        long populate = 0;
        long add = 0;
        long get = 0;
        long contains = 0;
        long remove = 0;
        long addIter = 0;
        long removeIter = 0;

        for (int i = 0; i < size; i++) {
            populate += calculatePopulate(item, size);
            item.clear();
        }

        calculatePopulate(item, size);

        for (int i = 0; i < 100; i++) {
            add += calculateAdd(item);
            get += calculateGet(item);
            contains += calculateContains(item);
            remove += calculateRemove(item);
            addIter += calculateAddIterator(item);
            removeIter += calculateRemoveIterator(item);
        }
        return new String[]{aName, Long.toString(add / count), Long.toString(get / count), Long.toString(remove / count),
                Long.toString(contains / count), Long.toString(populate / count), Long.toString(addIter / count), Long.toString(removeIter / count)};
    }

    static String[] calculateEfficiency(String aName, Set item, int size) {
        long count = 100;
        long populate = 0;
        long add = 0;
        long contains = 0;
        long remove = 0;

        for (int i = 0; i < size; i++) {
            populate += calculatePopulate(item, size);
            item.clear();
        }

        calculatePopulate(item, size);

        for (int i = 0; i < 100; i++) {
            add += calculateAdd(item);
            contains += calculateContains(item);
            remove += calculateRemove(item);
        }
        return new String[]{aName, Long.toString(add / count), "N/A", Long.toString(remove / count),
                Long.toString(contains / count), Long.toString(populate / count), "N/A", "N/A"};
    }

    static long calculateAddIterator(List items) {
        ListIterator iterator = items.listIterator();

        long start = System.nanoTime();

        iterator.next();
        for (int i = 1; i < (int) Math.random() * (items.size() - 1); i++) {
            if (!iterator.hasNext()) break;
            iterator.next();
        }
        iterator.add(new Double(1000 * Math.random()).longValue());

        long finish = System.nanoTime();
        return finish - start;
    }

    static long calculateRemoveIterator(List items) {
        ListIterator iterator = items.listIterator();
        long start = System.nanoTime();

        iterator.next();
        for (int i = 1; i < (int) Math.random() * (items.size() - 1); i++) {
            if (!iterator.hasNext()) break;
            iterator.next();
        }
        iterator.remove();

        long finish = System.nanoTime();
        return finish - start;
    }

    static long calculateContains(Collection items) {
        long start = System.nanoTime();

        items.contains(new Double(1000 * Math.random()).longValue());

        long finish = System.nanoTime();
        return finish - start;
    }

    static long calculatePopulate(Collection items, int size) {
        long start = System.nanoTime();

        for (int i = 0; i < size - 1; i++)
            items.add(new Double(10000 * Math.random()).longValue());

        long finish = System.nanoTime();
        return finish - start;
    }


    static long calculateAdd(List items) {
        long start = System.nanoTime();

        int i = (int) (Math.random() * (items.size() - 1));
        items.add(i, (long) i);

        long finish = System.nanoTime();
        return finish - start;
    }

    static long calculateAdd(Set items) {
        long start = System.nanoTime();

        int i = (int) (Math.random() * 10000);
        items.add((long) i);

        long finish = System.nanoTime();
        return finish - start;
    }

    static long calculateGet(List items) {
        long start = System.nanoTime();

        int i = (int) (Math.random() * (items.size() - 1));
        items.get(i);

        long finish = System.nanoTime();
        return finish - start;
    }

    static long calculateRemove(List items) {
        long start = System.nanoTime();

        int i = (int) (Math.random() * (items.size() - 1));
        items.remove(i);

        long finish = System.nanoTime();
        return finish - start;
    }

    static long calculateRemove(Set items) {
        long start = System.nanoTime();

        items.remove(new Double(10000 * Math.random()).longValue());

        long finish = System.nanoTime();
        return finish - start;
    }

}

class ShowTable extends JFrame {

    private final static String[] columnNames =
            {"Collection", "add", "get", "contains", "remove", "populate", "iterator.add", "iterator.remove"};

    private String[][] data = new String[4][8];

    private JButton jbtSave = new JButton("Save Table");

    private JTable myJTable = new JTable(data, columnNames);

    private JFileChooser myJFileChooser = new JFileChooser(new File("."));

    private void saveTable() {
        if (myJFileChooser.showSaveDialog(this) ==
                JFileChooser.APPROVE_OPTION) {
            saveTable(myJFileChooser.getSelectedFile());
        }
    }

    private void saveTable(File file) {

        try {
            FileWriter fw = new FileWriter(file);

            String AlignFormat = "| %-10s |  %-5s  |   %-5s  |   %-5s  |   %-8s |   %-12s  |    %-8s  |      %-10s |%n";
            String writeToFile;
            fw.write("+------------+---------+----------+----------+------------+-----------------+--------------+-----------------+\r\n");
            fw.write("| Collection |   add   |    get   |  remove  |  contains  |     populate    | iterator.add | iterator.remove |\r\n");
            fw.write("+------------+---------+----------+----------+------------+-----------------+--------------+-----------------+\r\n");
            for (int i = 0; i < myJTable.getRowCount(); i++) {
                writeToFile = String.format(AlignFormat, data[i][0], data[i][1], data[i][2], data[i][3], data[i][4], data[i][5], data[i][6], data[i][7]);
                fw.write(writeToFile);
            }
            fw.write("+------------+---------+----------+----------+------------+-----------------+--------------+-----------------+");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ShowTable(String title, List<String[]> tableData) {

        setTitle(title);

        int i = 0;
        for (String[] row :
                tableData) {
            for (int j = 0; j < row.length; j++) {
                data[i][j] = row[j];
            }
            i++;
        }

        add(new JScrollPane(myJTable), BorderLayout.CENTER);

        JPanel panel = new JPanel(new java.awt.GridLayout(1, 2));
        panel.add(jbtSave);
        add(panel, BorderLayout.SOUTH);

        jbtSave.addActionListener(e -> saveTable());

        setSize(800, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
}