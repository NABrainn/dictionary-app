package gg.jte.generated.precompiled.translation;
import lule.dictionary.controllerAdvice.data.BaseAttribute;
import lule.dictionary.translations.data.TranslationLocalizationKey;
import lule.dictionary.translations.data.attribute.PhraseAttribute;
import java.util.Map;
@SuppressWarnings("unchecked")
public final class JtenewphraseGenerated {
	public static final String JTE_NAME = "translation/new-phrase.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,11,11,11,11,11,13,13,13,13,13,13,13,13,13,14,14,14,14,14,14,14,14,24,24,24,24,27,27,27,27,28,28,28,28,29,29,29,29,34,34,34,34,44,44,44,44,66,66,66,66,68,68,68,68,68,68,68,68,68,71,75,77,77,78,85,86,86,87,87,87,5,6,7,8,8,8,8};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtenewphraseGenerated.class, "JtenewphraseGenerated.bin", 139,2,10,1,13,1,249,52,22,14,174,334,716,6,10,1,97,16,9,5,7);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BaseAttribute baseAttribute, PhraseAttribute attribute, Map<TranslationLocalizationKey, String> messages, Map<String, String> errors) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		jteOutput.setContext("div", "id");
		jteOutput.writeUserContent(attribute.phraseAttribute().id());
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		var __jte_html_attribute_0 = attribute.phraseAttribute().id();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
			jteOutput.setContext("div", "data-id");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
		}
		var __jte_html_attribute_1 = attribute.phraseAttribute().translation().targetWord().toLowerCase();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
			jteOutput.setContext("div", "data-value");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
		jteOutput.setContext("div", "hx-target");
		jteOutput.writeUserContent(attribute.phraseAttribute().id());
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
		jteOutput.setContext("div", "hx-vals");
		jteOutput.writeUserContent(attribute.phraseAttribute().documentId());
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
		jteOutput.setContext("div", "hx-vals");
		jteOutput.writeUserContent(attribute.phraseAttribute().translation().targetWord());
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
		jteOutput.setContext("div", "hx-vals");
		jteOutput.writeUserContent(attribute.phraseAttribute().id());
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
		jteOutput.setContext("div", "hx-on::load");
		jteOutput.writeUserContent(attribute.phraseAttribute().id());
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
		jteOutput.setContext("div", "hx-on:click");
		jteOutput.writeUserContent(attribute.phraseAttribute().id());
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
		jteOutput.setContext("div", "id");
		jteOutput.writeUserContent(attribute.phraseAttribute().id());
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
		var __jte_html_attribute_2 = attribute.phraseAttribute().id();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
			jteOutput.setContext("div", "data-id");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
		gg.jte.generated.precompiled.translation.JteaddtranslationformGenerated.render(jteOutput, jteHtmlInterceptor, baseAttribute, attribute.phraseAttribute(), errors, messages);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
		for (int wordId = 0; wordId < attribute.phrasePartsAttribute().size(); wordId++) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_18);
			gg.jte.generated.precompiled.document.JtewordGenerated.render(jteOutput, jteHtmlInterceptor, attribute.phrasePartsAttribute().get(wordId).id(), attribute.phraseAttribute().documentId(), attribute.phrasePartsAttribute().get(wordId).translation().unprocessedTargetWord(), attribute.phrasePartsAttribute().get(wordId).translation().targetWord(), "", "", true, false, attribute.phrasePartsAttribute().get(wordId).translation().familiarity(), attribute.phrasePartsAttribute().get(wordId).isPersisted(), true);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_19);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_20);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BaseAttribute baseAttribute = (BaseAttribute)params.get("baseAttribute");
		PhraseAttribute attribute = (PhraseAttribute)params.get("attribute");
		Map<TranslationLocalizationKey, String> messages = (Map<TranslationLocalizationKey, String>)params.get("messages");
		Map<String, String> errors = (Map<String, String>)params.get("errors");
		render(jteOutput, jteHtmlInterceptor, baseAttribute, attribute, messages, errors);
	}
}
