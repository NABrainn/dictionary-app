@import lule.dictionary.service.dto.ServiceResult

@param ServiceResult result = null

@template.layout.base(content = @`
    <form
            hx-post="/auth/login"
            hx-target="#main"
            hx-swap="outerHTML"
            class="flex flex-col bg-secondary w-2/5 p-8 mx-auto gap-4">
        <span class="text-accent text-2xl font-bold">Log in</span>
        <div class="flex flex-col">
            <label class="text-accent" for="login">Username</label>
            <input class="border border-2 border-primary" type="text" name="login" id="login">
        </div>
        <div class="flex flex-col">
            <label class="text-accent" for="password">Password</label>
            <input class="border border-2 border-primary" type="text" name="password" id="password">
        </div>
        <div class="flex gap-4 items-center">
            <button type="submit" class="text-left px-2 py-1 bg-tertiary text-neutral rounded cursor-pointer">Submit</button>
            @if(result != null)
                <span class="grow-1">${result.message()}</span>
            @else
                <span class="grow-1"></span>
            @endif
            <a
            class="cursor-pointer underline text-tertiary font-semibold"
            hx-get="/auth/signup"
            hx-target="#main"
            hx-swap="outerHTML"
            hx-push-url="true">Create account</a>
        </div>
    </form>
`)

