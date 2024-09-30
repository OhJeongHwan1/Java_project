import java.io.*;

public class SSD {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage:");
            System.out.println("For writing: java SSD W <block> <data>");
            System.out.println("For reading: java SSD R <block>");
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

        if (mode.equalsIgnoreCase("R")) {
            readFromFile(block);
        } else if (mode.equalsIgnoreCase("W")) {
            if (args.length != 3) {
                System.out.println("Invalid arguments for write operation.");
                return;
            }
            String data = args[2];
            writeToFile(block, data);
        } else {
            System.out.println("Invalid mode. Use 'W' for write or 'R' for read.");
        }
    }

    private static void writeToFile(int block, String data) {
        File file = new File("nand.txt");
        try {
            // 기존 파일 내용을 모두 읽어옴
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            int currentLine = 0;

            while ((line = reader.readLine()) != null) {
                if (currentLine == block) {
                    content.append(data).append("\n"); // 해당 줄을 새 데이터로 변경
                } else {
                    content.append(line).append("\n");
                }
                currentLine++;
            }

            reader.close();

            // 변경된 내용을 다시 파일에 기록
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());
            writer.close();

            System.out.println("Data written to nand.txt at line " + block);

        } catch (IOException e) {
            System.out.println("An error occurred while writing to nand.txt: " + e.getMessage());
        }
    }

    private static void readFromFile(int block) {
        File file = new File("nand.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter resultWriter = new BufferedWriter(new FileWriter("result.txt", true))) { // append 모드

            String line;
            int currentLine = 0;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (currentLine == block) {
                    System.out.println("Data from line " + block + ": " + line); // 화면에 출력
                    resultWriter.write(line); // result.txt에 값 추가
                    resultWriter.newLine();
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
