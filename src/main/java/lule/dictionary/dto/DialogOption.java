package lule.dictionary.dto;

import lule.dictionary.command.DialogManagerAction;

public record DialogOption(
        String text,
        String input,
        DialogManagerAction command) {
}
