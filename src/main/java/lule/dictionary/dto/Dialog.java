package lule.dictionary.dto;

import lule.dictionary.command.DialogRunner;

import java.util.List;

public record Dialog(DialogRunner runner, List<DialogOption> options) {
}
