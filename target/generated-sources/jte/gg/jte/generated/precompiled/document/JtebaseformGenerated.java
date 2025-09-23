package gg.jte.generated.precompiled.document;
import lule.dictionary.controllerAdvice.data.BaseAttribute;
import lule.dictionary.documents.data.DocumentLocalizationKey;
import lule.dictionary.documents.data.documentSubmission.ContentSubmissionStrategy;
import lule.dictionary.documents.data.attribute.DocumentFormAttribute;
import lule.dictionary.controllerAdvice.data.NavbarAttribute;
import lule.dictionary.documents.data.documentSubmission.UrlSubmissionStrategy;
import java.util.Map;
@SuppressWarnings("unchecked")
public final class JtebaseformGenerated {
	public static final String JTE_NAME = "document/base-form.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,5,6,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,13,13,14,14,16,18,45,45,45,45,45,45,45,45,45,47,47,47,56,59,59,59,59,59,60,60,61,61,61,62,62,65,65,65,67,67,80,80,80,93,93,93,94,94,107,107,107,120,120,120,121,121,124,124,125,128,129,129,130,133,134,134,139,139,139,143,143,143,144,144,144,8,9,10,11,11,11,11};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtebaseformGenerated.class, "JtebaseformGenerated.bin", 1,46,1045,8,1,130,370,28,56,24,127,78,622,604,30,611,615,30,41,21,17,21,17,182,60,1);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, DocumentFormAttribute attribute, NavbarAttribute navbarAttribute, BaseAttribute baseAttribute, Map<String, String> errors) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		gg.jte.generated.precompiled.JtebaseGenerated.render(jteOutput, jteHtmlInterceptor, null, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
				gg.jte.generated.precompiled.navbar.JtenavbarGenerated.render(jteOutput, jteHtmlInterceptor, baseAttribute, navbarAttribute, true);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
				var __jte_html_attribute_0 = baseAttribute._csrf().getToken();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
				jteOutput.setContext("label", null);
				jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.TITLE));
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
				jteOutput.setContext("textarea", null);
				jteOutput.writeUserContent(switch (attribute.submissionStrategy()){
                    case ContentSubmissionStrategy content -> content.title();
                    case UrlSubmissionStrategy url -> url.title();
                });
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
				if (errors.get("title") != null) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(errors.get("title"));
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.CONTENT));
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
				if (attribute.submissionStrategy() instanceof UrlSubmissionStrategy) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
					jteOutput.setContext("button", null);
					jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.IMPORT_BY_URL));
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
					jteOutput.setContext("button", null);
					jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.INSERT_MANUALLY));
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
				} else {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
					jteOutput.setContext("button", null);
					jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.IMPORT_BY_URL));
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
					jteOutput.setContext("button", null);
					jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.INSERT_MANUALLY));
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_18);
				if (attribute.submissionStrategy() instanceof ContentSubmissionStrategy content) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_19);
					gg.jte.generated.precompiled.document.JtecontentformGenerated.render(jteOutput, jteHtmlInterceptor, content.contentPlaceholderText(), content.content(), errors.getOrDefault("content", ""));
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_20);
				} else if (attribute.submissionStrategy() instanceof UrlSubmissionStrategy url) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_21);
					gg.jte.generated.precompiled.document.JteurlformGenerated.render(jteOutput, jteHtmlInterceptor, url.urlPlaceholderText(), errors.getOrDefault("url", ""), url.url());
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_22);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_23);
				jteOutput.setContext("button", null);
				jteOutput.writeUserContent(attribute.localization().get(DocumentLocalizationKey.SUBMIT));
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_24);
			}
		}, baseAttribute);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_25);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		DocumentFormAttribute attribute = (DocumentFormAttribute)params.get("attribute");
		NavbarAttribute navbarAttribute = (NavbarAttribute)params.get("navbarAttribute");
		BaseAttribute baseAttribute = (BaseAttribute)params.get("baseAttribute");
		Map<String, String> errors = (Map<String, String>)params.get("errors");
		render(jteOutput, jteHtmlInterceptor, attribute, navbarAttribute, baseAttribute, errors);
	}
}
