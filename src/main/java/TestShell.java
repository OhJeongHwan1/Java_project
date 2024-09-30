import java.io.File;
import java.util.Scanner;

public class TestShell {
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
                System.out.println("write <block> <data> - Writes data to a specific block");
                System.out.println("read <block> - Reads data from a specific block");
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
                        System.out.println("Usage: write <block> <data>");
                        continue;
                    }
                    int block = Integer.parseInt(parts[1]);
                    String data = parts[2];

                    // SSD 프로그램의 write 명령어 실행
                    executeSSDCommand("W", block, data);

                } else if (command.equalsIgnoreCase("read")) {
                    if (parts.length != 2) {
                        System.out.println("Usage: read <block>");
                        continue;
                    }
                    int block = Integer.parseInt(parts[1]);

                    // SSD 프로그램의 read 명령어 실행
                    executeSSDCommand("R", block, null);

                } else {
                    System.out.println("Unknown command. Type 'help' for a list of commands.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid block number format.");
            }
        }
    }

    // SSD 프로그램 실행 메서드
    private static void executeSSDCommand(String mode, int block, String data) {
        try {
            ProcessBuilder builder;

            if ("W".equals(mode)) {
                builder = new ProcessBuilder("java", "SSD", mode, String.valueOf(block), data);
            } else {
                builder = new ProcessBuilder("java", "SSD", mode, String.valueOf(block));
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
