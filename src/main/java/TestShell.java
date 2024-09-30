import java.io.File;
import java.util.Scanner;
import java.io.*;
import java.util.*;

public class TestShell {
    public static void fullWriteToFile(String filePath, String data) {
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();


        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(data);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String l : lines) {
                    writer.write(l);
                    writer.newLine();
                }
            }

            System.out.println("All lines in " + filePath + " are updated with: " + data);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to " + filePath + ": " + e.getMessage());
        }
    }

    public static void fullReadToFile(String filePath) {
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // 파일의 각 줄을 출력
            }


        } catch (IOException e) {
            System.out.println("An error occurred while writing to " + filePath + ": " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str;

        while (true) {
            System.out.print("SHELL > ");
            str = sc.nextLine().trim();

            // exit 명령어 처리
            if (str.equalsIgnoreCase("exit")) {
                System.out.println("BYE");
                break;
            }

            // help 명령어 처리
            if (str.equalsIgnoreCase("help")) {
                System.out.println("Available commands:");
                System.out.println("write <address> <data> - Writes data to a specific address");
                System.out.println("read <address> - Reads data from a specific address");
                System.out.println("exit - Exit the shell");
                System.out.println("help - Show available commands");
                continue;
            }

            // 명령어 분석
            String[] parts = str.split(" ");
            if (parts.length == 0) continue;

            String command = parts[0];

            try {
                if (command.equalsIgnoreCase("write")) {
                    if (parts.length != 3) {
                        System.out.println("Usage: write <address> <data>");
                        continue;
                    }
                    int address = Integer.parseInt(parts[1]);
                    String data = parts[2];

                    // SSD 프로그램의 write 명령어 실행
                    executeSSDCommand("W", address, data);

                } else if (command.equalsIgnoreCase("read")) {
                    if (parts.length != 2) {
                        System.out.println("Usage: read <address>");
                        continue;
                    }
                    int address = Integer.parseInt(parts[1]);

                    // SSD 프로그램의 read 명령어 실행
                    executeSSDCommand("R", address, null);
                }
                else if (command.equalsIgnoreCase("fullwrite")) {
                    fullWriteToFile("C:\\oeunsol_java\\untitled5\\src\\main\\java\\nand.txt", parts[1]);
                    break;
                }
                else if (command.equalsIgnoreCase("fullread")) {
                    fullReadToFile("C:\\oeunsol_java\\untitled5\\src\\main\\java\\nand.txt");
                    break;
                }

                else {
                    System.out.println("INVALID COMMAND");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid address number format.");
            }
        }
    }

    // SSD 프로그램 실행 메서드
    private static void executeSSDCommand(String mode, int address, String data) {
        try {
            ProcessBuilder builder;

            if ("W".equals(mode)) {
                builder = new ProcessBuilder("java", "SSD", mode, String.valueOf(address), data);
            } else {
                builder = new ProcessBuilder("java", "SSD", mode, String.valueOf(address));
            }

            // 현재 작업 디렉토리를 명시적으로 설정 (SSD.class 파일이 있는 위치)
            builder.directory(new File(".")); // 현재 디렉토리를 의미함, TestShell이 실행되는 디렉토리와 동일
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // SSD 프로그램의 출력을 읽어 화면에 출력
            try (Scanner processOutput = new Scanner(process.getInputStream())) {
                while (processOutput.hasNextLine()) {
                    System.out.println(processOutput.nextLine());
                }
            }

            process.waitFor();
        } catch (Exception e) {
            System.out.println("Error executing SSD command: " + e.getMessage());
        }
    }
}





