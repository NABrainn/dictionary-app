package lule.dictionary.documents.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.collector.service.CollectorFactory;
import lule.dictionary.documents.data.documentProcessing.*;
import lule.dictionary.documents.data.request.ProcessDocumentRequest;
import lule.dictionary.stringUtil.service.StringUtils;
import lule.dictionary.translations.data.Translation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentProcessor {

    private final StringUtils stringUtils;
    private final CollectorFactory collectorFactory;

    public List<DocumentUnit> read(@NonNull ProcessDocumentRequest request) {
        return Arrays.stream(request.content().split("\\s+"))
                .sequential()
                .map(word -> switch (request.translations().get(stringUtils.normalize(word))) {
                    case Translation translation -> TranslationWordUnit.of(translation, word, request.phrases().containsWord(translation.processedTargetWord()));
                    case null -> (NonPhraseUnit) NonTranslationWordUnit.of(
                            Translation.nonTranslation(stringUtils.normalize(word), request.sourceLanguage(), request.targetLanguage(), request.owner()),
                            word,
                            request.phrases().containsWord(stringUtils.normalize(word))
                    );
                })
                .collect(collectorFactory.documentUnitCollector(request.phrases()))
                .documentUnits();
    }
}
