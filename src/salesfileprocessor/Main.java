package salesfileprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    

    public static void main(String[] args) {
        Main processor = new Main();
        processor.processSalesFiles();
    }
}