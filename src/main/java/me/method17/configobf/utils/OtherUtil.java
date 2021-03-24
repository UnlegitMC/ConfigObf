package me.method17.configobf.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;

public class OtherUtil {
    public static String readFile(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(File file, String text) {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writer.write(text);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(str.length() - 1);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static int randomInteger(double min, double max) {
        return (int) (Math.random() * (max - min) + min);
    }
}
