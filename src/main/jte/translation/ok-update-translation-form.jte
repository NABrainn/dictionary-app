@import lule.dictionary.controllerAdvice.data.user.User
@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.TranslationAttribute
@import java.util.Map

@param User user
@param TranslationAttribute attribute
@param Map<String, String> errors
@param Map<TranslationLocalizationKey, String> messages
<div
hx-on:click="event.stopPropagation()"
_="
on load send updateFamiliarities
on updateFamiliarities
set textColors to {
    unknown: 'text-primary',
    recognized: 'text-primary',
    familiar: 'text-primary',
    known: 'text-neutral',
    ignored: 'text-neutral'
}
then set bgColors to {
    unknown: 'bg-accent',
    recognized: 'bg-accent/80',
    familiar: 'bg-accent/60',
    known: 'bg-primary',
    ignored: 'bg-primary'
}
!{String targetWord = attribute.translation().processedTargetWord();}
!{String familiarity = attribute.translation().familiarity().name().toLowerCase();}
!{int selectedUnitId = attribute.id();}
if '${targetWord}'.split(' ').length is less than 2
    then set allUnits to <span[data-text='${targetWord}']/>
    then set selectedUnit to <span#word-${selectedUnitId}[data-text='${targetWord}']/>
    then remove .unknown .recognized .familiar .known .ignored from allUnits
    then remove .{'bg-accent'} .{'bg-accent/80'} .{'bg-accent/60'} .{'bg-primary'} .{'text-primary'} .{'text-neutral'} from allUnits
    then add .${familiarity} to allUnits
    then add .{textColors.${familiarity}} to allUnits
    then add .{bgColors.${familiarity}} to allUnits
    then remove .{'bg-accent'} .{'bg-accent/80'} .{'bg-accent/60'} .{'bg-primary'} .{'text-primary'} .{'text-neutral'} from selectedUnit
    then add .text-neutral .bg-tertiary to selectedUnit
else
    set allPhrases to <div[data-text='${targetWord}']/>
    then set selectedPhrase to <div#phrase-${selectedUnitId}[data-text='${targetWord}']/>
    repeat for phrase in allPhrases
        remove .pointer-events-none from phrase
        remove .unknown .recognized .familiar .known .ignored from phrase
        then remove .{'bg-accent'} .{'bg-accent/80'} .{'bg-accent/60'} .{'bg-primary'} .{'text-primary'} .{'text-neutral'} from phrase
        then add .{textColors.${familiarity}} to phrase
        then add .{bgColors.${familiarity}} to phrase
    end
    then remove .unknown .recognized .familiar .known .ignored from selectedPhrase
    then remove .{'bg-accent'} .{'bg-accent/80'} .{'bg-accent/60'} .{'bg-primary'} .{'text-primary'} .{'text-neutral'} from selectedPhrase
    then add .text-neutral to <span.phrase-part/> in selectedPhrase
    then add .text-neutral to <div.phrase-part/> in selectedPhrase
    then add .pointer-events-none to selectedPhrase
end

