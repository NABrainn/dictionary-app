@import gg.jte.Content
@import lule.dictionary.controllerAdvice.data.user.User

@param Content authView = null
@param Content content = null
@param User user


<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="/htmx.min.js"></script>
        <link rel="stylesheet" href="/output.css">
        <link rel="icon" type="image/png" sizes="32x32" href="/images/icon.png">
        <title>application</title>
    </head>
    <body
    class="bg-primary grow-1 flex flex-col gap-8 min-h-screen"
    id="main">
        @if(authView != null)
            ${authView}
        @endif
        @if(content != null)
            ${content}
        @endif
    </body>
</html>