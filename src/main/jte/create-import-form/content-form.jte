@import lule.dictionary.dto.application.result.ServiceResultImp
@import java.util.Map

@param ServiceResultImp<?> result
@param Map<String, String> importFormLocalization


<div class="flex flex-col" id="content">
    <input type="hidden" name="url">
    <div class="w-full">
        <label for="area"></label>
        <textarea
        class="
        md:min-h-120
        min-h-85 outline-none border border-2 border-primary rounded text-neutral resize-none
        focus:border-accent w-full"
        name="content"
        id="area"
        placeholder="${importFormLocalization.get("space_for_content")}"></textarea>
        @if(result != null)
            @if(result.hasError() && result.messages().get("pageContent") != null)
                <span class="text-error font-semibold text-sm">${result.messages().get("pageContent")}</span>
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        @else
            <span class="invisible text-sm">fallback</span>
        @endif
    </div>
</div>

