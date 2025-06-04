@param String token

@template.layout.base(content = @`
    <div class="bg-primary min-h-4/5 w-4/5 mx-auto flex flex-col gap-8 p-4" id="imports">
        <form hx-post="/imports/submit">
            <label for="title">Title</label>
            <input type="text" id="title" name="title">
            <button type="submit">Submit</button>
        </form>
    </div>
`)