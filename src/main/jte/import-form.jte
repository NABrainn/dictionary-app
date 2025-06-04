@import lule.dictionary.dto.web.ImportForm

@param ImportForm form

@template.layout.base(content = @`
    <form
    id="form"
    class="grow-1 bg-secondary flex flex-col justify-between gap-8 p-8 rounded"
    hx-post="/imports/submit"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="/imports">
        <div class="flex flex-col justify-between gap-8">
            <div class="flex flex-col">
                <label for="title">Title</label>
                <input type="text" id="title" name="title" value="${form.title()}"
                class="
                border border-neutral border-1
                focus:outline-none focus:bg-tertiary">
            </div>
            <div class="flex flex-col">
                <label for="url">Url</label>
                <input type="text" id="url" name="url" value="${form.url()}"
                class="
                border border-neutral border-1
                focus:outline-none focus:bg-tertiary">
            </div>
            <div class="flex flex-col">
                <label for="content">Content</label>
                <textarea id="content" name="content" content="${form.content()}"
                class="
                border border-neutral border-1 resize-none min-h-200
                focus:outline-none focus:bg-tertiary"></textarea>
            </div>
        </div>
        <div class="flex flex-row-reverse">
            <button type="submit"
                    class="bg-accent text-neutral py-1 px-1.5 rounded cursor-pointer">Submit</button>
        </div>
    </form>
`)