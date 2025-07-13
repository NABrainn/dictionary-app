@import gg.jte.Content
@import lule.dictionary.dto.application.BaseAttribute

@param Content authView = null
@param Content content = null
@param BaseAttribute baseAttribute

<script>
    document.addEventListener("htmx:configRequest", function(event) {
        event.detail.headers["X-XSRF-TOKEN"] = "${baseAttribute._csrf().getToken()}";
    });
</script>

<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="/htmx.min.js"></script>
        <link rel="stylesheet" href="/output.css">
        <link rel="icon" type="image/png" sizes="32x32" href="/images/icon.png">
        <title>here is the required title element</title>
    </head>
    <body
    class="bg-primary grow-1 flex flex-col gap-8 min-h-screen pb-8"
    id="main">
        @if(authView != null)
            ${authView}
        @endif
        @if(content != null)
            ${content}
        @endif
    </body>
</html>