import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MiniShell {
    private static String color;

    // Main method
    public static void main(String[] args) {
        // Clear the console at the start
        clearConsole();

        Scanner scanner = new Scanner(System.in);
        String input;

        // Set the initial console text color to green
        color = "\033[0;32m";
        final String reset = "\033[0m";

        // Main loop to read user input and execute commands
        while (true) {
            // Print the current directory path with the set color
            System.out.print(color + System.getProperty("user.dir") + ">" + reset);
            input = scanner.nextLine().trim();

            // Process the input command
            if (input.equals("exit")) {
                break;
            } else if (input.startsWith("run(") && input.endsWith(")")) {
                runCommand(input.substring(4, input.length() - 1));
                System.out.println("");
            } else if (input.startsWith("parallel(") && input.endsWith(")")) {
                runParallel(input.substring(9, input.length() - 1));
                System.out.println("");
            } else if (input.equals("help")) {
                runHelp();
                System.out.println("");
            } else if (input.equals("clear")) {
                clearConsole();
            } else if (input.startsWith("setcolor(") && input.endsWith(")")) {
                setColor(input.substring(9, input.length() - 1));
                System.out.println("");
            } else {
                System.out.println("Command not found!\n");
            }
        }
        // Close the scanner
        scanner.close();
    }

    // Method to clear the console
    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").startsWith("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Type 'help' for a list of commands.\n");
    }

    // Method to run a single command
    private static void runCommand(String command) {
        List<String> commandList = parseCommand(command);
        ProcessBuilder processBuilder = new ProcessBuilder(commandList);

        try {
            // Start the process
            Process process = processBuilder.start();
            // Print the output of the process
            printProcessOutput(process);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to run multiple commands in parallel
    private static void runParallel(String commands) {
        // Split the commands
        String[] commandArray = commands.split("\\)\\(");
        List<Process> processes = new ArrayList<>();

        // Start each command as a separate process
        for (String command : commandArray) {
            command = command.replace("(", "").replace(")", "");
            List<String> commandList = parseCommand(command);
            ProcessBuilder processBuilder = new ProcessBuilder(commandList);

            try {
                Process process = processBuilder.start();
                processes.add(process);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Print the output of each process in a separate thread
        for (Process process : processes) {
            new Thread(() -> printProcessOutput(process)).start();
        }

        // Wait for all processes to finish
        for (Process process : processes) {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to display help information
    private static void runHelp() {
        String[] strings = {
                "Commands:",
                "exit",
                "clear",
                "setcolor(r,g,b)",
                "run(prog arg1 arg2 arg3 arg4 ...)",
                "parallel(prog1)(prog2 arg1 arg2 ...) ..."
        };

        for (String string : strings) {
            System.out.println(string);
        }
    }

    // Method to set the console text color
    private static void setColor(String colorString) {
        colorString = colorString.replace(" ", "");
        String[] clr = colorString.split(",");

        if (clr.length != 3) {
            System.out.println("Invalid color input [setcolor(r,g,b)]");
            return;
        }

        // Parse input arguments into integers
        int r, g, b;
        try {
            r = Integer.parseInt(clr[0]);
            g = Integer.parseInt(clr[1]);
            b = Integer.parseInt(clr[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid integer input!");
            return;
        }

        if ((r >= 0 && r < 256) && (g >= 0 && g < 256) && (b >= 0 && b < 256))
            color = String.format("\033[38;2;%d;%d;%dm", r, g, b);
        else
            System.out.println("Min value is 0 and Max value is 255!");
    }

    // Method to parse a command string into a list of arguments
    private static List<String> parseCommand(String command) {
        List<String> commandList = new ArrayList<>();
        Scanner scanner = new Scanner(command);
        while (scanner.hasNext()) {
            commandList.add(scanner.next());
        }
        scanner.close();

        return commandList;
    }

    // Method to print the output of a process
    private static void printProcessOutput(Process process) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }

            while ((line = error.readLine()) != null) {
                System.err.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}