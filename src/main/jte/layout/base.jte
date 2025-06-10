@import gg.jte.Content

@param Content content

<!DOCTYPE>
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
        @template.navbar()
        ${content}
    </body>
</html>