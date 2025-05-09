package lule.dictionary.service;

import lule.dictionary.functionalInterface.DialogManagerAction;
import lule.dictionary.dto.Dialog;
import lule.dictionary.dto.DialogOption;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;

public class DialogManager {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Deque<Dialog> dialogHistory = new ArrayDeque<>();

    public static DialogOption createOption(String text, String input, DialogManagerAction command) {
        return new DialogOption(text, input, command);
    }

    public static Dialog createDialog(DialogManagerAction runner, DialogOption... options) {
        return new Dialog(runner, options);
    }

    public static void start(Dialog dialog) {
        dialogHistory.push(dialog);
        finalize(dialog);
    }

    public static DialogManagerAction executeDialog(Dialog dialog) {
        return () -> {
            dialogHistory.push(dialog);
            finalize(dialog);
        };
    }

    public static DialogManagerAction reopenCurrent() {
        return () -> {
            Dialog previous = dialogHistory.peek();
            System.out.println(dialogHistory.size());
            if(previous != null) finalize(previous);
        };
    }

    public static DialogManagerAction openPrevious() {
        return () -> {
            dialogHistory.pop();
            Dialog previous = dialogHistory.peek();
            if(previous != null) finalize(previous);
        };
    }

    private static void finalize(Dialog dialog) {
        dialog.runner().execute();
        for(DialogOption option : dialog.options()) {
            System.out.println(option.text() + ": [" + option.input() + "]");
        }
        while(true) {
            String input = scanner.nextLine();
            for(DialogOption option : dialog.options()) {
                if(option.input().equals(input)) {
                    option.command().execute();
                    return;
                }
            }
            System.out.println("Incorrect input. Try the following: " + Arrays.stream(dialog.options()).map(DialogOption::input));
        }
    }

    public static DialogManagerAction terminate() {
        return () -> {
            System.exit(0);
        };
    }
}
