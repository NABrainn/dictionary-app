package lule.dictionary.entity.console;

import lule.dictionary.functionalInterface.ConsoleDialogAction;


public record ConsoleDialog(ConsoleDialogAction runner, ConsoleDialogOption... options) {
}
