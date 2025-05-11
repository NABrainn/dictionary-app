package lule.dictionary.dto;

import lule.dictionary.functionalInterface.ConsoleDialogAction;

public record ConsoleDialogOption(
        String text,
        String input,
        ConsoleDialogAction command) {
}
