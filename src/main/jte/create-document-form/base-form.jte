@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.documents.data.DocumentLocalizationKey
@import lule.dictionary.documents.data.documentSubmission.ContentSubmissionStrategy
@import lule.dictionary.documents.data.attribute.DocumentFormAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import lule.dictionary.documents.data.documentSubmission.UrlSubmissionStrategy
@import java.util.Map

@param DocumentFormAttribute attribute
@param NavbarAttribute navbarAttribute
@param BaseAttribute baseAttribute
@param Map<String, String> violation

@template.base(content = @`
    <div id="document-form-wrapper">
        @template.navbar.navbar(
        navbarAttribute = navbarAttribute,
        baseAttribute = baseAttribute)
        <script>
            if (!customElements.get('static-textarea')) {
                class StaticTextArea extends HTMLTextAreaElement {
                    constructor() {
                        super()
                    }
                    connectedCallback() {
                        this.addEventListener('keydown', (event) => {
                            if (event.key === 'Enter') {
                                event.preventDefault();
                            }
                        });
                    }
                }
                customElements.define('static-textarea', StaticTextArea, { extends: 'textarea' });
            }
        </script>
        <form
        class="md:w-3/5
        w-9/10 flex flex-col gap-6 bg-tertiary mx-auto p-6 rounded mt-16"
        hx-post="/documents/new"
        hx-params="title,url,content,strategy,_csrf"
        hx-target="#main"
        hx-swap="innerHTML"
        hx-push-url="true">
            <input type="hidden" name="_csrf" value="${baseAttribute._csrf().getToken()}">
            <div class="flex flex-col gap-2">
                <label class="text-accent font-bold text-2xl min-h-8" for="title">${attribute.localization().get(DocumentLocalizationKey.TITLE)}</label>
                <textarea
                is="static-textarea"
                maxlength="175"
                class="
                outline-none overflow-hidden resize-none border border-2 border-primary rounded text-neutral h-16 leading-8
                focus:border-accent"
                rows="2"
                name="title"
                id="title">${switch (attribute.submissionStrategy()){
                    case ContentSubmissionStrategy content -> content.title();
                    case UrlSubmissionStrategy url -> url.title();
                }}</textarea>
                @if(violation.get("title") != null)
                    <span class="text-error font-bold">${violation.get("title")}</span>
                @endif
            </div>
            <div class="flex flex-col gap-4">
                <span class="text-accent font-bold text-2xl">${attribute.localization().get(DocumentLocalizationKey.CONTENT)}</span>
                <div class="flex justify-center">
                    @if(attribute.submissionStrategy() instanceof UrlSubmissionStrategy)
                        <button class="
                        bg-primary border border-2 border-r-1 border-primary text-accent px-4 py-1 grow-1 outline-none"
                        type="button"
                        id="url"
                        hx-get="/documents/new/"
                        hx-params="strategy"
                        hx-vals='{
                            "strategy": "url_submit"
                        }'
                        hx-target="#document-form-wrapper"
                        hx-swap="outerHTML"
                        hx-push-url="false"
                        autofocus>${attribute.localization().get(DocumentLocalizationKey.IMPORT_BY_URL)}</button>
                        <button
                        class="
                        border border-2 border-l-1 border-primary text-accent px-4 py-1 grow-1"
                        type="button"
                        id="insert"
                        hx-get="/documents/new/"
                        hx-params="strategy"
                        hx-vals='{
                            "strategy": "content_submit"
                        }'
                        hx-target="#document-form-wrapper"
                        hx-swap="outerHTML"
                        hx-push-url="false">${attribute.localization().get(DocumentLocalizationKey.INSERT_MANUALLY)}</button>
                    @else
                        <button class="
                        border border-2 border-r-1 border-primary text-accent px-4 py-1 grow-1 outline-none"
                        type="button"
                        id="url"
                        hx-get="/documents/new/"
                        hx-params="strategy"
                        hx-vals='{
                            "strategy": "url_submit"
                        }'
                        hx-target="#document-form-wrapper"
                        hx-swap="outerHTML"
                        hx-push-url="false"
                        autofocus>${attribute.localization().get(DocumentLocalizationKey.IMPORT_BY_URL)}</button>
                        <button
                        class="
                        bg-primary border border-2 border-l-1 border-primary text-accent px-4 py-1 grow-1"
                        type="button"
                        id="insert"
                        hx-get="/documents/new/"
                        hx-params="strategy"
                        hx-vals='{
                            "strategy": "content_submit"
                        }'
                        hx-target="#document-form-wrapper"
                        hx-swap="outerHTML"
                        hx-push-url="false">${attribute.localization().get(DocumentLocalizationKey.INSERT_MANUALLY)}</button>
                    @endif

                </div>
                @if(attribute.submissionStrategy() instanceof ContentSubmissionStrategy content)
                    @template.create-document-form.content-form(
                    contentPlaceholderText = content.contentPlaceholderText(),
                    violation = violation.getOrDefault("content", ""),
                    content = content.content())
                @elseif(attribute.submissionStrategy() instanceof UrlSubmissionStrategy url)
                    @template.create-document-form.url-form(
                    urlPlaceholderText = url.urlPlaceholderText(),
                    violation = violation.getOrDefault("url", ""),
                    content = url.url())
                @endif
            </div>
            <div>
                <button
                type="submit"
                class="text-left px-2 py-1 bg-accent text-primary rounded cursor-pointer">${attribute.localization().get(DocumentLocalizationKey.SUBMIT)}</button>
            </div>
        </form>
    </div>
    `,
baseAttribute = baseAttribute)
