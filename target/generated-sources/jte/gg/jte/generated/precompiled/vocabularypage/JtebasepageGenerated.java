package gg.jte.generated.precompiled.vocabularypage;
import lule.dictionary.controllerAdvice.data.BaseAttribute;
import lule.dictionary.controllerAdvice.data.NavbarAttribute;
import lule.dictionary.translations.data.TranslationLocalizationKey;
import lule.dictionary.translations.data.attribute.FlashcardConfigAttribute;
import java.util.Map;
@SuppressWarnings("unchecked")
public final class JtebasepageGenerated {
	public static final String JTE_NAME = "vocabulary-page/base-page.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,6,6,6,6,6,6,6,6,6,13,13,14,14,15,18,19,21,21,21,22,22,22,6,7,8,9,9,9,9};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtebasepageGenerated.class, "JtebasepageGenerated.bin", 3,1,1,1);
	private static final byte[] TEXT_PART_BINARY_0 = BINARY_CONTENT.get(0);
	private static final byte[] TEXT_PART_BINARY_1 = BINARY_CONTENT.get(1);
	private static final byte[] TEXT_PART_BINARY_2 = BINARY_CONTENT.get(2);
	private static final byte[] TEXT_PART_BINARY_3 = BINARY_CONTENT.get(3);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BaseAttribute baseAttribute, NavbarAttribute navbarAttribute, FlashcardConfigAttribute attribute, Map<TranslationLocalizationKey, String> messages) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		gg.jte.generated.precompiled.JtebaseGenerated.render(jteOutput, jteHtmlInterceptor, null, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
				gg.jte.generated.precompiled.navbar.JtenavbarGenerated.render(jteOutput, jteHtmlInterceptor, baseAttribute, navbarAttribute, false);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
				gg.jte.generated.precompiled.vocabularypage.flashcard.JteflashcardconfigGenerated.render(jteOutput, jteHtmlInterceptor, attribute, messages);
			}
		}, baseAttribute);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BaseAttribute baseAttribute = (BaseAttribute)params.get("baseAttribute");
		NavbarAttribute navbarAttribute = (NavbarAttribute)params.get("navbarAttribute");
		FlashcardConfigAttribute attribute = (FlashcardConfigAttribute)params.get("attribute");
		Map<TranslationLocalizationKey, String> messages = (Map<TranslationLocalizationKey, String>)params.get("messages");
		render(jteOutput, jteHtmlInterceptor, baseAttribute, navbarAttribute, attribute, messages);
	}
}
