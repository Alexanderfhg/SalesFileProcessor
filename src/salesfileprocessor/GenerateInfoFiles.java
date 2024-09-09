package salesfileprocessor;

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
}
