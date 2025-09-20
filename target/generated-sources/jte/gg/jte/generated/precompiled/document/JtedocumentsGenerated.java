package gg.jte.generated.precompiled.document;
import lule.dictionary.controllerAdvice.data.BaseAttribute;
import lule.dictionary.controllerAdvice.data.NavbarAttribute;
import lule.dictionary.documents.data.DocumentLocalizationKey;
import lule.dictionary.documents.data.attribute.DocumentListAttribute;
@SuppressWarnings("unchecked")
public final class JtedocumentsGenerated {
	public static final String JTE_NAME = "document/documents.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,9,9,10,10,11,13,14,14,18,18,20,20,20,26,26,26,26,26,26,29,29,33,33,36,36,36,36,43,43,43,43,50,50,50,52,52,52,52,52,52,53,53,53,53,53,53,54,54,54,54,54,54,55,55,55,55,55,55,58,58,61,61,76,76,76,79,79,80,80,80,81,81,81,5,6,7,7,7,7};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtedocumentsGenerated.class, "JtedocumentsGenerated.bin", 1,5,5,144,124,309,5,76,137,185,305,377,113,41,57,41,57,41,57,41,113,45,675,49,1,1);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, NavbarAttribute navbarAttribute, BaseAttribute baseAttribute, DocumentListAttribute attribute) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		gg.jte.generated.precompiled.JtebaseGenerated.render(jteOutput, jteHtmlInterceptor, null, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
				gg.jte.generated.precompiled.navbar.JtenavbarGenerated.render(jteOutput, jteHtmlInterceptor, baseAttribute, navbarAttribute, true);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
				if (baseAttribute.isAuthenticated()) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
					if (attribute.documents().isEmpty()) {
						jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
						jteOutput.setContext("span", null);
						jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.THERE_ARE_NO_DOCUMENTS_IN_THE_LIBRARY));
						jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
						jteOutput.setContext("a", null);
						jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.CLICK_HERE));
						jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
						jteOutput.setContext("span", null);
						jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.TO_ADD_YOUR_FIRST));
						jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
					} else {
						jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
						for (var item : attribute.documents()) {
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
							jteOutput.setContext("div", "hx-get");
							jteOutput.writeUserContent(item.id());
							jteOutput.setContext("div", null);
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
							jteOutput.setContext("div", "hx-push-url");
							jteOutput.writeUserContent(item.id());
							jteOutput.setContext("div", null);
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
							jteOutput.setContext("p", null);
							jteOutput.writeUserContent(item.title());
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.WORDS_TOTAL));
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(item.wordCount());
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.NEW_WORDS));
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(item.newWordCount());
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.TRANSLATIONS));
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(item.translationCount());
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_18);
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.AUTHOR));
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_19);
							jteOutput.setContext("span", null);
							jteOutput.writeUserContent(item.owner());
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_20);
						}
						jteOutput.writeBinaryContent(TEXT_PART_BINARY_21);
					}
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_22);
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.ADD_BOOK));
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_23);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_24);
			}
		}, baseAttribute);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_25);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		NavbarAttribute navbarAttribute = (NavbarAttribute)params.get("navbarAttribute");
		BaseAttribute baseAttribute = (BaseAttribute)params.get("baseAttribute");
		DocumentListAttribute attribute = (DocumentListAttribute)params.get("attribute");
		render(jteOutput, jteHtmlInterceptor, navbarAttribute, baseAttribute, attribute);
	}
}
