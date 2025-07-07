@import lule.dictionary.service.imports.importService.dto.importsAttribute.ImportContentAttribute
@import lule.dictionary.enumeration.Familiarity
@import gg.jte.support.ForSupport
@import lule.dictionary.service.language.Language
@import lule.dictionary.service.translation.dto.TranslationAttribute
@import org.springframework.security.web.csrf.CsrfToken
@import java.util.Map

@param ImportContentAttribute importContentAttribute
@param TranslationAttribute translationAttribute = null
@param CsrfToken _csrf
@param boolean authenticated
@param String username
@param String targetLanguage
@param int wordsLearned
@param int dailyStreak
@param Map<Language, String> languages

<div>
    <div
    id="page-content"
    class="flex flex-col gap-8 min-h-full justify-between">
        @template.navbar(
            username = username,
            authenticated = authenticated,
            targetLanguage = targetLanguage,
            wordsLearned = wordsLearned,
            dailyStreak = dailyStreak,
            languages = languages
        )
        <div class=" w-3/5 mx-auto">
            <span class="text-neutral font-bold text-4xl">${importContentAttribute.importData().title()}</span>
            <div class="leading-10 min-h-150">
                @for(var word : ForSupport.of(importContentAttribute.importData().content()))
                    !{var cleanWord = word.get().replaceAll("[^\\p{L}\\p{N}]", "").trim().toLowerCase();}
                    @if(importContentAttribute.importData().translations().get(cleanWord) != null)
                        @if(importContentAttribute.importData().selectedWordId() == word.getIndex())
                            @if(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(), processedWord = cleanWord,
                                    bgColor = "bg-tertiary bg-accent",
                                    textColor = "text-neutral",
                                    translationAttribute = translationAttribute,
                                    page = importContentAttribute.paginationData().currentPageNumber(),
                                    found = true
                                )
                            @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-tertiary bg-accent/80",
                                    textColor = "text-neutral",
                                    translationAttribute = translationAttribute,
                                    page = importContentAttribute.paginationData().currentPageNumber(),
                                    found = true
                                )

                            @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-tertiary bg-accent/60",
                                    textColor = "text-neutral",
                                    translationAttribute = translationAttribute,
                                    page = importContentAttribute.paginationData().currentPageNumber(),
                                    found = true
                                )
                            @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-tertiary bg-primary",
                                    textColor = "text-neutral",
                                    translationAttribute = translationAttribute,
                                    page = importContentAttribute.paginationData().currentPageNumber(),
                                    found = true
                                )
                            @else
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-tertiary bg-primary",
                                    textColor = "text-neutral",
                                    translationAttribute = translationAttribute,
                                    page = importContentAttribute.paginationData().currentPageNumber(),
                                    found = true
                                )
                            @endif
                        @else
                            @if(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-accent",
                                    textColor = "text-primary",
                                    translationAttribute = null,
                                    page = importContentAttribute.paginationData().currentPageNumber(),
                                    found = true
                                )
                            @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-accent/80",
                                    textColor = "text-primary",
                                    translationAttribute = null,
                                    page = importContentAttribute.paginationData().currentPageNumber(),
                                    found = true
                                )
                            @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-accent/60",
                                    textColor = "text-primary",
                                    translationAttribute = null,
                                    page = importContentAttribute.paginationData().currentPageNumber(),
                                    found = true
                                )
                            @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-primary",
                                    textColor = "text-neutral",
                                    translationAttribute = null,
                                    page = importContentAttribute.paginationData().currentPageNumber(),
                                    found = true
                                )
                            @else
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-primary",
                                    textColor = "text-neutral",
                                    translationAttribute = null,
                                    page = importContentAttribute.paginationData().currentPageNumber(),
                                    found = true
                                )
                            @endif
                        @endif
                    @else
                        @if(importContentAttribute.importData().selectedWordId() == word.getIndex())
                            @template.import-page.word(
                                wordIndex = word.getIndex(),
                                importId = importContentAttribute.importData().importId(),
                                word = word.get(),
                                processedWord = cleanWord,
                                bgColor = "bg-tertiary",
                                textColor = "text-neutral",
                                translationAttribute = null,
                                page = importContentAttribute.paginationData().currentPageNumber()
                            )
                        @else
                            @template.import-page.word(
                                wordIndex = word.getIndex(),
                                importId = importContentAttribute.importData().importId(),
                                word = word.get(),
                                processedWord = cleanWord,
                                bgColor = "bg-accent",
                                textColor = "text-primary",
                                translationAttribute = null,
                                page = importContentAttribute.paginationData().currentPageNumber()
                            )
                        @endif
                    @endif
                @endfor
            </div>
        </div>
        <div class="flex gap-4 justify-center max-w-2/5 mx-auto items-center">
            @template.import-page.pagination.paginator(
                id = importContentAttribute.importData().importId(),
                currentPage = importContentAttribute.paginationData().currentPageNumber(),
                pages = importContentAttribute.paginationData().numberOfPages(),
                importId = importContentAttribute.importData().importId(),
                rows = importContentAttribute.paginationData().rows(),
                currentRow = importContentAttribute.paginationData().currentRowNumber(),
                firstPageOfRow = importContentAttribute.paginationData().firstPageOfRowNumber()
            )
        </div>
    </div>
</div>