<nav class="flex justify-between bg-secondary p-4">
    <div class="text-lg text-neutral">Logo</div>
    <a
    class="text-lg text-tertiary underline cursor-pointer"
    hx-get="/auth/login"
    hx-target="#main"
    hx-swap="innerHTML"
    hx-push-url="true">Log in</a>
</nav>