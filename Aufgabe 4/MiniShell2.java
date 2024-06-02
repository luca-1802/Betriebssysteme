import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MiniShell2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equals("exit")) break;
            else if (input.contains("run(")) runCommand(input.substring(4, input.length() - 1));
            else if (input.contains("parallel(")) parallelCommand(input.substring(9, input.length() - 1));
            else System.out.println("Unknown command: " + input);
        }
        scanner.close();
    }

    private static void runCommand(String command) {
        List<String> commandList = parseCommand(command);
        executeProcess(commandList);
    }

    private static void parallelCommand(String command) {
        String[] commands = command.split("\\)\\(");
        List<List<String>> commandLists = new ArrayList<>();

        for (String cmd : commands) {
            cmd = cmd.replace("(", "").replace(")", "");
            commandLists.add(parseCommand(cmd));
        }

        List<Thread> threads = new ArrayList<>();
        for (List<String> cmdList : commandLists) {
            Thread thread = new Thread(() -> executeProcess(cmdList));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> parseCommand(String command) {
        List<String> commandList = new ArrayList<>();
        Scanner scanner = new Scanner(command);
        while (scanner.hasNext()) {
            commandList.add(scanner.next());
        }
        scanner.close();
        return commandList;
    }

    private static void executeProcess(List<String> commandList) {
        ProcessBuilder processBuilder = new ProcessBuilder(commandList);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
