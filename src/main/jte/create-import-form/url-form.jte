@import lule.dictionary.dto.application.result.ServiceResultImp

@param ServiceResultImp<?> result
@param String spaceForUrlText

<div class="flex flex-col" id="content">
    <input type="hidden" name="content">
    <div class="w-full">
        <label for="url"></label>
        <input
        class="
        outline-none border border-2 border-primary rounded text-neutral min-h-8
        focus:border-accent w-full"
        name="url"
        id="url"
        placeholder="${spaceForUrlText}">
        @if(result != null)
            @if(result.hasError() && result.messages().get("url") != null)
                <span class="text-error font-semibold text-sm">${result.messages().get("url")}</span>
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        @else
            <span class="invisible text-sm">fallback</span>
        @endif
    </div>
</div>