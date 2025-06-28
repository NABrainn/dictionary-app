@import lule.dictionary.service.dto.ServiceResult
@import org.springframework.security.web.csrf.CsrfToken

@param ServiceResult result = null
@param CsrfToken _csrf
@param boolean authenticated

@template.layout.base(authView = @`
    <form
        hx-post="/auth/login"
        hx-target="#main"
        hx-swap="outerHTML"
        hx-push-url="true"
        class="flex flex-col bg-secondary w-2/5 p-8 mx-auto gap-8">
        <span class="text-accent text-2xl font-bold">Log in</span>
        <div class="flex flex-col">
            <label class="text-accent" for="login">Username</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="text" name="login" id="login">
            @if(result != null)
                @if(result.error() && result.errors().get("login") != null)
                    <span class="text-error font-semibold text-sm>${result.errors().get("login")}</span>
                @else
                    <span class="invisible text-sm">fallback</span>
                @endif
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        </div>
        <div class="flex flex-col">
            <label class="text-accent" for="password">Password</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="password" name="password" id="password">
            @if(result != null)
                @if(result.error() && result.errors().get("password") != null)
                    <span class="text-error font-semibold text-sm">${result.errors().get("password")}</span>
                @else
                    <span class="invisible text-sm">fallback</span>
                @endif
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        </div>
        <div class="flex gap-4 items-center justify-between">
            <button
            type="submit"
            class="text-left px-2 py-1 bg-accent text-primary rounded cursor-pointer">Submit</button>
            <a
            class="cursor-pointer underline text-accent font-semibold"
            hx-get="/auth/signup"
            hx-target="#main"
            hx-swap="outerHTML"
            hx-push-url="true">Create account</a>
        </div>
    </form>
`, authenticated = authenticated,  _csrf = _csrf)

