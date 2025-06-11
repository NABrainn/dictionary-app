@import gg.jte.Content
@import org.springframework.security.core.Authentication

@param Content authView = null
@param Content content = null
@param Authentication authentication = null

<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="/htmx.min.js"></script>
        <link rel="stylesheet" href="/output.css">
        <link rel="icon" type="image/png" sizes="32x32" href="/images/icon.png">
        <title>here is the required title element</title>
    </head>
    <body class="bg-primary grow-1 flex flex-col gap-8 min-h-screen" id="main">
        @template.navbar(authentication = authentication)
        @if(authView != null)
            ${authView}
        @endif
        @if(content != null && authentication != null)
            ${content}
        @endif
    </body>
</html>