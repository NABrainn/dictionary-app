package lule.dictionary.factory;

import lule.dictionary.command.DialogRunner;
import lule.dictionary.dto.Dialog;
import lule.dictionary.dto.DialogOption;

import java.util.List;

public class Dialogs {
    public static Dialog create(DialogRunner runner, List<DialogOption> options) {
        return new Dialog(runner, options);
    }
}
