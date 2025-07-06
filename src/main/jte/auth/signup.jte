@import lule.dictionary.service.dto.ServiceResult
@import lule.dictionary.service.language.Language
@import org.springframework.security.web.csrf.CsrfToken
@import java.util.Map

@param ServiceResult result = null
@param CsrfToken _csrf
@param boolean authenticated
@param String username
@param String targetLanguage
@param int wordsLearned
@param int dailyStreak
@param Map<Language, String> languages


@template.layout.base(authView = @`
    @template.navbar(
        username = username,
        authenticated = authenticated,
        targetLanguage = targetLanguage,
        wordsLearned = wordsLearned,
        dailyStreak = dailyStreak,
        languages = languages
    )
    <form
        hx-post="/auth/signup"
        hx-target="#main"
        hx-swap="outerHTML"
        hx-headers='js:{
            timeZoneOffset: new Date().getTimezoneOffset()
        }'
        class="flex flex-col bg-secondary w-2/5 p-8 mx-auto gap-8">
        <span class="text-accent text-2xl font-bold">Sign up</span>
        <div class="flex flex-col">
            <label class="text-accent" for="login">Username</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="text" name="login" id="login">
            @if(result != null)
                @if(result.error() && result.messages().get("login") != null)
                    <span class="text-error font-semibold text-sm">${result.messages().get("login")}</span>
                @else
                    <span class="invisible text-sm">fallback</span>
                @endif
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        </div>
        <div class="flex flex-col">
            <label class="text-accent" for="email">Email</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral text-sm min-h-8
            focus:border-accent"
            type="email" name="email" id="email">
            @if(result != null)
                @if(result.error() && result.messages().get("email") != null)
                    <span class="text-error font-semibold text-sm">${result.messages().get("email")}</span>
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
                @if(result.error() && result.messages().get("password") != null)
                    <span class="text-error font-semibold text-sm">${result.messages().get("password")}</span>
                @else
                    <span class="invisible text-sm">fallback</span>
                @endif
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        </div>
        <div class="flex gap-4 items-center justify-between">
            <button type="submit" class="text-left px-2 py-1 bg-accent text-primary rounded cursor-pointer">Submit</button>
            <a
            class="cursor-pointer underline text-accent font-semibold"
            hx-get="/auth/login"
            hx-target="#main"
            hx-swap="outerHTML"
            hx-push-url="true">Log in</a>
        </div>
    </form>
`,
authenticated = authenticated,
_csrf = _csrf,
username = username,
targetLanguage = targetLanguage,
wordsLearned = wordsLearned,
languages = languages
)
