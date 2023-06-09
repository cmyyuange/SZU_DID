package org.example;

import java.io.*;

public class countTime {
    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();
        int sum = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("./test.txt"))) {
            String line;
            String separator = "：";
            String ms = "ms";
            int start;
            int end;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            String txt = sb.toString();
            start = txt.indexOf(separator);
            end = txt.indexOf(ms);
            while (start != -1 && end != -1) {
                i++;
                int checkNum = sum;
                sum = sum + Integer.parseInt(txt.substring(start + 1,end));
                if (checkNum > sum) {
                    System.err.println("算术溢出！");
                }
                if (!txt.contains(ms)) {
                    break;
                }
                txt = txt.substring(end + 2);
                start = txt.indexOf(separator);
                end = txt.indexOf(ms);
            }
            System.out.println("sum是：" + sum);
            System.out.println("i是：" + i);
            System.out.println("结果是：" + (sum/i));
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./test.txt"));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
