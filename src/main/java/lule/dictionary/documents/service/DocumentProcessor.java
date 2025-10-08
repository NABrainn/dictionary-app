package lule.dictionary.documents.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.documents.data.documentProcessing.*;
import lule.dictionary.documents.data.parseDocument.ParseDocument;
import lule.dictionary.documents.data.parseDocument.ParseWithPhraseSelection;
import lule.dictionary.documents.data.parseDocument.ParseWithWordSelection;
import lule.dictionary.documents.data.parseDocument.ParseWithoutSelection;
import lule.dictionary.jsoup.data.Token;
import lule.dictionary.language.service.Language;
import lule.dictionary.stringUtil.service.PatternService;
import lule.dictionary.stringUtil.service.StringUtils;
import lule.dictionary.translations.data.entity.Translation;
import lule.dictionary.translations.data.entity.UninitializedWordTranslation;
import lule.dictionary.userProfiles.data.OwnerInfo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DocumentProcessor {

    private final StringUtils stringUtils;
    private final PatternService patternService;
    private final DocumentUnitCollectors collectors;
    private final DocumentUnitGatherers gatherers;

    public String write(String documentContent) {
        String[] documentAsArray = documentContent.split("(?<=\\n)(?=\\w)");
        return Arrays.stream(documentAsArray)
                .map(tokenBlob -> Token.of(tokenBlob, stringUtils.getCharQuantity(tokenBlob, '\n')))
                .map(token -> switch (token.newlineCount()) {
                    case 0 -> token;
                    case 1, 2, 3 -> token.withContent(patternService.replaceNewline(token.content(), " "));
                    default -> token.withContent(patternService.replaceAllNewlines(token.content(), '\n', 30));
                })
                .map(Token::content)
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.joining());
    }

    public List<DocumentUnit> read(@NonNull ParseDocument parseDocument) {
        Phrases phrases = parseDocument.translationInfo().phrases();
        Map<String, Translation> translations = parseDocument.translationInfo().translations();

        Language sourceLanguage = parseDocument.ownerInfo().sourceLanguage();
        Language targetLanguage = parseDocument.ownerInfo().targetLanguage();
        String owner = parseDocument.ownerInfo().owner();

        AtomicInteger idStore = new AtomicInteger(0);
        Stream<String> contentStream = Stream.of(parseDocument.contentBlob().split(" "))
                .flatMap(rawWord -> rawWord.contains("\n") ?
                        Arrays.stream((rawWord.substring(0, rawWord.lastIndexOf("\n")) + " " + rawWord.substring(rawWord.lastIndexOf("\n"))).split(" ")) :
                        Stream.of(rawWord));
        final List<DocumentUnit> documentUnits = contentStream
                .filter(rawWord -> rawWord.length() <= 50)
                .filter(rawWord -> !rawWord.isBlank())
                .map(rawWord -> switch (translations.get(stringUtils.normalize(rawWord))) {
                    case Translation persistedWord -> PersistedWordUnit.of(
                            idStore.getAndIncrement(),
                            persistedWord,
                            rawWord,
                            phrases.containsWord(persistedWord.processedTargetWord())
                    );
                    case null -> (WordUnit) (stringUtils.normalize(rawWord).isBlank() ?
                            InvalidWordUnit.of(idStore.getAndIncrement(), rawWord) :
                            NewWordUnit.of(
                                    idStore.getAndIncrement(),
                                    UninitializedWordTranslation.of(stringUtils.normalize(rawWord), OwnerInfo.of(sourceLanguage, targetLanguage, owner)),
                                    rawWord,
                                    phrases.containsWord(stringUtils.normalize(rawWord))
                            ));
                })
                .gather(gatherers.gatherDocumentUnits(phrases))
                .toList();
        return switch (parseDocument) {
            case ParseWithPhraseSelection parseWithPhraseSelection -> documentUnits.stream()
                    .anyMatch(unit -> unit.translation().processedTargetWord().equals(parseWithPhraseSelection.selectedPhraseInfo().phraseText()))
                    ?
                    documentUnits.stream()
                            .collect(collectors.toExistingPhraseSelection(parseWithPhraseSelection.selectedPhraseInfo()))
                            .units()
                    :
                    documentUnits.stream()
                            .collect(collectors.toNewPhraseSelection(parseWithPhraseSelection.selectedPhraseInfo()))
                            .units();
            case ParseWithWordSelection parseWithWordSelection -> documentUnits
                    .stream()
                    .collect(collectors.toWordSelection(parseWithWordSelection.selectedWordInfo()))
                    .units();
            case ParseWithoutSelection ignored -> documentUnits;
        };
    }

    public List<Paragraph> asParagraphs(@NonNull List<DocumentUnit> units) {
        return units.stream()
                .collect(collectors.toParagraphs())
                .paragraphs();
    }
}
