package lule.dictionary.dto;

import lule.dictionary.functionalInterface.ConsoleDialogAction;


public record ConsoleDialog(ConsoleDialogAction runner, ConsoleDialogOption... options) {
}
