package toy.view;

import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Map;

public class TextMenu {
    private final Map<String, Command> commands;

    public TextMenu() {
        this.commands = new LinkedHashMap<>();
    }

    public void addCommand(Command command) {
        commands.put(command.getKey(), command);
    }

    private void printMenu() {
        System.out.println("Available commands:");
        for (Command command : commands.values()) {
            String line = String.format("%4s : %s", command.getKey(), command.getDescription());
            System.out.println(line);
        }
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("Input option: ");
            String key = scanner.nextLine();
            Command command = commands.get(key);
            if (command == null) {
                System.out.println("Invalid option!");
                continue;
            }
            command.execute();
        }
    }
}
