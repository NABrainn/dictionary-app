package lule.dictionary.service.imports.dto.importData;

import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.translation.Translation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ImportDataFactory {

    public ImportData of(@NonNull String title,
                         @NonNull List<String> content,
                         @NonNull Map<String, Translation> translations,
                         int importId,
                         int selectedWordId) {
        return new ImportData(title, content, translations, importId, selectedWordId);
    }
}
