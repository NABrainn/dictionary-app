@import lule.dictionary.service.imports.importPageService.dto.ImportModel
@import lule.dictionary.enumeration.Familiarity
@import gg.jte.support.ForSupport
@import lule.dictionary.service.translation.dto.TranslationModel
@import org.springframework.security.web.csrf.CsrfToken

@param ImportModel importModel
@param TranslationModel translationModel = null
@param CsrfToken _csrf
@param boolean authenticated

<div
id="page-content"
class="flex flex-col gap-8 w-3/5 min-h-full justify-between mx-auto">
    <span class="text-neutral font-bold text-4xl">${importModel.title()}</span>
    <div class="leading-10 min-h-150">
        @for(var word : ForSupport.of(importModel.content()))
            !{var cleanWord = word.get().replaceAll("[^\\p{L}\\p{N}]", "").trim().toLowerCase();}
            @if(importModel.translations().get(cleanWord) != null)
                @if(importModel.selectedWordId() == word.getIndex())
                    @if(importModel.translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary bg-accent", textColor = "text-neutral", translationModel = translationModel, page = importModel.currentPage())

                    @elseif(importModel.translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary bg-accent/80", textColor = "text-neutral", translationModel = translationModel, page = importModel.currentPage())

                    @elseif(importModel.translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary bg-accent/60", textColor = "text-neutral", translationModel = translationModel, page = importModel.currentPage())

                    @elseif(importModel.translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary bg-primary", textColor = "text-neutral", translationModel = translationModel, page = importModel.currentPage())

                    @else
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary bg-primary", textColor = "text-neutral", translationModel = translationModel, page = importModel.currentPage())

                    @endif
                @else
                    @if(importModel.translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-accent", textColor = "text-primary", page = importModel.currentPage())

                    @elseif(importModel.translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-accent/80", textColor = "text-primary", page = importModel.currentPage())

                    @elseif(importModel.translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-accent/60", textColor = "text-primary", page = importModel.currentPage())

                    @elseif(importModel.translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-primary", textColor = "text-neutral", page = importModel.currentPage())

                    @else
                        @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-primary", textColor = "text-neutral", page = importModel.currentPage())


                    @endif
                @endif
            @else
                @if(importModel.selectedWordId() == word.getIndex())
                    @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-tertiary", textColor = "text-neutral", page = importModel.currentPage())
                @else
                    @template.import-page.word(wordIndex = word.getIndex(), importId = importModel.importId(), word = word.get(), processedWord = cleanWord, bgColor = "bg-primary", textColor = "text-neutral", page = importModel.currentPage())

                @endif
            @endif
        @endfor
    </div>
    <div class="flex gap-4 justify-center max-w-2/5 mx-auto items-center">
        @template.import-page.pagination.paginator(
        id = importModel.importId(),
        currentPage = importModel.currentPage(),
        pages = importModel.pages(),
        importId = importModel.importId(),
        rows = importModel.rows(),
        currentRow = importModel.currentRow(),
        firstPageOfRow = importModel.firstPageOfRow())
    </div>
</div>
