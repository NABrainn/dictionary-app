@param int id
@param int currentPage
@param int pages
@param int importId

@if(currentPage > 1)
    <div
    class="text-2xl cursor-pointer select-none"
    hx-get="/imports/${importId}?page=${currentPage - 1}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">⬅️</div>
@else
    <div
    class="text-2xl cursor-pointer invisible select-none"
    hx-get="/imports/${importId}?page=${currentPage - 1}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">⬅️</div>
@endif
@if(currentPage - 3 >= 1)
    <form
    class="bg-accent rounded text-primary font-bold text-xl"
    hx-get="/imports/${id}?page=${currentPage - 3}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">
        <button class="px-2 cursor-pointer" type="submit">${currentPage - 3}</button>
    </form>
@endif
@if(currentPage - 2 >= 1)
    <form
    class="bg-accent rounded text-primary font-bold text-xl"
    hx-get="/imports/${id}?page=${currentPage - 2}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">
        <button class="px-2 cursor-pointer" type="submit">${currentPage - 2}</button>
    </form>
@endif
@if(currentPage - 1 >= 1)
    <form
    class="bg-accent rounded text-primary font-bold text-xl"
    hx-get="/imports/${id}?page=${currentPage - 1}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">
        <button class="px-2 cursor-pointer" type="submit">${currentPage - 1}</button>
    </form>
@endif
@if(currentPage <= pages)
    <form
    class="bg-tertiary rounded text-accent font-bold text-xl"
    hx-get="/imports/${id}?page=${currentPage}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">
        <button class="px-2 cursor-pointer" type="submit">${currentPage}</button>
    </form>
@endif
@if(currentPage + 1 < pages)
    <form
    class="bg-accent rounded text-primary font-bold text-xl"
    hx-get="/imports/${id}?page=${currentPage + 1}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">
        <button class="px-2 cursor-pointer" type="submit">${currentPage + 1}</button>
    </form>
@endif
@if(currentPage + 2 < pages)
    <form
    class="bg-accent rounded text-primary font-bold text-xl"
    hx-get="/imports/${id}?page=${currentPage + 2}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">
        <button class="px-2 cursor-pointer" type="submit">${currentPage + 2}</button>
    </form>
@endif
@if(currentPage != pages)
    <form
    class="bg-accent rounded text-primary font-bold text-xl"
    hx-get="/imports/${id}?page=${pages}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">
        <button class="px-2 cursor-pointer" type="submit">${pages}</button>
    </form>
@endif
@if(currentPage < pages)
    <div
    class="text-2xl cursor-pointer select-none"
    hx-get="/imports/${importId}?page=${currentPage + 1}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">➡️</div>
@else
    <div
    class="text-2xl cursor-pointer invisible select-none"
    hx-get="/imports/${importId}?page=${currentPage + 1}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">➡️</div>
@endif