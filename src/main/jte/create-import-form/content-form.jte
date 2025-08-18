@param String spaceForContentText

<div class="flex flex-col" id="content">
    <input type="hidden" name="url">
    <div class="w-full">
        <label for="area"></label>
        <textarea
        class="
        md:min-h-110
        min-h-85 outline-none border border-2 border-primary rounded text-neutral resize-none
        focus:border-accent w-full"
        name="content"
        id="area"
        placeholder="${spaceForContentText}"></textarea>
        <%--ERROR MESSAGES--%>
    </div>
</div>

