package lule.dictionary.util;

import lule.dictionary.functionalInterface.ConsoleDialogAction;
import lule.dictionary.entity.console.ConsoleDialog;
import lule.dictionary.entity.console.ConsoleDialogOption;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;

public class ConsoleDialogManager {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Deque<ConsoleDialog> dialogHistory = new ArrayDeque<>();

    public static ConsoleDialogOption createOption(String text, String input, ConsoleDialogAction command) {
        return new ConsoleDialogOption(text, input, command);
    }

    public static ConsoleDialog createDialog(ConsoleDialogAction runner, ConsoleDialogOption... options) {
        return new ConsoleDialog(runner, options);
    }

    public static void start(ConsoleDialog dialog) throws IOException {
        dialogHistory.push(dialog);
        finalize(dialog);
    }

    public static ConsoleDialogAction executeDialog(ConsoleDialog dialog) {
        return () -> {
            dialogHistory.push(dialog);
            finalize(dialog);
        };
    }

    public static ConsoleDialogAction reopenCurrent() {
        return () -> {
            ConsoleDialog previous = dialogHistory.peek();
            System.out.println(dialogHistory.size());
            if(previous != null) finalize(previous);
        };
    }

    public static ConsoleDialogAction openPrevious() {
        return () -> {
            dialogHistory.pop();
            ConsoleDialog previous = dialogHistory.peek();
            if(previous != null) finalize(previous);
        };
    }

    private static void finalize(ConsoleDialog dialog) {
        dialog.runner().execute();
        System.out.println("Enter keys in the brackets to navigate");
        for(ConsoleDialogOption option : dialog.options()) {
            System.out.println(option.text() + ": [" + option.input() + "]");
        }
        while(true) {
            String input = scanner.nextLine();
            for(ConsoleDialogOption option : dialog.options()) {
                if(option.input().equals(input)) {
                    option.command().execute();
                    return;
                }
            }
            System.out.print("Incorrect input. Try the following: ");
            Arrays.stream(dialog.options()).forEach(option -> System.out.print("[" + option.input() + "] "));
            System.out.println();
        }
    }

    public static ConsoleDialogAction terminate() {
        return () -> {
            System.exit(0);
        };
    }
}
