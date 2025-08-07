@import gg.jte.support.ForSupport
@import lule.dictionary.enumeration.Familiarity
@import lule.dictionary.service.imports.importService.dto.Phrase
@import java.util.List

@param Phrase phrase
@param String targetWords
@param List<String> familiarities
@param int selectableId
@param int documentId

!{var familiarityEnums = familiarities.stream().map(familiarity -> switch (familiarity) {
        case "recognized" -> Familiarity.RECOGNIZED;
        case "familiar" -> Familiarity.FAMILIAR;
        case "known" -> Familiarity.KNOWN;
        case "ignored" -> Familiarity.IGNORED;
        default -> Familiarity.UNKNOWN;
    })
    .toList();
}
!{var bgColor = "";}
!{var textColor = "";}
    @for(var wordEntry : ForSupport.of(targetWords.split(" ")))
        !{bgColor = switch (familiarityEnums.get(wordEntry.getIndex())){
            case UNKNOWN -> "bg-accent";
            case RECOGNIZED -> "bg-accent/80";
            case FAMILIAR -> "bg-accent/60";
            default -> "bg-primary";
        };}
        !{textColor = switch (familiarityEnums.get(wordEntry.getIndex())){
            case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
            default -> "text-neutral";
        };}
        @template.document-page.content.word(
            wordId = selectableId + wordEntry.getIndex(),
            bgColor = bgColor,
            textColor = textColor,
            processedWord = wordEntry.get(),
            documentId = documentId,
            word = wordEntry.get(),
            familiarity = familiarityEnums.get(wordEntry.getIndex()),
            selectable = true
        )
    @endfor
