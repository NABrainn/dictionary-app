package gg.jte.generated.precompiled.navbar;
import lule.dictionary.language.service.LanguageData;
import java.util.List;
@SuppressWarnings("unchecked")
public final class JtesettingsGenerated {
	public static final String JTE_NAME = "navbar/settings.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,15,15,15,15,15,28,28,28,34,34,34,37,37,37,39,42,46,46,46,48,51,66,66,66,66,68,68,68,83,83,83,3,4,5,6,7,9,10,11,12,13,13,13,13};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtesettingsGenerated.class, "JtesettingsGenerated.bin", 64,1355,262,183,67,181,67,442,34,1254);
	private static final byte[] TEXT_PART_BINARY_0 = BINARY_CONTENT.get(0);
	private static final byte[] TEXT_PART_BINARY_1 = BINARY_CONTENT.get(1);
	private static final byte[] TEXT_PART_BINARY_2 = BINARY_CONTENT.get(2);
	private static final byte[] TEXT_PART_BINARY_3 = BINARY_CONTENT.get(3);
	private static final byte[] TEXT_PART_BINARY_4 = BINARY_CONTENT.get(4);
	private static final byte[] TEXT_PART_BINARY_5 = BINARY_CONTENT.get(5);
	private static final byte[] TEXT_PART_BINARY_6 = BINARY_CONTENT.get(6);
	private static final byte[] TEXT_PART_BINARY_7 = BINARY_CONTENT.get(7);
	private static final byte[] TEXT_PART_BINARY_8 = BINARY_CONTENT.get(8);
	private static final byte[] TEXT_PART_BINARY_9 = BINARY_CONTENT.get(9);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String settingsText, String languageText, String uiText, String translationsText, String logoutText, String size, LanguageData userInterfaceLanguage, LanguageData translationLanguage, List<LanguageData> languages, String _csrf) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		jteOutput.setContext("div", "class");
		jteOutput.writeUserContent(size);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(settingsText);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(languageText);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(uiText);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
		gg.jte.generated.precompiled.navbar.JtelanguagepickerGenerated.render(jteOutput, jteHtmlInterceptor, userInterfaceLanguage, languages, "ui");
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(translationsText);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
		gg.jte.generated.precompiled.navbar.JtelanguagepickerGenerated.render(jteOutput, jteHtmlInterceptor, translationLanguage, languages, "source");
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
		jteOutput.setContext("a", "hx-vals");
		jteOutput.writeUserContent(_csrf);
		jteOutput.setContext("a", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(logoutText);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String settingsText = (String)params.get("settingsText");
		String languageText = (String)params.get("languageText");
		String uiText = (String)params.get("uiText");
		String translationsText = (String)params.get("translationsText");
		String logoutText = (String)params.get("logoutText");
		String size = (String)params.get("size");
		LanguageData userInterfaceLanguage = (LanguageData)params.get("userInterfaceLanguage");
		LanguageData translationLanguage = (LanguageData)params.get("translationLanguage");
		List<LanguageData> languages = (List<LanguageData>)params.get("languages");
		String _csrf = (String)params.get("_csrf");
		render(jteOutput, jteHtmlInterceptor, settingsText, languageText, uiText, translationsText, logoutText, size, userInterfaceLanguage, translationLanguage, languages, _csrf);
	}
}