"
class="
md:gap-1
gap-2 bg-tertiary flex flex-col p-3 rounded"
id="translation-form">
    <div
    class="flex justify-end">
        <span
        class="md:text-md
        text-sm text-red-400 font-bold cursor-pointer"
        _="on click remove <div#translation-form/>">❌</span>
    </div>
    <span
    class="font-bold text-lg text-accent text-wrap">${attribute.translation().processedTargetWord()}</span>
    <div
    class="flex flex-col gap-2 text-wrap">
        @for(String word : attribute.translation().sourceWords())
            <div
            class="flex gap-2 items-center min-h-8">
                <input
                name="sourceWords"
                type="hidden"
                value="${word}">
                <span class="block text-accent text-wrap">${word}</span>
                <form
                class="flex items-center"
                hx-delete="/translations/sourceWords"
                hx-target="#form-container-${attribute.id()}"
                hx-swap="innerHTML"
                hx-headers='{
                    "X-XSRF-TOKEN": "${user._csrf()}"
                }'
                hx-params="sourceWord,targetWord,id"
                hx-vals='{
                    "targetWord": "${attribute.translation().processedTargetWord()}",
                    "id": "${attribute.id()}",
                    "sourceWord": "${word}"
                }'>
                    <button
                    class="cursor-pointer text-md block"
                    type="submit">🗑️</button>
                </form>
            </div>
        @endfor
        <div class="flex flex-col gap-2">
            <div id="source-word-form" class="flex">
                <form
                class="flex flex-col gap-2"
                hx-headers='{
                    "X-XSRF-TOKEN": "${user._csrf()}"
                }'
                hx-params="id,targetWord,familiarity,sourceWords"
                hx-vals='{
                    "targetWord": "${attribute.translation().processedTargetWord()}",
                    "familiarity": "${attribute.translation().familiarity()}",
                    "id": "${attribute.id()}"
                }'
                hx-put="/translations/sourceWords/update"
                hx-swap="outerHTML"
                hx-target="#translation-form">
                    @for(String word : attribute.translation().sourceWords())
                        <input
                        name="sourceWords"
                        type="hidden"
                        value="${word}">
                    @endfor
                    <div class="flex gap-2">
                        <label
                        for="sw-input"
                        class="hidden"></label>
                        <input
                        id="sw-input"
                        class="
                        md:max-w-50
                        max-w-40 outline-none border border-2 ${errors.get("sourceWord") != null ? "border-error" : "border-primary"} rounded text-neutral text-md grow-1
                        focus:border-accent"
                        type="text"
                        placeholder=""
                        name="sourceWords">
                        <button
                        id="submit-source-word"
                        class="text-3xl cursor-pointer text-accent"
                        type="submit">+</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="
    md:flex md:flex-col md:gap-0.25
    flex flex-row justify-center gap-3">
        @for(var level : attribute.familiarityLevels().entrySet())
            @if(level.getKey() == attribute.currentFamiliarity())
                <div
                class="
                md:bg-tertiary md:text-primary md:justify-start md:gap-1
                bg-primary text-accent justify-center size-6 rounded flex font-bold items-center">
                    <span>${level.getKey()}<span class="md:inline hidden">: </span></span>
                    <span class="
                    md:inline
                    hidden">${messages.get(TranslationLocalizationKey.valueOf(level.getValue().name().toUpperCase()))}</span>
                </div>
            @else
                <form
                class="
                md:bg-tertiary md:text-accent md:cursor-normal md:justify-start md:gap-1
                bg-accent text-primary cursor-pointer justify-center flex font-bold rounded size-6  items-center
                hover:text-primary"
                id="translation-form-${attribute.id()}"
                hx-trigger="click"
                hx-put="/translations/familiarity/update"
                hx-headers='{
                    "X-XSRF-TOKEN": "${user._csrf()}"
                }'
                hx-params="targetWord,familiarity,sourceLanguage,targetLanguage,id,isPhrase"
                hx-vals='
                {
                    "targetWord": "${attribute.translation().processedTargetWord().toLowerCase()}",
                    "familiarity": "${level.getValue().name()}",
                    "sourceLanguage": "${attribute.translation().ownerInfo().sourceLanguage().name()}",
                    "targetLanguage": "${attribute.translation().ownerInfo().targetLanguage().name()}",
                    "id": "${attribute.id()}",
                    "isPhrase": "${attribute.isPhrase()}"
                }'
                hx-swap="outerHTML"
                hx-target="#translation-form">
                    <span>${level.getKey()}<span class="md:inline hidden">: </span></span>
                    <input
                    class="
                    md:inline
                    hidden cursor-pointer"
                    value="${messages.get(TranslationLocalizationKey.valueOf(level.getValue().name().toUpperCase()))}"
                    type="submit">
                </form>
            @endif
        @endfor
    </div>
</div>
