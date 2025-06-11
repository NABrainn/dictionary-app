@import org.springframework.security.core.Authentication

@param Authentication authentication

<nav class="flex justify-between bg-secondary p-4">
    <div class="text-lg text-neutral">Logo</div>
    @if(authentication != null)
        <a
        class="text-lg text-neutral underline cursor-pointer"
        hx-post="/auth/logout"
        hx-target="#main"
        hx-swap="innerHTML"
        hx-push-url="/auth/login">
            Log out
        </a>
    @else
        <a
        class="text-lg text-neutral underline cursor-pointer"
        hx-get="/auth/login"
        hx-target="#main"
        hx-swap="innerHTML"
        hx-push-url="true">
            Log in
        </a>
    @endif
</nav>