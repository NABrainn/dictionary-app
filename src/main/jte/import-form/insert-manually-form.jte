@import lule.dictionary.service.dto.ServiceResult

@param ServiceResult result

<div class="flex flex-col" id="content">
    <input type="hidden" name="url">
    <div class="w-full">
        <label for="area"></label>
        <textarea
        class="
        outline-none border border-2 border-primary rounded text-neutral min-h-30 resize-none min-h-200
        focus:border-accent w-full"
        name="content"
        id="area"
        placeholder="Type in content here..."></textarea>
        @if(result != null)
            @if(result.error() && result.errors().get("content") != null)
                <span class="text-error font-semibold text-sm">${result.errors().get("content")}</span>
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        @else
            <span class="invisible text-sm">fallback</span>
        @endif
    </div>
</div>

