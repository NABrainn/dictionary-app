@param String spaceForUrlText
@param String message = ""

<div class="flex flex-col" id="content">
    <input type="hidden" name="content">
    <input type="hidden" name="strategy" value="url_submit">
    <div class="w-full">
        <label for="url"></label>
        <textarea
        maxlength="450"
        class="
        outline-none overflow-none border border-2 border-primary rounded text-neutral min-h-20 resize-none
        focus:border-accent w-full"
        name="url"
        id="url"
        placeholder="${spaceForUrlText}"></textarea>
        <textfield-input></textfield-input>
        @if(!message.isBlank())
            <span class="text-error font-bold">${message}</span>
        @endif
    </div>
</div>