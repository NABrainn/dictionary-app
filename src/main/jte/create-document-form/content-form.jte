@param String contentPlaceholderText
@param String content = ""
@param String violation = ""

<div class="flex flex-col" id="content">
    <input type="hidden" name="url">
    <input type="hidden" name="strategy" value="content_submit">
    <div class="w-full">
        <label for="area"></label>
        <textarea
        class="
        md:min-h-110
        min-h-85 outline-none border border-2 border-primary rounded text-neutral resize-none
        focus:border-accent w-full"
        name="content"
        id="area"
        placeholder="${contentPlaceholderText}">${content}</textarea>
        @if(!violation.isBlank())
            <span class="text-error font-bold">${violation}</span>
        @endif
    </div>
</div>

