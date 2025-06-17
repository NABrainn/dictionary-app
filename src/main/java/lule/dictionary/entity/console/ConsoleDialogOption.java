package lule.dictionary.entity.console;

import lule.dictionary.functionalInterface.ConsoleDialogAction;

public record ConsoleDialogOption(
        String text,
        String input,
        ConsoleDialogAction command) {
}
