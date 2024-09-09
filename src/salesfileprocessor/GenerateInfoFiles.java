package salesfileprocessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class GenerateInfoFiles {

	/**
	 * Generates a sales report file for a salesman with random sales data.
	 * The file is named as "sales_{name}_{id}.txt".
	 * 
	 * @param randomSalesCount the number of random sales records to generate
	 * @param name             the name of the salesman
	 * @param id               the unique ID of the salesman
	 */
	public void createSalesManFile(int randomSalesCount, String name, long id) {
		// File name format: sales_{name}_{id}.txt
		String folderName = "output";
		String fileName = folderName + "/sales_" + name + "_" + id + ".txt";
		Random random = new Random(); // Random object to generate random values

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
				int quantitySold = random.nextInt(10) + 1; // Generate random quantity (1-10)

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
		Random random = new Random(); // Random object to generate random prices

		// Create folder if it doesn't exist
		File folder = new File(folderName);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			// Generate and write product information
			for (int i = 1; i <= productsCount; i++) {
				int productId = i; // Sequential product ID
				String productName = "Product" + i; // Product name
				int price = (random.nextInt(91) + 10) * 1000; // Random price between 10000 and 100000

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

	/**
	 * Generates a file containing salesman information with randomly generated
	 * document numbers and names.
	 * The file is named "salesman.txt".
	 * 
	 * @param salesmanCount the number of salesman to include in the file
	 * @return a list of salesman with their names and document numbers
	 */
	public List<String[]> createSalesMenInfoFile(int salesmanCount) {
		// File name for the salesmen information
		String folderName = "output";
		String fileName = folderName + "/salesmen.txt";
		Random random = new Random(); // Random object to generate random document numbers and names

		// Check if the folder exists; if not, create it
		File folder = new File(folderName);
		if (!folder.exists()) {
			folder.mkdirs(); // Create the folder if it doesn't exist
		}

		// Lists of possible first names and last names
		List<String> firstNames = Arrays.asList(
				"Juan", "Pedro", "Luis", "Ana", "Maria", "Carlos", "Sofia", "David", "Laura",
				"Jose", "Paula", "Andres", "Daniela", "Miguel", "Camila");
		List<String> lastNames = Arrays.asList(
				"Perez", "Gomez", "Lopez", "Garcia", "Martinez", "Rodriguez", "Hernandez", "Morales",
				"Torres", "Ramirez", "Gutierrez", "Rojas", "Mendoza", "Vargas", "Castro");

		// List to store generated salesmen information
		List<String[]> salesmen = new ArrayList<>();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			// Generate and write salesmen information
			for (int i = 1; i <= salesmanCount; i++) {
				String documentType = "CC";
				long documentNumber = 1000000000L + random.nextInt(999999999); // Random document number
				String firstName = firstNames.get(random.nextInt(firstNames.size())); // Random first name
				String lastName = lastNames.get(random.nextInt(lastNames.size())); // Random last name

				// Add salesman information to the list
				salesmen.add(new String[] { firstName + "_" + lastName, String.valueOf(documentNumber) });

				// Write salesmen information to the file
				writer.write(documentType + ";" + documentNumber + ";" + firstName + ";" + lastName);
				writer.newLine();
			}

			System.out.println("Salesmen information file generated: " + fileName);
		} catch (IOException e) {
			// Handle file writing errors
			System.err.println("Error generating salesmen information file: " + e.getMessage());
		}

		return salesmen; // Return the list of generated salesmen
	}

	/**
	 * Main method to generate salesmen information and sales files for each
	 * salesman.
	 * 
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		// Create an instance of GenerateInfoFiles to generate files
		GenerateInfoFiles generator = new GenerateInfoFiles();

		// Generate test files with sample data
		generator.createProductsFile(10); // Generates a file with 10 products

		// Generate salesmen information and retrieve the list of salesmen
		List<String[]> salesmen = generator.createSalesMenInfoFile(5); // Generates 5 salesmen

		// For each salesman, generate a corresponding sales file
		for (String[] salesman : salesmen) {
			String name = salesman[0]; // Full name of the salesman
			long id = Long.parseLong(salesman[1]); // Document number of the salesman
			generator.createSalesManFile(5, name, id); // Generate a sales file with 5 random sales
		}
	}
}
