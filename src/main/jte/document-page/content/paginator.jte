@import gg.jte.support.ForSupport
@import java.util.List

@param int id
@param int currentPage
@param int pages
@param int importId
@param List<List<Integer>> rows
@param int currentRow
@param int firstPageOfRow

@if(currentPage > 1)
    <div
    class="text-2xl cursor-pointer select-none"
    hx-params="page"
    hx-get="/imports/${importId}?page=${currentPage - 1}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">⬅️</div>
@else
    <div
    class="text-2xl cursor-pointer select-none invisible"
    hx-params="page"
    hx-get="/imports/${importId}?page=${currentPage + 1}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">➡️</div>
@endif
@for(var row : ForSupport.of(rows.get(currentRow)))
    @if(currentPage == row.get())
        <form
        hx-params="page"
        hx-get="/imports/${id}?page=${row.get()}"
        hx-target="#main"
        hx-swap="outerHTML"
        hx-push-url="false">
            <div class="bg-tertiary rounded text-primary font-bold text-xl">
                <button class="px-2 cursor-pointer" type="submit">${row.get()}</button>
            </div>
        </form>
    @else
        <form
        hx-params="page"
        hx-get="/imports/${id}?page=${row.get()}"
        hx-target="#main"
        hx-swap="outerHTML"
        hx-push-url="false">
            <div class="bg-accent rounded text-primary font-bold text-xl">
                <button class="px-2 cursor-pointer" type="submit">${row.get()}</button>
            </div>
        </form>
    @endif
@endfor

@if(currentPage != pages)
    <div
    class="text-2xl cursor-pointer select-none"
    hx-params="page"
    hx-get="/imports/${importId}?page=${currentPage + 1}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">➡️</div>
@else
    <div
    class="text-2xl cursor-pointer select-none invisible"
    hx-params="page"
    hx-get="/imports/${importId}?page=${currentPage + 1}"
    hx-target="#main"
    hx-swap="outerHTML"
    hx-push-url="false">➡️</div>
@endif