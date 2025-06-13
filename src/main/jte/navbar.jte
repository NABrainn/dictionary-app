@param boolean authenticated
<nav class="flex justify-between bg-secondary p-4">
    <div class="text-lg text-neutral cursor-pointer">
        <a
        hx-get="/"
        hx-swap="innerHTML"
        hx-target="#main"
        hx-push-url="true"
        class="text-lg">Logo</a>
    </div>
    @if(authenticated)
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