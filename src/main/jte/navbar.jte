@import lule.dictionary.service.language.Language

@param String username = null
@param Language sourceLanguage = null
@param String targetLanguage
@param boolean authenticated
@param int wordsLearned

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
                <span class="font-bold text-neutral text-lg text-primary">${targetLanguage}</span>
                <span class="font-bold text-neutral text-lg text-accent">🧠 words: ${wordsLearned}</span>
                <span class="font-bold text-neutral text-lg text-accent">💪 streak: 420 days</span>
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