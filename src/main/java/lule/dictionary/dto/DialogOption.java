package lule.dictionary.dto;

import lule.dictionary.functionalInterface.DialogManagerAction;

public record DialogOption(
        String text,
        String input,
        DialogManagerAction command) {
}
