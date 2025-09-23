package gg.jte.generated.precompiled.vocabularypage.flashcard;
import lule.dictionary.translations.data.attribute.BaseFlashcardAttribute;
import java.util.List;
import lule.dictionary.translations.data.attribute.WordCardAttribute;
@SuppressWarnings("unchecked")
public final class JteflashcardGenerated {
	public static final String JTE_NAME = "vocabulary-page/flashcard/flashcard.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,6,6,6,7,7,8,8,9,9,10,10,11,11,12,12,13,13,14,14,21,21,23,23,31,31,31,31,33,33,34,34,34,34,34,34,34,34,34,35,35,37,37,39,39,40,40,40,40,40,40,41,41,49,49,49,49,51,51,52,52,52,52,52,52,52,52,52,53,53,55,55,63,63,63,63,64,64,64,64,65,65,65,65,68,68,69,69,69,69,69,69,69,69,69,70,70,72,72,77,77,77,4,4,4,4};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JteflashcardGenerated.class, "JteflashcardGenerated.bin", 1,1,1,5,5,1,5,5,1,100,119,281,40,45,8,1,32,29,82,46,1,17,281,40,45,8,1,32,29,286,32,31,64,45,8,1,32,29,22);
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
	private static final byte[] TEXT_PART_BINARY_35 = BINARY_CONTENT.get(35);
	private static final byte[] TEXT_PART_BINARY_36 = BINARY_CONTENT.get(36);
	private static final byte[] TEXT_PART_BINARY_37 = BINARY_CONTENT.get(37);
	private static final byte[] TEXT_PART_BINARY_38 = BINARY_CONTENT.get(38);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BaseFlashcardAttribute attribute) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		String targetWord;
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		List<String> sourceWord;
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
		if (!attribute.translations().isEmpty()) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
			targetWord = attribute.translations().get(attribute.id() - 1).targetWord();
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
			sourceWord = attribute.translations().get(attribute.id() - 1).sourceWords();
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
		} else {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
			targetWord = "";
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
			sourceWord = List.of();
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
		gg.jte.generated.precompiled.vocabularypage.flashcard.JtetargetwordcardGenerated.render(jteOutput, jteHtmlInterceptor, WordCardAttribute.of(sourceWord, targetWord));
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
		if (attribute.id() > 1) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
			jteOutput.setContext("form", "hx-vals");
			jteOutput.writeUserContent(attribute.id() - 1);
			jteOutput.setContext("form", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
			for (var sw : sourceWord) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
				var __jte_html_attribute_0 = sw;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
		} else {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_18);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_19);
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(attribute.id());
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_20);
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(attribute.size());
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_21);
		if (attribute.id() < attribute.size()) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_22);
			jteOutput.setContext("form", "hx-vals");
			jteOutput.writeUserContent(attribute.id() + 1);
			jteOutput.setContext("form", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_23);
			for (var sw : sourceWord) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_24);
				var __jte_html_attribute_1 = sw;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_25);
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_1);
					jteOutput.setContext("input", null);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_26);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_27);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_28);
		} else {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_29);
			jteOutput.setContext("form", "hx-vals");
			jteOutput.writeUserContent(attribute.familiarity());
			jteOutput.setContext("form", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_30);
			jteOutput.setContext("form", "hx-vals");
			jteOutput.writeUserContent(attribute.quantity());
			jteOutput.setContext("form", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_31);
			jteOutput.setContext("form", "hx-vals");
			jteOutput.writeUserContent(attribute.isPhrase());
			jteOutput.setContext("form", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_32);
			for (var sw : sourceWord) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_33);
				var __jte_html_attribute_2 = sw;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_34);
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("input", null);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_35);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_36);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_37);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_38);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BaseFlashcardAttribute attribute = (BaseFlashcardAttribute)params.get("attribute");
		render(jteOutput, jteHtmlInterceptor, attribute);
	}
}
