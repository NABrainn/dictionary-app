@param String text

<div
class="flex items-center align-center text-lg text-neutral">
    <a
    hx-get="/"
    hx-swap="innerHTML"
    hx-target="#main"
    hx-push-url="true"
    class="
    md:text-lg md:inline
    hidden text-md cursor-pointer underline font-bold">${text}</a>
    <a
    hx-get="/"
    hx-swap="innerHTML"
    hx-target="#main"
    hx-push-url="true"
    class="
    md:text-lg md:hidden
    inline text-lg cursor-pointer font-bold"
    >
        <svg
        class="md:size-10
        size-6 cursor-pointer fill-accent" width="20px" height="20px" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
            <path d="M1 6V15H6V11C6 9.89543 6.89543 9 8 9C9.10457 9 10 9.89543 10 11V15H15V6L8 0L1 6Z"/>
        </svg>
    </a>
</div>