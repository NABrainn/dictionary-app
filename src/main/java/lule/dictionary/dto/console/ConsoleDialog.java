package lule.dictionary.dto.console;

import lule.dictionary.functionalInterface.ConsoleDialogAction;


public record ConsoleDialog(ConsoleDialogAction runner, ConsoleDialogOption... options) {
}
