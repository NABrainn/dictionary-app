package gg.jte.generated.precompiled.document;
import lule.dictionary.translations.data.Familiarity;
@SuppressWarnings("unchecked")
public final class JtewordGenerated {
	public static final String JTE_NAME = "document/word.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,13,13,16,16,16,16,17,17,17,17,19,19,19,19,19,19,19,19,19,20,20,20,20,20,20,20,20,20,21,21,21,21,21,21,21,21,21,26,26,26,26,26,26,26,26,26,27,27,27,27,27,27,27,27,27,30,30,30,30,34,34,34,34,35,35,35,35,36,36,36,36,37,37,37,37,39,39,39,39,45,45,45,45,49,49,49,49,52,52,52,52,52,52,52,52,53,53,53,55,55,59,59,59,59,59,59,59,59,59,59,59,61,61,61,61,2,3,4,5,6,7,8,9,10,11,12,12,12,12};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtewordGenerated.class, "JtewordGenerated.bin", 26,99,6,10,1,4,13,1,4,19,1,88,16,1,4,18,1,77,117,23,15,49,118,211,116,88,1,43,19,137,1,10,19);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, int wordId, int documentId, String word, String processedWord, String bgColor, String textColor, boolean isSelectable, boolean isPhraseWord, Familiarity familiarity, boolean isPersisted, boolean isWrapped) {
		if (isSelectable) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
			jteOutput.setContext("div", "class");
			jteOutput.writeUserContent(isWrapped ? "pointer-events-none" : "border border-2  border-transparent");
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
			jteOutput.setContext("div", "id");
			jteOutput.writeUserContent(wordId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
			var __jte_html_attribute_0 = wordId;
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
				jteOutput.setContext("div", "data-id");
				jteOutput.writeUserContent(__jte_html_attribute_0);
				jteOutput.setContext("div", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
			var __jte_html_attribute_1 = processedWord;
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
				jteOutput.setContext("div", "data-value");
				jteOutput.writeUserContent(__jte_html_attribute_1);
				jteOutput.setContext("div", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
			var __jte_html_attribute_2 = familiarity != null ? familiarity.name().toLowerCase() : "unknown";
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
				jteOutput.setContext("div", "data-familiarity");
				jteOutput.writeUserContent(__jte_html_attribute_2);
				jteOutput.setContext("div", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
			var __jte_html_attribute_3 = isPersisted  ? "true" : "false";
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
				jteOutput.setContext("div", "data-is-saved");
				jteOutput.writeUserContent(__jte_html_attribute_3);
				jteOutput.setContext("div", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
			var __jte_html_attribute_4 = isWrapped  ? "true" : "false";
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
				jteOutput.setContext("div", "data-is-wrapped");
				jteOutput.writeUserContent(__jte_html_attribute_4);
				jteOutput.setContext("div", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
			jteOutput.setContext("div", "hx-target");
			jteOutput.writeUserContent(wordId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_18);
			jteOutput.setContext("div", "hx-vals");
			jteOutput.writeUserContent(documentId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_19);
			jteOutput.setContext("div", "hx-vals");
			jteOutput.writeUserContent(processedWord);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_20);
			jteOutput.setContext("div", "hx-vals");
			jteOutput.writeUserContent(wordId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_21);
			jteOutput.setContext("div", "hx-vals");
			jteOutput.writeUserContent(wordId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_22);
			jteOutput.setContext("div", "hx-on::after-request");
			jteOutput.writeUserContent(wordId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_23);
			jteOutput.setContext("div", "hx-on:click");
			jteOutput.writeUserContent(wordId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_24);
			jteOutput.setContext("div", "id");
			jteOutput.writeUserContent(wordId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_25);
			jteOutput.setContext("span", "class");
			jteOutput.writeUserContent(bgColor);
			jteOutput.setContext("span", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_26);
			jteOutput.setContext("span", "class");
			jteOutput.writeUserContent(textColor);
			jteOutput.setContext("span", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_27);
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(word);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_28);
		} else {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_29);
			jteOutput.setContext("span", "class");
			jteOutput.writeUserContent(bgColor);
			jteOutput.setContext("span", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_30);
			jteOutput.setContext("span", "class");
			jteOutput.writeUserContent(textColor);
			jteOutput.setContext("span", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_31);
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(word);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_32);
		}
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		int wordId = (int)params.get("wordId");
		int documentId = (int)params.get("documentId");
		String word = (String)params.get("word");
		String processedWord = (String)params.get("processedWord");
		String bgColor = (String)params.getOrDefault("bgColor", "");
		String textColor = (String)params.getOrDefault("textColor", "");
		boolean isSelectable = (boolean)params.getOrDefault("isSelectable", true);
		boolean isPhraseWord = (boolean)params.getOrDefault("isPhraseWord", false);
		Familiarity familiarity = (Familiarity)params.get("familiarity");
		boolean isPersisted = (boolean)params.getOrDefault("isPersisted", false);
		boolean isWrapped = (boolean)params.getOrDefault("isWrapped", false);
		render(jteOutput, jteHtmlInterceptor, wordId, documentId, word, processedWord, bgColor, textColor, isSelectable, isPhraseWord, familiarity, isPersisted, isWrapped);
	}
}
