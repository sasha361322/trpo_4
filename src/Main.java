import java.io.*;
import java.util.*;

public class Main {
    private static final int buffer=5;

    public static Boolean isOrdered(List<Integer> arr){
        boolean res = true;
        int i=1;
        while ((i < arr.size())&&(res)){
            res = arr.get(i)>=arr.get(i-1);
            i++;
        }
        return res;
    }

    private static void randomFile(int N){
        try{
            PrintWriter writer = new PrintWriter("source.txt", "UTF-8");
            Random random = new Random();
            for (int i = 0; i < N; i++){
                writer.write(random.nextInt(1000) + " ");
            }
            writer.close();
        } catch (IOException e) {}
        System.out.println("done");
    }

    public static void pass() throws UnsupportedEncodingException, FileNotFoundException {
        Scanner sc = new Scanner(new File("source.txt"));
        int currentFileNumber = 0, previous=0;
        ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
        for (int i = 1; i <= buffer; i++) {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("temp"+i+".txt", true), "UTF-8"));
            printWriters.add(pw);
        }
        if (sc.hasNext()) {
            previous = sc.nextInt();
            printWriters.get(0).write(previous + " ");
        }
        while (sc.hasNext()) {
            int current = sc.nextInt();
            if (current < previous) {//пишем в следующий
                currentFileNumber = (currentFileNumber + 1) % buffer;
            }
            //пишем в текущий файл
            printWriters.get(currentFileNumber).write(current + " ");
            previous = current;
        }
        sc.close();
        for (int i = 0; i < buffer; i++) {
            printWriters.get(i).close();
        }
        new File("source.txt").delete();
        //распихали по файлам, теперь слияние

        List<Scanner>scanners = new ArrayList<Scanner>();
        for (int i = 1; i <= buffer; i++) {
            if (new File("temp" + i + ".txt").exists()) {
                sc = new Scanner(new File("temp" + i + ".txt"));
                scanners.add(sc);
            }
        }
        //slice - "срез" текущих файлов
        List<Integer> slise = new ArrayList<Integer>();
        for (int i = 0; i < scanners.size(); i++){
            if (scanners.get(i).hasNext())
                slise.add(scanners.get(i).nextInt());
        }
        PrintWriter pw = new PrintWriter(new File("source.txt"), "UTF-8");
        int min;
        while ((min = (int)Collections.min(slise))!=Integer.MAX_VALUE){
            int index = slise.indexOf(min);
            pw.write(min + " ");
            if (scanners.get(index).hasNext())
                slise.set(index, scanners.get(index).nextInt());//вместо минимального, берем след. из этого файла
            else
                slise.set(index, Integer.MAX_VALUE);//если файл кончился, ставим в срез null
        }
        for (int i = 1; i <= scanners.size(); i++){
            scanners.get(i-1).close();//закрываем сканеры
            new File("temp" + i + ".txt").delete();//удаляем tmp файлы
        }
        pw.close();
        List result = new ArrayList<Integer>();
        Scanner scanner = new Scanner(new File("source.txt"));
        while (scanner.hasNext())
            result.add(scanner.nextInt());
        scanner.close();
        System.out.println(result.toString());
        if (!isOrdered(result))
            pass();
     }

    public static void main(String[] args) {
        randomFile(31);
        try {
            pass();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("done");
        new File("source.txt").delete();
    }
}
