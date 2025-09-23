package gg.jte.generated.precompiled.navbar;
import lule.dictionary.language.service.LanguageData;
import java.util.List;
@SuppressWarnings("unchecked")
public final class JtelanguagepickerGenerated {
	public static final String JTE_NAME = "navbar/language-picker.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,18,18,18,18,18,20,20,20,20,23,23,23,23,23,23,23,23,23,27,27,27,30,30,30,30,32,32,33,33,38,38,38,38,40,40,40,40,45,45,45,45,45,45,45,45,45,48,48,48,48,48,48,48,48,48,49,49,49,51,51,52,52,57,65,65,65,65,65,65,65,65,65,65,65,65,66,66,67,67,71,71,71,71,76,76,76,76,76,76,76,76,76,76,76,76,77,77,78,78,82,82,82,3,4,5,5,5,5};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtelanguagepickerGenerated.class, "JtelanguagepickerGenerated.bin", 485,104,87,6,1,133,58,184,21,247,74,181,13,1,105,6,1,101,59,17,43,164,8,1,1,18,13,131,141,8,1,1,22,9,23);
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
	private static final byte[] TEXT_PART_BINARY_10 = BINARY_CONTENT.get(10);
	private static final byte[] TEXT_PART_BINARY_11 = BINARY_CONTENT.get(11);
	private static final byte[] TEXT_PART_BINARY_12 = BINARY_CONTENT.get(12);
	private static final byte[] TEXT_PART_BINARY_13 = BINARY_CONTENT.get(13);
	private static final byte[] TEXT_PART_BINARY_14 = BINARY_CONTENT.get(14);
	private static final byte[] TEXT_PART_BINARY_15 = BINARY_CONTENT.get(15);
	private static final byte[] TEXT_PART_BINARY_16 = BINARY_CONTENT.get(16);
	private static final byte[] TEXT_PART_BINARY_17 = BINARY_CONTENT.get(17);
	private static final byte[] TEXT_PART_BINARY_18 = BINARY_CONTENT.get(18);
	private static final byte[] TEXT_PART_BINARY_19 = BINARY_CONTENT.get(19);
	private static final byte[] TEXT_PART_BINARY_20 = BINARY_CONTENT.get(20);
	private static final byte[] TEXT_PART_BINARY_21 = BINARY_CONTENT.get(21);
	private static final byte[] TEXT_PART_BINARY_22 = BINARY_CONTENT.get(22);
	private static final byte[] TEXT_PART_BINARY_23 = BINARY_CONTENT.get(23);
	private static final byte[] TEXT_PART_BINARY_24 = BINARY_CONTENT.get(24);
	private static final byte[] TEXT_PART_BINARY_25 = BINARY_CONTENT.get(25);
	private static final byte[] TEXT_PART_BINARY_26 = BINARY_CONTENT.get(26);
	private static final byte[] TEXT_PART_BINARY_27 = BINARY_CONTENT.get(27);
	private static final byte[] TEXT_PART_BINARY_28 = BINARY_CONTENT.get(28);
	private static final byte[] TEXT_PART_BINARY_29 = BINARY_CONTENT.get(29);
	private static final byte[] TEXT_PART_BINARY_30 = BINARY_CONTENT.get(30);
	private static final byte[] TEXT_PART_BINARY_31 = BINARY_CONTENT.get(31);
	private static final byte[] TEXT_PART_BINARY_32 = BINARY_CONTENT.get(32);
	private static final byte[] TEXT_PART_BINARY_33 = BINARY_CONTENT.get(33);
	private static final byte[] TEXT_PART_BINARY_34 = BINARY_CONTENT.get(34);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, LanguageData selectedLanguage, List<LanguageData> languages, String type) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		jteOutput.setContext("button", "onclick");
		jteOutput.writeUserContent(type);
		jteOutput.setContext("button", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		jteOutput.setContext("button", "onclick");
		jteOutput.writeUserContent(type);
		jteOutput.setContext("button", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
		var __jte_html_attribute_0 = selectedLanguage.imgPath();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
			jteOutput.setContext("img", "src");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("img", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(selectedLanguage.languageCode().toUpperCase());
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
		jteOutput.setContext("div", "id");
		jteOutput.writeUserContent(type);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
		for (var language : languages) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
			if (!language.languageCode().equals(selectedLanguage.languageCode())) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
				jteOutput.setContext("div", "hx-get");
				jteOutput.writeUserContent(type);
				jteOutput.setContext("div", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
				jteOutput.setContext("div", "hx-vals");
				jteOutput.writeUserContent(language.language());
				jteOutput.setContext("div", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
				var __jte_html_attribute_1 = language.fullName();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
					jteOutput.setContext("div", "data-value");
					jteOutput.writeUserContent(__jte_html_attribute_1);
					jteOutput.setContext("div", null);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
				var __jte_html_attribute_2 = language.imgPath();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
					jteOutput.setContext("img", "src");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("img", null);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(language.languageCode().toUpperCase());
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_18);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_19);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_20);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_21);
		var __jte_html_attribute_3 = selectedLanguage.fullName();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_22);
			jteOutput.setContext("option", "value");
			jteOutput.writeUserContent(__jte_html_attribute_3);
			jteOutput.setContext("option", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_23);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_24);
		jteOutput.setContext("option", null);
		jteOutput.writeUserContent(selectedLanguage.languageCode().toUpperCase());
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_25);
		for (var language : languages) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_26);
			if (!language.languageCode().equals(selectedLanguage)) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_27);
				jteOutput.setContext("option", "hx-vals");
				jteOutput.writeUserContent(language.language());
				jteOutput.setContext("option", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_28);
				var __jte_html_attribute_4 = language.fullName();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_29);
					jteOutput.setContext("option", "value");
					jteOutput.writeUserContent(__jte_html_attribute_4);
					jteOutput.setContext("option", null);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_30);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_31);
				jteOutput.setContext("option", null);
				jteOutput.writeUserContent(language.languageCode().toUpperCase());
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_32);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_33);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_34);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		LanguageData selectedLanguage = (LanguageData)params.get("selectedLanguage");
		List<LanguageData> languages = (List<LanguageData>)params.get("languages");
		String type = (String)params.get("type");
		render(jteOutput, jteHtmlInterceptor, selectedLanguage, languages, type);
	}
}
