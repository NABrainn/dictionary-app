@import lule.dictionary.auth.data.localization.AuthText
@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import java.util.Map

@param NavbarAttribute navbarAttribute
@param BaseAttribute baseAttribute
@param Map<String, String> navbarLocalization
@param Map<AuthText, String> localization
@param Map<String, String> error

@template.layout.base(authView = @`
    @template.navbar.navbar(
    navbarAttribute = navbarAttribute,
    isAuthenticated = baseAttribute.isAuthenticated())
    <form
        hx-post="/auth/signup"
        hx-target="#main"
        hx-swap="outerHTML"
        hx-headers='js:{
            timeZoneOffset: new Date().getTimezoneOffset()
        }'
        class="md:w-2/5
        w-4/5 flex flex-col bg-secondary p-8 mx-auto gap-8">
        <span class="text-accent text-2xl font-bold">${localization.get(AuthText.SIGN_UP)}</span>
        <div class="flex flex-col">
            <label class="text-accent" for="login">${localization.get(AuthText.USERNAME)}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="text" name="login" id="login">
            @if(error.get("login") != null)
                <span class="font-bold text-error">${error.get("login")}</span>
            @endif
        </div>
        <div class="flex flex-col">
            <label class="text-accent" for="email">${localization.get(AuthText.EMAIL)}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="email" name="email" id="email">
            @if(error.get("email") != null)
                <span class="font-bold text-error">${error.get("email")}</span>
            @endif        </div>
        <div class="flex flex-col">
            <label class="text-accent" for="password">${localization.get(AuthText.PASSWORD)}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="password" name="password" id="password">
            @if(error.get("password") != null)
                <span class="font-bold text-error">${error.get("password")}</span>
            @endif
        </div>
        <div class="flex gap-4 items-center justify-between">
            <button type="submit" class="text-left px-2 py-1 bg-accent text-primary rounded cursor-pointer">${localization.get(AuthText.SUBMIT)}</button>
            <a
            class="cursor-pointer underline text-accent font-semibold"
            hx-get="/auth/login"
            hx-target="#main"
            hx-swap="outerHTML"
            hx-push-url="true">${localization.get(AuthText.LOG_IN)}</a>
        </div>
    </form>
`,
baseAttribute = baseAttribute)
