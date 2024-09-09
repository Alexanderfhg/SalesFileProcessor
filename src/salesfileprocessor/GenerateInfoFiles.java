package salesfileprocessor;

import java.io.FileWriter;
//import java.io.FileReader;
//import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class GenerateInfoFiles {

	/**
	 * Generates a sales report file for a salesman with random sales data.
	 * The file is named as "sales_{name}_{id}.txt".
	 * 
	 * @param randomSalesCount the number of random sales records to generate
	 * @param name  the name of the salesman
	 * @param id    the unique ID of the salesman
	 */
	public void createSalesManFile(int randomSalesCount, String name, long id) {
	    // File name format: sales_{name}_{id}.txt
		String folderName = "output";
	    String fileName = folderName + "/sales_" + name + "_" + id + ".txt";
	    Random random = new Random();  // Random object to generate random values
	    
	    // Create folder if it doesn't exist
	    File folder = new File(folderName);
	    if (!folder.exists()) {
	        folder.mkdirs();
	    }

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
	        // Write header information for the salesman
	        writer.write("CC;" + id);
	        writer.newLine();

	        // Generate and write random sales records
	        for (int i = 1; i <= randomSalesCount; i++) {
	            int productId = i;
	            int quantitySold = random.nextInt(10) + 1;  // Generate random quantity (1-10)
	            
	            // Write product ID and quantity sold
	            writer.write(productId + ";" + quantitySold);
	            writer.newLine();
	        }

	        System.out.println("Sales file generated: " + fileName);
	    } catch (IOException e) {
	        // Handle any file writing errors
	        System.err.println("Error generating sales file: " + e.getMessage());
	    }
	}
	
	public static void main(String[] args) {
	    
	}
	
	/**
	 * Generates a product file with sequential product IDs and random prices.
	 * The file is named "products.txt".
	 * 
	 * @param productsCount the number of products to generate in the file
	 */
	public void createProductsFile(int productsCount) {
	    // File name for the products data
		String folderName = "output";
	    String fileName = folderName + "/products.txt";
	    Random random = new Random();  // Random object to generate random prices
	    
	    // Create folder if it doesn't exist
	    File folder = new File(folderName);
	    if (!folder.exists()) {
	        folder.mkdirs();
	    }

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
	        // Generate and write product information
	        for (int i = 1; i <= productsCount; i++) {
	            int productId = i;  // Sequential product ID
	            String productName = "Product" + i;  // Product name
	            int price = (random.nextInt(91) + 10) * 1000;  // Random price between 10000 and 100000

	            // Write product information to the file
	            writer.write(productId + ";" + productName + ";" + price);
	            writer.newLine();
	        }

	        System.out.println("Product file generated: " + fileName);
	    } catch (IOException e) {
	        // Handle file writing errors
	        System.err.println("Error generating product file: " + e.getMessage());
	    }
	}
}
