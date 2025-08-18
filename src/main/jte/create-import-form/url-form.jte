@param String spaceForUrlText

<div class="flex flex-col" id="content">
    <input type="hidden" name="content">
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
        <%--ERROR MESSAGES--%>
    </div>
</div>