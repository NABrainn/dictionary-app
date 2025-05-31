package lule.dictionary.dto.application.interfaces.imports;

import java.util.List;

public interface ImportDetails {
    String title();
    String content();
    String url();
    List<String> importDetailsSummary();
}
