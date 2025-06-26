@import lule.dictionary.service.dto.ServiceResult

@param ServiceResult result

<div class="flex flex-col" id="content">
    <input type="hidden" name="content">
    <div class="w-full">
        <label for="url"></label>
        <input
        class="
        outline-none border border-2 border-primary rounded text-neutral
        focus:border-accent w-full"
        name="url"
        id="url"
        placeholder="Type in URL here...">
        @if(result != null)
            @if(result.error() && result.errors().get("url") != null)
                <span class="text-error font-semibold text-sm">${result.errors().get("url")}</span>
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        @else
            <span class="invisible text-sm">fallback</span>
        @endif
    </div>
</div>