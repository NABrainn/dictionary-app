@import lule.dictionary.service.imports.importPageService.dto.ImportPageModel
@import lule.dictionary.enumeration.Familiarity
@import gg.jte.support.ForSupport
@import org.springframework.security.web.csrf.CsrfToken

@param ImportPageModel importPageModel
@param CsrfToken _csrf
@param boolean authenticated

<div id="page-content" class="flex flex-col gap-8 w-3/5 mx-auto">
    <span class="text-neutral font-bold text-4xl">${importPageModel.title()}</span>
    <div class="leading-10">
        @for(var word : ForSupport.of(importPageModel.content()))
            !{var cleanWord = word.get().replaceAll("[^\\p{L}\\p{N}]", "").trim().toLowerCase();}
            @if(importPageModel.translations().get(cleanWord) != null)
                @if(importPageModel.selectedWordId() == word.getIndex())
                    @if(importPageModel.translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary bg-accent", textColor = "text-primary")
                    @elseif(importPageModel.translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary bg-accent/80", textColor = "text-primary")
                    @elseif(importPageModel.translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary bg-accent/60", textColor = "text-primary")
                    @elseif(importPageModel.translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary bg-primary", textColor = "text-neutral")
                    @else
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary bg-primary", textColor = "text-neutral")
                    @endif
                @else
                    @if(importPageModel.translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-accent", textColor = "text-primary")
                    @elseif(importPageModel.translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-accent/80", textColor = "text-primary")
                    @elseif(importPageModel.translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-accent/60", textColor = "text-primary")
                    @elseif(importPageModel.translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-primary", textColor = "text-neutral")
                    @else
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-primary", textColor = "text-neutral")

                    @endif
                @endif
            @else
                @if(importPageModel.selectedWordId() == word.getIndex())
                    @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary", textColor = "text-neutral")

                @else
                    @template.import-page.word(wordIndex = word.getIndex(), importId = importPageModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-primary", textColor = "text-neutral")
                @endif
            @endif
        @endfor
    </div>
</div>

