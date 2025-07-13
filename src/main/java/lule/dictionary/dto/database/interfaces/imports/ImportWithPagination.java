package lule.dictionary.dto.database.interfaces.imports;

import lule.dictionary.dto.database.interfaces.imports.base.Import;

public interface ImportWithPagination extends Import {
    String pageContent();
}
