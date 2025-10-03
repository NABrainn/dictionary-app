@import gg.jte.support.ForSupport
@import java.util.List

@param int id
@param int currentPage
@param int pages
@param List<List<Integer>> rows
@param int currentRow
@param int firstPageOfRow

@if(currentPage > 1)
    <div
    class="text-2xl cursor-pointer select-none"
    hx-trigger="click"
    hx-params="page,selection"
    hx-vals='{
        "page": "${currentPage - 1}",
        "selection": "none"
    }'
    hx-get="/documents/${id}"
    hx-target="#main"
    hx-swap="innerHTML"
    hx-push-url="false">⬅️</div>
@else
    <div
    class="text-2xl cursor-pointer select-none invisible"
    hx-trigger="click"
    hx-params="page,selection"
    hx-vals='{
        "page": "${currentPage + 1}",
        "selection": "none"
    }'
    hx-get="/documents/${id}"
    hx-target="#main"
    hx-swap="innerHTML"
    hx-push-url="false">➡️</div>
@endif
@for(var row : ForSupport.of(rows.get(currentRow)))
    @if(currentPage == row.get())
        <form
        hx-trigger="click"
        hx-params="page,selection"
        hx-vals='{
            "page": "${row.get()}",
            "selection": "none"
        }'
        hx-get="/documents/${id}"
        hx-target="#main"
        hx-swap="innerHTML"
        hx-push-url="false">
            <div class="bg-tertiary rounded text-primary font-bold text-xl">
                <button class="px-2 cursor-pointer" type="submit">${row.get()}</button>
            </div>
        </form>
    @else
        <form
        hx-trigger="click"
        hx-params="page,selection"
        hx-vals='{
            "page": "${row.get()}",
            "selection": "none"
        }'
        hx-get="/documents/${id}"
        hx-target="#main"
        hx-swap="innerHTML"
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
    hx-trigger="click"
    hx-params="page,selection"
    hx-vals='{
        "page": "${currentPage + 1}",
        "selection": "none"
    }'
    hx-get="/documents/${id}"
    hx-target="#main"
    hx-swap="innerHTML"
    hx-push-url="false">➡️</div>
@else
    <div
    class="text-2xl cursor-pointer select-none invisible"
    hx-trigger="click"
    hx-params="page,selection"
    hx-vals='{
        "page": "${currentPage + 1}",
        "selection": "none"
    }'
    hx-get="/documents/${id}"
    hx-target="#main"
    hx-swap="innerHTML"
    hx-push-url="false">➡️</div>
@endif