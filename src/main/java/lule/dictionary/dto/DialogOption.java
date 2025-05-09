package lule.dictionary.dto;

import lule.dictionary.command.Command;

public record DialogOption(
        String text,
        String input,
        Command command) {
}
