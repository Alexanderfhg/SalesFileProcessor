package salesfileprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    /**
     * Processes the sales files for all salesmen and generates the sales report and product report.
     */
    public void processSalesFiles() {
    	// Load salesman information from the salesmen file
        Map<String, String> salesmenInfo = loadSalesmenInfo("output/salesmen.txt");
    }
    
    /**
     * Loads the salesman information from the salesmen file.
     *
     * @param salesmenFilePath the path to the salesmen information file
     * @return a map of salesman name to document number
     */
    private Map<String, String> loadSalesmenInfo(String salesmenFilePath) {
        Map<String, String> salesmenInfo = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(salesmenFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String documentType = parts[0];
                String documentNumber = parts[1];
                String firstName = parts[2];
                String lastName = parts[3];

                String salesmanName = firstName + " " + lastName;
                salesmenInfo.put(salesmanName, documentNumber);
            }
        } catch (IOException e) {
            System.err.println("Error loading salesman information: " + e.getMessage());
        }

        return salesmenInfo;
    }

     /**
     * Generates the sales report from the individual sales files.
     *
     * @param salesReportPath the path to the sales report file
     * @param salesmenInfo    the map of salesman name to document number
     */
    private void generateSalesReport(String salesReportPath, Map<String, String> salesmenInfo) {
        Map<String, Double> salesReport = new HashMap<>();

        // Process each sales file and update the sales report
        File salesFolder = new File("output");
        for (File salesFile : salesFolder.listFiles((dir, name) -> name.startsWith("sales_"))) {
            // TODO: process Sales File method
        }

        // Sort the sales report by total sales in descending order
        List<Map.Entry<String, Double>> sortedSalesReport = new ArrayList<>(salesReport.entrySet());
        Collections.sort(sortedSalesReport, (a, b) -> Double.compare(b.getValue(), a.getValue()));

        // Write the sales report to the output file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(salesReportPath))) {
            for (Map.Entry<String, Double> entry : sortedSalesReport) {
                String salesmanName = entry.getKey();
                double totalSales = entry.getValue();
                writer.write(salesmanName + ";" + totalSales);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error generating sales report: " + e.getMessage());
        }
    }

    

    public static void main(String[] args) {
        Main processor = new Main();
        processor.processSalesFiles();
    }
}
