package lule.dictionary.dto;

import lule.dictionary.command.DialogManagerAction;


public record Dialog(DialogManagerAction runner, DialogOption... options) {
}
