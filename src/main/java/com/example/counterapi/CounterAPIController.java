package com.example.counterapi;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/counter-api")
public class CounterAPIController {

    private static final Logger log = LoggerFactory.getLogger(CounterAPIController.class);

    @PostMapping("/search")
    public Map<String, Map<String, Long>> search(@RequestBody HashMap<String, ArrayList<String>> map) throws Exception {
        if (map.isEmpty()) {
            return new HashMap<>();
        }
        ArrayList<String> list = map.get("searchText");
        Map<String, Long> hashMap = new HashMap<>();
        Map<String, Long> fileMap = FileUtil.processFile(new FileReader("sample.txt"));
        for (String word : list) {
            hashMap.put(word, fileMap.getOrDefault(word, 0L));
        }
        Map<String, Map<String, Long>> finalMap = new HashMap<>();
        finalMap.put("counts", hashMap);
        return finalMap;
    }

    @GetMapping(value = "/top/{num}", produces = "text/csv")
    public ResponseEntity generateCsvReport(@PathVariable Long num) throws Exception {
        Map<String, Long> fileMap = FileUtil.processFile(new FileReader("sample.txt"));
        Map<String, Long> linkedHashMap =  MapSortUtil.sortByValueDesc(fileMap);
        ByteArrayInputStream byteArrayOutputStream;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT);
            int count = 1;
            Iterator<Map.Entry<String, Long>> it = linkedHashMap.entrySet().iterator();
            while (it.hasNext() && count <= num) {
                Map.Entry<String, Long> entry = it.next();
                csvPrinter.printRecord(entry.getKey() + "|" + entry.getValue());
                count++;
            }
            csvPrinter.flush();
            byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);
        String csvFileName = "result.csv";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        return new ResponseEntity<>(fileInputStream, headers, HttpStatus.OK);
    }
}