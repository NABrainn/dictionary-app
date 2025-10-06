@import lule.dictionary.pagination.data.PaginationData

@param PaginationData paginationData
@param int documentId

!{String leftArrowStyles = paginationData.currentPage() != 1 ? "block" : "invisible pointer-events-none";}
!{String rightArrowStyles = paginationData.currentPage() != paginationData.pages() ? "block" : "invisible pointer-events-none";}
<div class="border-b border-b-4 border-b-primary ${leftArrowStyles}">
    <span
    class="text-2xl text-accent font-bold cursor-pointer"
    hx-trigger="click"
    hx-params="page,selection"
    hx-vals='{
        "page": "${paginationData.currentPage() - 1}",
        "selection": "none"
    }'
    hx-get="/documents/${documentId}"
    hx-target="#main"
    hx-swap="innerHTML"
    hx-push-url="false">
        @raw^@endraw
    </span>
</div>
@for(var page : paginationData.currentRow())
    @if(page == paginationData.currentPage())
        <div class="border-b border-b-4 border-b-tertiary">
            <span class="text-2xl font-bold text-accent">
                ${page}
            </span>
        </div>
    @else
        <div class="border-b border-b-4 border-b-primary">
            <span
            class="text-2xl font-bold text-accent cursor-pointer"
            hx-trigger="click"
            hx-params="page,selection"
            hx-vals='{
                "page": "${page}",
                "selection": "none"
            }'
            hx-get="/documents/${documentId}"
            hx-target="#main"
            hx-swap="innerHTML"
            hx-push-url="false">
                ${page}
            </span>
        </div>
    @endif
@endfor
<div class="border-b border-b-4 border-b-primary ${rightArrowStyles}">
    <span
    class="text-2xl text-accent font-bold cursor-pointer"
    hx-trigger="click"
    hx-params="page,selection"
    hx-vals='{
        "page": "${paginationData.currentPage() + 1}",
        "selection": "none"
    }'
    hx-get="/documents/${documentId}"
    hx-target="#main"
    hx-swap="innerHTML"
    hx-push-url="false">
        @rawv@endraw
    </span>
</div>