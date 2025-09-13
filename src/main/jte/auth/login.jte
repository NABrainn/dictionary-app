@import lule.dictionary.auth.data.localization.AuthTextLocalization
@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import java.util.Map

@param BaseAttribute baseAttribute
@param NavbarAttribute navbarAttribute
@param Map<AuthTextLocalization, String> localization
@param Map<String, String> violation

@template.layout.base(authView = @`
    @template.navbar.navbar(
    navbarAttribute = navbarAttribute,
    isAuthenticated = baseAttribute.isAuthenticated())
    <form
    hx-post="/auth/login"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="true"
    hx-headers='js:{
        timeZoneOffset: new Date().getTimezoneOffset()
    }'
    class="md:w-2/5
    w-4/5 flex flex-col bg-secondary p-8 mx-auto gap-8">
        <span class="text-accent text-2xl font-bold">${localization.get(AuthTextLocalization.LOG_IN)}</span>
        <div class="flex flex-col">
            <label class="text-accent" for="login">${localization.get(AuthTextLocalization.USERNAME)}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="text" name="login" id="login">
            @if(violation.get("login") != null)
                <span class="font-bold text-error">${violation.get("login")}</span>
            @endif
        </div>
        <div class="flex flex-col">
            <label class="text-accent" for="password">${localization.get(AuthTextLocalization.PASSWORD)}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="password" name="password" id="password">
            @if(violation.get("password") != null)
                <span class="font-bold text-error">${violation.get("password")}</span>
            @endif
        </div>
        <div class="flex gap-4 items-center justify-between">
            <button
            type="submit"
            class="text-left px-2 py-1 bg-accent text-primary rounded cursor-pointer">${localization.get(AuthTextLocalization.SUBMIT)}</button>
            <a
            class="cursor-pointer underline text-accent font-semibold"
            hx-get="/auth/signup"
            hx-target="#main"
            hx-swap="outerHTML"
            hx-push-url="true">${localization.get(AuthTextLocalization.CREATE_ACCOUNT)}</a>
        </div>
    </form>
`,
baseAttribute = baseAttribute)

