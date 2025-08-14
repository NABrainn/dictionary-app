@import lule.dictionary.dto.application.LanguageData
@import java.util.List

@param String text
@param String size
@param LanguageData selectedInterfaceLanguage
@param LanguageData selectedTranslationLanguage
@param List<LanguageData> languages

<div class="flex justify-end items-center justify-center ${size}">
<%--    <a--%>
<%--    class="--%>
<%--    md:text-lg md:inline--%>
<%--    text-md hidden text-neutral underline cursor-pointer font-bold"--%>
<%--    hx-post="/auth/logout"--%>
<%--    hx-target="#main"--%>
<%--    hx-swap="innerHTML"--%>
<%--    hx-push-url="/auth/login">--%>
<%--        ${text}--%>
<%--    </a>--%>
<%--    <a--%>
<%--    class="--%>
<%--    md:text-lg md:hidden--%>
<%--    text-md inline text-neutral cursor-pointer font-bold"--%>
<%--    hx-post="/auth/logout"--%>
<%--    hx-target="#main"--%>
<%--    hx-swap="innerHTML"--%>
<%--    hx-push-url="/auth/login">--%>
<%--    <svg width="20px" height="20px" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" class="fill-accent size-6">--%>
<%--        <path d="M868 732h-70.3c-4.8 0-9.3 2.1-12.3 5.8-7 8.5-14.5 16.7-22.4 24.5a353.84 353.84 0 0 1-112.7 75.9A352.8 352.8 0 0 1 512.4 866c-47.9 0-94.3-9.4-137.9-27.8a353.84 353.84 0 0 1-112.7-75.9 353.28 353.28 0 0 1-76-112.5C167.3 606.2 158 559.9 158 512s9.4-94.2 27.8-137.8c17.8-42.1 43.4-80 76-112.5s70.5-58.1 112.7-75.9c43.6-18.4 90-27.8 137.9-27.8 47.9 0 94.3 9.3 137.9 27.8 42.2 17.8 80.1 43.4 112.7 75.9 7.9 7.9 15.3 16.1 22.4 24.5 3 3.7 7.6 5.8 12.3 5.8H868c6.3 0 10.2-7 6.7-12.3C798 160.5 663.8 81.6 511.3 82 271.7 82.6 79.6 277.1 82 516.4 84.4 751.9 276.2 942 512.4 942c152.1 0 285.7-78.8 362.3-197.7 3.4-5.3-.4-12.3-6.7-12.3zm88.9-226.3L815 393.7c-5.3-4.2-13-.4-13 6.3v76H488c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h314v76c0 6.7 7.8 10.5 13 6.3l141.9-112a8 8 0 0 0 0-12.6z"/>--%>
<%--    </svg>--%>
<%--    </a>--%>
    <div>
        <a
        class="
        md:text-lg md:inline
        text-md hidden text-neutral cursor-pointer font-bold relative">
            Settings
        </a>
        <div class="absolute top-16 right-8 flex flex-col bg-tertiary rounded p-2">
            <p class="font-bold text-md text-primary">Language:</p>
            <div class="flex flex-col">
                <div class="flex gap-2 justify-between text-neutral font-bold text-md">
                    <span>User interface:</span>
                    @template.navbar.language-picker(selectedLanguage = selectedInterfaceLanguage, languages = languages)
                </div>
                <div class="flex gap-2 justify-between text-neutral font-bold text-md">
                    <span>Translations:</span>
                    @template.navbar.language-picker(selectedLanguage = selectedTranslationLanguage, languages = languages)
                </div>
            </div>
        </div>
    </div>
    <a
    class="
    md:text-lg md:hidden
    text-md inline text-neutral cursor-pointer font-bold">
        <svg width="20px" height="20px" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg" class="fill-accent">
            <path d="M6.50001 0H9.50001L10.0939 2.37548C10.7276 2.6115 11.3107 2.95155 11.8223 3.37488L14.1782 2.70096L15.6782 5.29904L13.9173 7.00166C13.9717 7.32634 14 7.65987 14 8C14 8.34013 13.9717 8.67366 13.9173 8.99834L15.6782 10.701L14.1782 13.299L11.8223 12.6251C11.3107 13.0484 10.7276 13.3885 10.0939 13.6245L9.50001 16H6.50001L5.90614 13.6245C5.27242 13.3885 4.68934 13.0484 4.17768 12.6251L1.82181 13.299L0.321808 10.701L2.08269 8.99834C2.02831 8.67366 2.00001 8.34013 2.00001 8C2.00001 7.65987 2.02831 7.32634 2.08269 7.00166L0.321808 5.29904L1.82181 2.70096L4.17768 3.37488C4.68934 2.95155 5.27241 2.6115 5.90614 2.37548L6.50001 0ZM8.00001 10C9.10458 10 10 9.10457 10 8C10 6.89543 9.10458 6 8.00001 6C6.89544 6 6.00001 6.89543 6.00001 8C6.00001 9.10457 6.89544 10 8.00001 10Z"/>
        </svg>
    </a>
</div>