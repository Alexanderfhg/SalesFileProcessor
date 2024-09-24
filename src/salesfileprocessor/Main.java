package salesfileprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    /**
     * Processes the sales files for all salesmen and generates the sales report and product report.
     */
    public void processSalesFiles() {
        // Load salesman information from the salesmen file
        Map<String, String> salesmenInfo = loadSalesmenInfo("output/salesmen.txt");

        // Process each sales file and generate the sales report
        generateSalesReport("output/sales_report.csv", salesmenInfo);

        // Generate the product report
        generateProductReport("output/product_report.csv");
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
            processSalesFile(salesFile, salesmenInfo, salesReport);
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

    /**
     * Processes a single sales file and updates the sales report.
     *
     * @param salesFile      the sales file to process
     * @param salesmenInfo   the map of salesman name to document number
     * @param salesReport    the map to store the sales report
     */
    private void processSalesFile(File salesFile, Map<String, String> salesmenInfo, Map<String, Double> salesReport) {
        try (BufferedReader reader = new BufferedReader(new FileReader(salesFile))) {
            String line = reader.readLine(); // Read the salesman's document type and number
            String[] parts = line.split(";");
            String documentType = parts[0];
            String documentNumber = parts[1];

            // Find the salesman's name based on the document number
            String salesmanName = null;
            for (Map.Entry<String, String> entry : salesmenInfo.entrySet()) {
                if (entry.getValue().equals(documentNumber)) {
                    salesmanName = entry.getKey();
                    break;
                }
            }

            if (salesmanName != null) {
                double totalSales = 0.0;
                while ((line = reader.readLine()) != null) {
                    parts = line.split(";");
                    int productId = Integer.parseInt(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);
                    // TODO: Fetch product price and calculate total sales
                    totalSales += quantity; // Temporary calculation, replace with actual total sales
                }
                salesReport.put(salesmanName, totalSales);
            } else {
                System.err.println("Salesman not found for document number: " + documentNumber);
            }
        } catch (IOException e) {
            System.err.println("Error processing sales file: " + salesFile.getName() + " - " + e.getMessage());
        }
    }

    /**
     * Generates the product report from the product information file.
     *
     * @param productReportPath the path to the product report file
     */
    private void generateProductReport(String productReportPath) {
        Map<Integer, String[]> productInfo = new HashMap<>();

        // Load product information from the product file
        try (BufferedReader reader = new BufferedReader(new FileReader("output/products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int productId = Integer.parseInt(parts[0]);
                String productName = parts[1];
                int price = Integer.parseInt(parts[2]);
                productInfo.put(productId, new String[] { productName, String.valueOf(price) });
            }
        } catch (IOException e) {
            System.err.println("Error loading product information: " + e.getMessage());
        }

        // Sort the product information by total sales in descending order
        List<Map.Entry<Integer, String[]>> sortedProducts = new ArrayList<>(productInfo.entrySet());
        sortedProducts.sort((a, b) -> {
            // TODO: Sort by total sales instead of product ID
            return Integer.compare(b.getKey(), a.getKey());
        });

        // Write the product report to the output file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productReportPath))) {
            for (Map.Entry<Integer, String[]> entry : sortedProducts) {
                int productId = entry.getKey();
                String productName = entry.getValue()[0];
                String price = entry.getValue()[1];
                writer.write(productName + ";" + price);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error generating product report: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Main processor = new Main();
        processor.processSalesFiles();
    }
}