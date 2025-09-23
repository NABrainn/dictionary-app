package gg.jte.generated.precompiled.translation;
import gg.jte.support.ForSupport;
import lule.dictionary.translations.data.Translation;
import lule.dictionary.documents.data.selectable.Phrase;
import java.util.Map;
import java.util.regex.Pattern;
@SuppressWarnings("unchecked")
public final class JteupdatephraseGenerated {
	public static final String JTE_NAME = "translation/update-phrase.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,12,12,17,17,17,17,17,17,18,23,23,23,23,23,23,24,29,29,29,29,29,29,31,31,31,31,31,31,31,31,33,33,33,33,33,33,33,33,33,34,34,34,34,34,34,34,34,35,35,35,35,35,35,35,35,43,43,43,43,47,47,47,47,48,48,48,48,49,49,49,49,58,58,58,58,62,62,62,62,65,65,66,66,67,70,70,70,70,71,71,72,72,73,80,81,81,82,89,90,90,91,91,92,100,101,101,102,102,104,104,104,6,7,8,9,10,10,10,10};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JteupdatephraseGenerated.class, "JteupdatephraseGenerated.bin", 1,1,1,104,1,2,10,1,13,1,19,1,164,110,22,14,232,100,64,9,9,9,13,17,13,17,13,9,13,9,5,8);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Phrase phrase, Pattern cleanWordPattern, int selectableId, int documentId, Map<String, Translation> translations) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		var processedWord = String.join(" ", phrase.targetWords().stream()
                                                .map(word -> cleanWordPattern.matcher(word)
                                                        .replaceAll("")
                                                        .replace("-", " ")
                                                        .toLowerCase())
                                                .toList());
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		var wordBgColor = switch (phrase.familiarity()) {
    case UNKNOWN -> "bg-accent";
    case RECOGNIZED -> "bg-accent/80";
    case FAMILIAR -> "bg-accent/60";
    default -> "bg-primary";
};
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
		var wordTextColor = switch (phrase.familiarity()) {
    case UNKNOWN -> "text-primary";
    case RECOGNIZED -> "text-primary";
    case FAMILIAR -> "text-primary";
    default -> "text-neutral";
};
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
		jteOutput.setContext("div", "class");
		jteOutput.writeUserContent(wordTextColor);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
		jteOutput.setContext("div", "class");
		jteOutput.writeUserContent(wordBgColor);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
		var __jte_html_attribute_0 = selectableId;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
			jteOutput.setContext("div", "data-id");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
		}
		var __jte_html_attribute_1 = processedWord;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
			jteOutput.setContext("div", "data-value");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
		}
		var __jte_html_attribute_2 = phrase.familiarity() != null ? phrase.familiarity().name().toLowerCase() : null;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
			jteOutput.setContext("div", "data-familiarity");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
		jteOutput.setContext("div", "hx-target");
		jteOutput.writeUserContent(selectableId);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
		jteOutput.setContext("div", "hx-vals");
		jteOutput.writeUserContent(documentId);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
		jteOutput.setContext("div", "hx-vals");
		jteOutput.writeUserContent(processedWord);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
		jteOutput.setContext("div", "hx-vals");
		jteOutput.writeUserContent(selectableId);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
		jteOutput.setContext("div", "hx-on:click");
		jteOutput.writeUserContent(selectableId);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
		jteOutput.setContext("div", "id");
		jteOutput.writeUserContent(selectableId);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_18);
		for (var phraseEntry : ForSupport.of(phrase.targetWords())) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_19);
			var word = phraseEntry.get();
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_20);
			var cleanWord = cleanWordPattern.matcher(word)
                                                .replaceAll("")
                                                .replace("-", " ")
                                                .toLowerCase();
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_21);
			if (!cleanWord.isEmpty()) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_22);
				if (translations.get(cleanWord) != null) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_23);
					gg.jte.generated.precompiled.document.JtewordGenerated.render(jteOutput, jteHtmlInterceptor, -1, documentId, word, cleanWord, "", "", false, false, translations.get(cleanWord).familiarity(), false, true);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_24);
				} else {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_25);
					gg.jte.generated.precompiled.document.JtewordGenerated.render(jteOutput, jteHtmlInterceptor, -1, documentId, word, cleanWord, "", "", true, false, phrase.familiarity(), false, true);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_26);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_27);
			} else {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_28);
				gg.jte.generated.precompiled.document.JtewordGenerated.render(jteOutput, jteHtmlInterceptor, -1, documentId, word, cleanWord, "", wordTextColor, false, false, phrase.familiarity(), false, true);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_29);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_30);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_31);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Phrase phrase = (Phrase)params.get("phrase");
		Pattern cleanWordPattern = (Pattern)params.get("cleanWordPattern");
		int selectableId = (int)params.get("selectableId");
		int documentId = (int)params.get("documentId");
		Map<String, Translation> translations = (Map<String, Translation>)params.get("translations");
		render(jteOutput, jteHtmlInterceptor, phrase, cleanWordPattern, selectableId, documentId, translations);
	}
}
