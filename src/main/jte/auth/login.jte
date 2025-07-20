@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.result.ServiceResultImp
@import java.util.Map

@param ServiceResultImp<?> result
@param BaseAttribute baseAttribute 
@param Map<String, String> navbarLocalization
@param Map<String, String> authLocalization

@template.layout.base(authView = @`
    @template.navbar.navbar(
        navbarLocalization = navbarLocalization,
        username = baseAttribute.username(),
        authenticated = baseAttribute.isAuthenticated(),
        sourceLanguage = baseAttribute.sourceLanguageData(),
        targetLanguage = baseAttribute.targetLanguageData(),
        wordsLearned = baseAttribute.wordsLearned(),
        dailyStreak = baseAttribute.dailyStreak(),
        languageData = baseAttribute.allLanguageData()
    )
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
        <span class="text-accent text-2xl font-bold">${authLocalization.get("log_in")}</span>
        <div class="flex flex-col">
            <label class="text-accent" for="login">${authLocalization.get("username")}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="text" name="login" id="login">
            @if(result != null)
                @if(result.hasError() && result.messages().get("login") != null)
                    <span class="text-error font-semibold text-sm">${result.messages().get("login")}</span>
                @else
                    <span class="invisible text-sm">fallback</span>
                @endif
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        </div>
        <div class="flex flex-col">
            <label class="text-accent" for="password">${authLocalization.get("password")}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="password" name="password" id="password">
            @if(result != null)
                @if(result.hasError() && result.messages().get("password") != null)
                    <span class="text-error font-semibold text-sm">${result.messages().get("password")}</span>
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
            class="text-left px-2 py-1 bg-accent text-primary rounded cursor-pointer">${authLocalization.get("submit")}</button>
            <a
            class="cursor-pointer underline text-accent font-semibold"
            hx-get="/auth/signup"
            hx-target="#main"
            hx-swap="outerHTML"
            hx-push-url="true">${authLocalization.get("create_account")}</a>
        </div>
    </form>
`,
baseAttribute = baseAttribute)

