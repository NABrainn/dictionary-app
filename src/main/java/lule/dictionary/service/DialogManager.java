package lule.dictionary.service;

import lule.dictionary.command.Command;
import lule.dictionary.dto.Dialog;
import lule.dictionary.dto.DialogOption;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DialogManager {

    private final Scanner scanner = new Scanner(System.in);
    private final Deque<Dialog> dialogHistory = new ArrayDeque<>();

    public Command executeDialog(Dialog dialog) {
        return () -> {
            dialogHistory.push(dialog);
            finalize(dialog);
        };
    }

    public Command printTranslations() {
        return null;
    }

    public Command retryDialog() {
        return () -> {
            Dialog previous = dialogHistory.peek();
            System.out.println(dialogHistory.size());
            if(previous != null) finalize(previous);
        };
    }

    public Command openPrevious() {
        return () -> {
            dialogHistory.pop();
            Dialog previous = dialogHistory.peek();
            if(previous != null) finalize(previous);
        };
    }

    private void finalize(Dialog dialog) {
        dialog.runner().execute();
        dialog.options().forEach(dialogOption -> System.out.println(dialogOption.text() + ": [" + dialogOption.input() + "]"));
        while(true) {
            String input = scanner.nextLine();
            for(DialogOption option : dialog.options()) {
                if(option.input().equals(input)) {
                    option.command().execute();
                    return;
                }
            }
            System.out.println("Incorrect input. Try the following: " + dialog.options().stream().map(DialogOption::input));
        }
    }

    public Command terminate(Dialog runningProfileSettings) {
        return () -> {
            System.exit(0);
        };
    }
}
