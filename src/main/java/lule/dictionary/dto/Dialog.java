package lule.dictionary.dto;

import lule.dictionary.functionalInterface.DialogManagerAction;


public record Dialog(DialogManagerAction runner, DialogOption... options) {
}
