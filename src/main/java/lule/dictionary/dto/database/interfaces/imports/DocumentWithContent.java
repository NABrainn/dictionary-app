package lule.dictionary.dto.database.interfaces.imports;

import lule.dictionary.dto.database.interfaces.imports.base.Document;

public interface DocumentWithContent extends Document {
    String pageContent();
}
