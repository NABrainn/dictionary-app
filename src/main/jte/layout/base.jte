@import gg.jte.Content
@import lule.dictionary.controllerAdvice.data.BaseAttribute
@param Content authView = null
@param Content content = null
@param BaseAttribute baseAttribute

<html lang="en">
    <script>
        @if(baseAttribute != null)
            @if(!baseAttribute.isAuthenticated())
                if (window.location.pathname.startsWith('/auth/')) {
                    fetch('/localization/uiLanguage', {
                        method: "GET",
                        headers: {
                            "uiLanguage": window.navigator.languages[1] || "en"
                        }
                    })
                    .catch(error => console.error("Error setting system language:", error.message));
                }
            @endif
        @endif
    </script>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="/htmx.min.js"></script>
        <script src="/util.js"></script>
        <link rel="stylesheet" href="/output.css">
        <link rel="icon" type="image/png" sizes="32x32" href="/images/icon.png">
        <title>here is the required title element</title>
    </head>
    <body
    class="bg-primary grow-1 flex flex-col gap-8"
    id="main">
        @if(authView != null)
            ${authView}
        @endif
        @if(content != null)
            ${content}
        @endif
    </body>
</html>