package lule.dictionary.documents.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.collector.service.CollectorFactory;
import lule.dictionary.documents.data.documentProcessing.*;
import lule.dictionary.documents.data.parseDocument.ParseDocument;
import lule.dictionary.documents.data.parseDocument.ParseWithPhraseSelection;
import lule.dictionary.documents.data.parseDocument.ParseWithWordSelection;
import lule.dictionary.documents.data.parseDocument.ParseWithoutSelection;
import lule.dictionary.documents.data.request.ProcessDocumentRequest;
import lule.dictionary.jsoup.data.Token;
import lule.dictionary.language.service.Language;
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.stringUtil.service.StringUtils;
import lule.dictionary.translations.data.Translation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DocumentProcessor {

    private final StringUtils stringUtils;
    private final PatternService patternService;
    private final CollectorFactory collectorFactory;

    public String write(String documentContent) {
        String[] documentAsArray = documentContent.split("(?<=\\n)(?=\\w)");
        return Arrays.stream(documentAsArray)
                .map(tokenBlob -> Token.of(tokenBlob, stringUtils.getCharQuantity(tokenBlob, '\n')))
                .map(token -> switch (token.newlineCount()) {
                    case 0 -> token;
                    case 1 -> token.withContent(patternService.replaceNewline(token.content(), " "));
                    default -> token.withContent(patternService.replaceAllNewlines(token.content(), '\n', 30));
                })
                .map(Token::content)
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.joining());
    }

    public List<DocumentUnit> parse(@NonNull ParseDocument parseDocument) {
        Phrases phrases = parseDocument.translationInfo().phrases();
        Map<String, Translation> translations = parseDocument.translationInfo().translations();

        Language sourceLanguage = parseDocument.ownerInfo().sourceLanguage();
        Language targetLanguage = parseDocument.ownerInfo().targetLanguage();
        String owner = parseDocument.ownerInfo().owner();

        return Stream.of(parseDocument.contentBlob().split(" "))
                .flatMap(word -> Arrays.stream(word.split("(?<=\\n)(?=\\w)")))
                .map(word -> switch (translations.get(stringUtils.normalize(word))) {
                    case Translation translation -> TranslationWordUnit.of(translation, word, phrases.containsWord(translation.processedTargetWord()));
                    case null -> (WordUnit) NonTranslationWordUnit.of(
                            Translation.nonTranslation(stringUtils.normalize(word), sourceLanguage, targetLanguage, owner),
                            word,
                            phrases.containsWord(stringUtils.normalize(word))
                    );
                })
                .filter(unit -> unit.rawText().length() <= 50)
                .filter(Predicate.not(unit -> unit.translation().processedTargetWord().isBlank()))
                .collect(switch (parseDocument){
                    case ParseWithPhraseSelection parseWithPhraseSelection -> collectorFactory.toDocumentUnits(phrases, parseWithPhraseSelection.selectedPhraseInfo());
                    case ParseWithWordSelection parseWithWordSelection -> collectorFactory.toDocumentUnits(phrases, parseWithWordSelection.selectedWordInfo());
                    case ParseWithoutSelection ignored -> collectorFactory.toDocumentUnits(phrases);
                })
                .documentUnits();
    }

    public List<Paragraph> asParagraphs(@NonNull List<DocumentUnit> units) {
        return units.stream()
                .collect(collectorFactory.toParagraphs())
                .paragraphs();
    }
}
