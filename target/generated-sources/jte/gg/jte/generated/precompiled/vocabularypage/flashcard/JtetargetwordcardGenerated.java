package gg.jte.generated.precompiled.vocabularypage.flashcard;
import lule.dictionary.translations.data.attribute.WordCardAttribute;
@SuppressWarnings("unchecked")
public final class JtetargetwordcardGenerated {
	public static final String JTE_NAME = "vocabulary-page/flashcard/target-word-card.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,13,13,13,13,13,15,15,16,16,16,16,16,16,16,16,16,17,17,18,18,20,20,21,21,21,23,23,23,2,2,2,2};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtetargetwordcardGenerated.class, "JtetargetwordcardGenerated.bin", 258,10,33,8,1,20,5,62,85,13);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, WordCardAttribute attribute) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		jteOutput.setContext("form", "hx-vals");
		jteOutput.writeUserContent(attribute.targetWord());
		jteOutput.setContext("form", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		for (var sw : attribute.sourceWord()) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
			var __jte_html_attribute_0 = sw;
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_0);
				jteOutput.setContext("input", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
		if (attribute.sourceWord().isEmpty()) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(attribute.targetWord());
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		WordCardAttribute attribute = (WordCardAttribute)params.get("attribute");
		render(jteOutput, jteHtmlInterceptor, attribute);
	}
}
