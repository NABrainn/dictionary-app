@import lule.dictionary.dto.application.LanguageData
@import java.util.List

@param String username
@param LanguageData sourceLanguage
@param LanguageData targetLanguage
@param boolean authenticated
@param int wordsLearned
@param int dailyStreak
@param List<LanguageData> languageData


<nav class="flex justify-between bg-secondary p-4 items-center">
    <div class="text-lg text-neutral">
        <a
        hx-get="/"
        hx-swap="innerHTML"
        hx-target="#main"
        hx-push-url="true"
        class="text-lg  cursor-pointer underline font-bold">Home</a>
    </div>
    @if(authenticated)
        <div class="flex gap-3 items-center">
            <div class="flex gap-6 items-center bg-tertiary p-2 rounded select-none">
                @template.language-picker(
                    targetLanguage = targetLanguage.fullName(),
                    languages = languageData
                )
                <span class="font-bold text-neutral text-lg text-accent">🧠 words: ${wordsLearned}</span>
                @if(dailyStreak == 1)
                    <span class="font-bold text-neutral text-lg text-accent">💪 streak: ${dailyStreak} day</span>
                @else
                    <span class="font-bold text-neutral text-lg text-accent">💪 streak: ${dailyStreak} days</span>
                @endif
            </div>

        </div>
        <a
        class="text-lg text-neutral underline cursor-pointer font-bold"
        hx-post="/auth/logout"
        hx-target="#main"
        hx-swap="innerHTML"
        hx-push-url="/auth/login">
            Log out
        </a>
    @else
        <a
        class="text-lg text-neutral underline cursor-pointer font-bold"
        hx-get="/auth/login"
        hx-target="#main"
        hx-swap="innerHTML"
        hx-push-url="true">
            Log in
        </a>
    @endif
</nav>