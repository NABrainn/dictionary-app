@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.documents.data.strategy.ContentSubmission
@import lule.dictionary.documents.data.DocumentFormAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import lule.dictionary.documents.data.strategy.UrlSubmission
@import java.util.Map

@param DocumentFormAttribute documentFormAttribute
@param NavbarAttribute navbarAttribute
@param BaseAttribute baseAttribute
@param Map<String, String> messages


@template.layout.base(content = @`
    <div id="document-form-wrapper">
        @template.navbar.navbar(
            navbarAttribute = navbarAttribute,
            isAuthenticated = baseAttribute.isAuthenticated())
        <form
        class="md:w-3/5
        w-9/10 flex flex-col gap-6 bg-tertiary mx-auto p-6 rounded mt-16"
        hx-post="/imports/new"
        hx-params="title,url,content,strategy"
        hx-target="#main"
        hx-swap="innerHTML"
        hx-push-url="true">
            <div class="flex flex-col gap-2">
                <label class="text-accent font-bold text-2xl min-h-8" for="title">${documentFormAttribute.titleText()}</label>
                <textarea
                is="static-textarea"
                maxlength="175"
                class="
                outline-none overflow-hidden resize-none border border-2 border-primary rounded text-neutral h-16 leading-8
                focus:border-accent"
                rows="2"
                name="title"
                id="title"></textarea>
                @if(messages.get("title") != null)
                    <span class="text-error font-bold">${messages.get("title")}</span>
                @endif
            </div>
            <div class="flex flex-col gap-4">
                <span class="text-accent font-bold text-2xl">${documentFormAttribute.contentText()}</span>
                <div class="flex justify-center">
                    !{String color = switch(documentFormAttribute.submissionStrategy()) {
                        case ContentSubmission content -> messages.getOrDefault("content", "");
                        case UrlSubmission url -> messages.get("invalidUriMessage");
                        default -> "";
                    };}
                    @if(documentFormAttribute.submissionStrategy() instanceof UrlSubmission)
                        <button class="
                        bg-primary border border-2 border-r-1 border-primary text-accent px-4 py-1 grow-1 outline-none"
                        type="button"
                        id="url"
                        hx-get="/imports/new/"
                        hx-params="strategy"
                        hx-vals='{
                            "strategy": "url_submit"
                        }'
                        hx-target="#document-form-wrapper"
                        hx-swap="outerHTML"
                        hx-push-url="false"
                        autofocus>${documentFormAttribute.importByUrlBtnText()}</button>
                        <button
                        class="
                        border border-2 border-l-1 border-primary text-accent px-4 py-1 grow-1"
                        type="button"
                        id="insert"
                        hx-get="/imports/new/"
                        hx-params="strategy"
                        hx-vals='{
                            "strategy": "content_submit"
                        }'
                        hx-target="#document-form-wrapper"
                        hx-swap="outerHTML"
                        hx-push-url="false">${documentFormAttribute.insertManuallyBtnText()}</button>
                    @else
                        <button class="
                        border border-2 border-r-1 border-primary text-accent px-4 py-1 grow-1 outline-none"
                        type="button"
                        id="url"
                        hx-get="/imports/new/"
                        hx-params="strategy"
                        hx-vals='{
                            "strategy": "url_submit"
                        }'
                        hx-target="#document-form-wrapper"
                        hx-swap="outerHTML"
                        hx-push-url="false"
                        autofocus>${documentFormAttribute.importByUrlBtnText()}</button>
                        <button
                        class="
                        bg-primary border border-2 border-l-1 border-primary text-accent px-4 py-1 grow-1"
                        type="button"
                        id="insert"
                        hx-get="/imports/new/"
                        hx-params="strategy"
                        hx-vals='{
                            "strategy": "content_submit"
                        }'
                        hx-target="#document-form-wrapper"
                        hx-swap="outerHTML"
                        hx-push-url="false">${documentFormAttribute.insertManuallyBtnText()}</button>
                    @endif

                </div>
                !{String message = switch(documentFormAttribute.submissionStrategy()) {
                    case ContentSubmission content -> messages.getOrDefault("content", "");
                    case UrlSubmission url when messages.get("invalidUriMessage") != null -> messages.get("invalidUriMessage");
                    case UrlSubmission url when messages.get("url") != null -> messages.get("url");
                    default -> "";
                };}
                @if(documentFormAttribute.submissionStrategy() instanceof ContentSubmission content)
                    @template.create-document-form.content-form(
                        spaceForContentText = content.spaceForContentText(),
                        message = message
                    )
                @elseif(documentFormAttribute.submissionStrategy() instanceof UrlSubmission url)
                    @template.create-document-form.url-form(
                        spaceForUrlText = url.spaceForUrlText(),
                        message = message
                    )
                @endif
            </div>
            <div>
                <button
                type="submit"
                class="text-left px-2 py-1 bg-accent text-primary rounded cursor-pointer">${documentFormAttribute.submitBtnText()}</button>
            </div>
        </form>
    </div>
    `,
baseAttribute = baseAttribute)
