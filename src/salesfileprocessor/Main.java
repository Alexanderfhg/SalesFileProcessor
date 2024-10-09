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
import java.util.List;

/**
 * Main class responsible for processing sales files and generating reports.
 * It handles product loading, sales file processing, and report generation.
 */
public class Main {

    // Stores product details: productId mapped to a String array [name, price]
    private Map<Integer, String[]> products = new HashMap<>();
    
    // Stores the count of sales per product: productId mapped to number of sales
    private Map<Integer, Integer> productSales = new HashMap<>();
    
    // Stores the total sales made by each salesman: salesman name mapped to total sales amount
    private Map<String, Double> salesmenSales = new HashMap<>();

    /**
     * Generates reports for both products and salesmen.
     * It first loads the product data, processes the sales files, and then writes the reports.
     * 
     * @param productReportPath Path where the product report will be written.
     * @param salesmanReportPath Path where the salesman report will be written.
     */
    public void generateReports(String productReportPath, String salesmanReportPath) {
        loadProducts("output/products.txt");
        processSalesFiles("output");
        writeProductReport(productReportPath);
        writeSalesmanReport(salesmanReportPath);
    }

    /**
     * Loads product information from a given file and initializes the productSales map.
     * The product file is expected to have the format: productId;productName;price.
     * 
     * @param productsFilePath Path to the file containing the product data.
     */
    private void loadProducts(String productsFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(productsFilePath))) {
            String line;
            // Reads each line of the product file
            while ((line = reader.readLine()) != null) {
                // Splits the line into productId, productName, and price
                String[] parts = line.split(";");
                int productId = Integer.parseInt(parts[0]);
                String productName = parts[1];
                String price = parts[2];
                // Stores the product details in the products map
                products.put(productId, new String[]{productName, price});
                // Initializes the sales count for this product to 0
                productSales.put(productId, 0);
            }
        } catch (IOException e) {
            // Logs any IO errors encountered while reading the product file
            System.err.println("Error loading products: " + e.getMessage());
        }
    }

    /**
     * Processes all sales files in a specified folder. The method expects files
     * to be named with the pattern 'sales_*.txt'.
     * 
     * @param salesFolderPath Path to the folder containing sales files.
     */
    private void processSalesFiles(String salesFolderPath) {
        File folder = new File(salesFolderPath);
        // Filters files starting with 'sales_' and ending with '.txt'
        File[] salesFiles = folder.listFiles((dir, name) -> name.startsWith("sales_") && name.endsWith(".txt"));

        // Checks if sales files were found in the folder
        if (salesFiles != null) {
            // Processes each sales file found
            for (File file : salesFiles) {
                processSalesFile(file);
            }
        }
    }

    /**
     * Processes a single sales file. Each sales file contains sales data
     * for one salesman. The first line contains salesman information, and
     * subsequent lines contain product sales data in the format: productId;quantity.
     * 
     * @param salesFile The sales file to be processed.
     */
    private void processSalesFile(File salesFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(salesFile))) {
            // Read the first line containing general salesman information
            String salesmanInfo = reader.readLine(); 
            
            // Extract the salesman's name from the file name
            String salesmanName = extractSalesmanName(salesFile.getName());
            double salesmanTotal = 0.0; // Tracks the total sales amount for this salesman

            String line;
            // Process each line containing productId and quantity
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);

                // Update product sales count with the quantity sold
                productSales.put(productId, productSales.get(productId) + quantity);

                // Calculate the sales for the product and add to salesman's total
                int price = Integer.parseInt(products.get(productId)[1]);
                salesmanTotal += price * quantity;
            }

            // Record the total sales for this salesman
            salesmenSales.put(salesmanName, salesmanTotal);
        } catch (IOException e) {
            // Log any errors encountered while processing the sales file
            System.err.println("Error processing sales file " + salesFile.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Extracts the salesman's name from the sales file name.
     * The file name format is expected to be sales_firstname_lastname.txt,
     * and this method returns "firstname lastname".
     * 
     * @param fileName The name of the sales file (e.g., sales_john_doe.txt).
     * @return The salesman's name in the format "Firstname Lastname".
     */
    private String extractSalesmanName(String fileName) {
        // Split the file name by underscores and reconstruct the salesman's name
        String[] parts = fileName.split("_");
        return parts[1] + " " + parts[2];
    }

    /**
     * Writes a product sales report to the specified output file.
     * The report includes the product name, price, quantity sold, and total sales for each product.
     * The products are sorted by the quantity sold in descending order.
     * 
     * @param outputPath The file path where the product report will be written.
     */
    private void writeProductReport(String outputPath) {
        // Sort product sales by quantity sold in descending order
        List<Map.Entry<Integer, Integer>> sortedSales = new ArrayList<>(productSales.entrySet());
        sortedSales.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // Write report header
            writer.write("Product Name;Price;Quantity Sold;Total");
            writer.newLine();

            // Write each product's sales data
            for (Map.Entry<Integer, Integer> entry : sortedSales) {
                int productId = entry.getKey();
                String[] productInfo = products.get(productId);
                String productName = productInfo[0];
                int price = Integer.parseInt(productInfo[1]);
                int quantitySold = entry.getValue();
                int total = price * quantitySold;

                // Write product sales data in CSV format
                writer.write(String.format("%s;%d;%d;%d", productName, price, quantitySold, total));
                writer.newLine();
            }
        } catch (IOException e) {
            // Log any errors encountered while writing the product report
            System.err.println("Error writing product report: " + e.getMessage());
        }
    }

    /**
     * Writes a salesman sales report to the specified output file.
     * The report includes the salesman's name and the total sales amount.
     * The salesmen are sorted by their total sales in descending order.
     * 
     * @param outputPath The file path where the salesman report will be written.
     */
    private void writeSalesmanReport(String outputPath) {
        // Sort salesmen by total sales in descending order
        List<Map.Entry<String, Double>> sortedSalesmen = new ArrayList<>(salesmenSales.entrySet());
        sortedSalesmen.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // Write report header
            writer.write("Salesman Name;Total Sales");
            writer.newLine();

            // Write each salesman's total sales
            for (Map.Entry<String, Double> entry : sortedSalesmen) {
                String salesmanName = entry.getKey();
                double totalSales = entry.getValue();

                // Write salesman sales data in CSV format
                writer.write(String.format("%s;%.2f", salesmanName, totalSales));
                writer.newLine();
            }
        } catch (IOException e) {
            // Log any errors encountered while writing the salesman report
            System.err.println("Error writing salesman report: " + e.getMessage());
        }
    }

    /**
     * Main method to generate product and salesman sales reports.
     * It creates a Main instance and calls the generateReports method with the paths for both reports.
     * 
     * @param args Command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        // Create an instance of the Main class
        Main generator = new Main();
        
        // Generate product and salesman reports with specified file paths
        generator.generateReports("output/product_sales_report.csv", "output/salesman_sales_report.csv");
        
        // Notify that the reports have been generated successfully
        System.out.println("Sales reports generated successfully.");
    }
}