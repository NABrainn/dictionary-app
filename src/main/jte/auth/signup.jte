@import lule.dictionary.auth.data.AuthLocalizationKey
@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import java.util.Map

@param NavbarAttribute navbarAttribute
@param BaseAttribute baseAttribute
@param Map<String, String> navbarLocalization
@param Map<AuthLocalizationKey, String> localization


@template.layout.base(authView = @`
    @template.navbar.navbar(
        navbarAttribute = navbarAttribute,
        isAuthenticated = baseAttribute.isAuthenticated()
    )
    <form
        hx-post="/auth/signup"
        hx-target="#main"
        hx-swap="outerHTML"
        hx-headers='js:{
            timeZoneOffset: new Date().getTimezoneOffset()
        }'
        class="md:w-2/5
        w-4/5 flex flex-col bg-secondary p-8 mx-auto gap-8">
        <span class="text-accent text-2xl font-bold">${localization.get(AuthLocalizationKey.SIGN_UP)}</span>
        <div class="flex flex-col">
            <label class="text-accent" for="login">${localization.get(AuthLocalizationKey.USERNAME)}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="text" name="login" id="login">
            <%--ERROR MESSAGES--%>
        </div>
        <div class="flex flex-col">
            <label class="text-accent" for="email">${localization.get(AuthLocalizationKey.EMAIL)}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="email" name="email" id="email">
            <%--ERROR MESSAGES--%>
        </div>
        <div class="flex flex-col">
            <label class="text-accent" for="password">${localization.get(AuthLocalizationKey.PASSWORD)}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="password" name="password" id="password">
            <%--ERROR MESSAGES--%>
        </div>
        <div class="flex gap-4 items-center justify-between">
            <button type="submit" class="text-left px-2 py-1 bg-accent text-primary rounded cursor-pointer">${localization.get(AuthLocalizationKey.SUBMIT)}</button>
            <a
            class="cursor-pointer underline text-accent font-semibold"
            hx-get="/auth/login"
            hx-target="#main"
            hx-swap="outerHTML"
            hx-push-url="true">${localization.get(AuthLocalizationKey.LOG_IN)}</a>
        </div>
    </form>
`,
baseAttribute = baseAttribute)
