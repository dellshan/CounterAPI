package com.example.counterapi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

    public static Map<String, Long> processFile(FileReader file) throws IOException {
        Map<String, Long> map = new HashMap<String, Long>();
        String line;
        BufferedReader br = new BufferedReader(file);
        while ((line = br.readLine()) != null) {
            String words[] = line.split(" ");
            for (String w : words) {
                w = w.replace(".", "")
                        .replace(",", "")
                        .replace(";", "").trim();
                if (map.get(w) == null) {
                    map.put(w, Long.valueOf(1));
                } else {
                    map.put(w, map.get(w) + 1);
                }
            }
        }
        br.close();
        return map;
    }
}
