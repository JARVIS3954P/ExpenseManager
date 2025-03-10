package com.zidioDev.ExpenseManager.util;

import java.io.PrintWriter;
import java.util.List;

public class CsvExportUtils {

    public static void writeCsv(PrintWriter writer, List<String[]> data) {
        for (String[] row : data) {
            writer.println(String.join(",", row));
        }
    }
}
