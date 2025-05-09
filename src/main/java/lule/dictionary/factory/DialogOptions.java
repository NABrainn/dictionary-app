package lule.dictionary.factory;

import lule.dictionary.command.Command;
import lule.dictionary.dto.DialogOption;

public class DialogOptions {
    public static DialogOption create(String text, String input, Command command) {
        return new DialogOption(text, input, command);
    }
}
