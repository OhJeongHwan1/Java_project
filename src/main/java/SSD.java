import java.io.*;
import java.util.*;

public class SSD {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage:");
            System.out.println("For writing: java ssd.java W <block> <data>");
            System.out.println("For reading: java ssd.java R <block>");
            return;
        }

        String mode = args[0];
        int block;
        try {
            block = Integer.parseInt(args[1]);
            if (block < 0 || block > 99) {
                System.out.println("Block number must be between 0 and 99.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid block number.");
            return;
        }

        if (mode.equalsIgnoreCase("W")) {
            if (args.length != 3) {
                System.out.println("Invalid arguments for write operation.");
                return;
            }
            String data = args[2];
            writeToFile(block, data);
        } else if (mode.equalsIgnoreCase("R")) {
            readFromFile(block);
        } else {
            System.out.println("Invalid mode. Use 'W' for write or 'R' for read.");
        }
    }

    private static void writeToFile(int block, String data) {
        File file = new File("nand.txt");
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            if (block >= lines.size()) {
                System.out.println("The block number is out of the current nand.txt range.");
                return;
            }

            lines.set(block, data);  // Update the specified line with the new data

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String l : lines) {
                    writer.write(l);
                    writer.newLine();
                }
            }

            System.out.println("Data written to block " + block + " in nand.txt");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to nand.txt: " + e.getMessage());
        }
    }

    private static void readFromFile(int block) {
        File file = new File("nand.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter resultWriter = new BufferedWriter(new FileWriter("result.txt"))) {

            String line;
            int currentLine = 0;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (currentLine == block) {
                    resultWriter.write(line);
                    resultWriter.newLine();
                    System.out.println("Data found and written to result.txt: " + line);
                    found = true;
                    break;
                }
                currentLine++;
            }

            if (!found) {
                System.out.println("No data found for block " + block);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading from nand.txt or writing to result.txt: " + e.getMessage());
        }
    }
}
