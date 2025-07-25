@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.result.ServiceResultImp
@import java.util.Map
@import lule.dictionary.dto.application.attribute.ProfilePanelAttribute

@param ProfilePanelAttribute profilePanelAttribute
@param ServiceResultImp<?> result = null
@param BaseAttribute baseAttribute 
@param Map<String, String> navbarLocalization
@param Map<String, String> importFormLocalization


@template.layout.base(content = @`
    @template.navbar.navbar(
        navbarLocalization = navbarLocalization,
        profilePanelAttribute = profilePanelAttribute,
        isAuthenticated = baseAttribute.isAuthenticated(),
        isProfileOpen = baseAttribute.isProfileOpen()
    )
    <form
    class="md:w-3/5
    w-9/10 flex flex-col gap-6 bg-tertiary mx-auto p-6 rounded"
    hx-post="/imports/new"
    hx-target="#main"
    hx-swap="innerHTML"
    hx-push-url="true">
        <input type="hidden" name="${baseAttribute._csrf().getParameterName()}" value="${baseAttribute._csrf().getToken()}">
        <div class="flex flex-col gap-2">
            <label class="text-accent font-bold text-2xl min-h-8" for="title">${importFormLocalization.get("title")}</label>
            <input
            class="
            outline-none border border-2 border-primary rounded text-neutral
            focus:border-accent"
            name="title"
            id="title">
            @if(result != null)
                @if(result.hasError() && result.messages().get("title") != null)
                    <span class="text-error font-semibold text-sm">${result.messages().get("title")}</span>
                @else
                    <span class="invisible text-sm">fallback</span>
                @endif
            @else
                <span class="invisible text-sm">fallback</span>
            @endif
        </div>
        <div class="flex flex-col gap-4">
            <span class="text-accent font-bold text-2xl">${importFormLocalization.get("content")}</span>
            <div class="flex justify-center">
                <button class="
                bg-primary border border-2 border-r-1 border-primary text-accent px-4 py-1 grow-1 outline-none"
                type="button"
                id="url"
                hx-get="/imports/url-form"
                hx-target="#content"
                hx-swap="outerHTML"
                hx-push-url="false"
                autofocus>${importFormLocalization.get("import_by_url")}</button>
                <button class="
                border border-2 border-l-1 border-primary text-accent px-4 py-1 grow-1"
                type="button"
                id="insert"
                hx-get="/imports/textarea-form"
                hx-target="#content"
                hx-swap="outerHTML"
                hx-push-url="false">${importFormLocalization.get("insert_manually")}</button>
            </div>
            @template.create-import-form.url-form(
                result = result,
                importFormLocalization = importFormLocalization
            )
        </div>
        <div>
            <button
            type="submit"
            class="text-left px-2 py-1 bg-accent text-primary rounded cursor-pointer">${importFormLocalization.get("submit")}</button>
        </div>
    </form>
    <script>
        if (!customElements.get('button-toggler')) {
            class ButtonToggler extends HTMLElement {
                constructor() {
                    super();
                    this.initListeners();
                }

                initListeners() {
                    document.getElementById('url').addEventListener('click', (e) => {
                        document.getElementById('url').classList.add('bg-primary');
                        document.getElementById('insert').classList.remove('bg-primary');
                    });

                    document.getElementById('insert').addEventListener('click', (e) => {
                        document.getElementById('url').classList.remove('bg-primary');
                        document.getElementById('insert').classList.add('bg-primary');
                    });
                }
            }
            customElements.define('button-toggler', ButtonToggler);
        }
    </script>
    <button-toggler></button-toggler>
`,
baseAttribute = baseAttribute)