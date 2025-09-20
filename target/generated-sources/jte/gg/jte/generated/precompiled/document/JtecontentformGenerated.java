package gg.jte.generated.precompiled.document;
@SuppressWarnings("unchecked")
public final class JtecontentformGenerated {
	public static final String JTE_NAME = "document/content-form.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,16,16,16,16,16,16,16,16,16,16,16,16,16,17,17,18,18,18,19,19,23,23,23,0,1,2,2,2,2};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtecontentformGenerated.class, "JtecontentformGenerated.bin", 437,14,1,1,20,48,16,20);
	private static final byte[] TEXT_PART_BINARY_0 = BINARY_CONTENT.get(0);
	private static final byte[] TEXT_PART_BINARY_1 = BINARY_CONTENT.get(1);
	private static final byte[] TEXT_PART_BINARY_2 = BINARY_CONTENT.get(2);
	private static final byte[] TEXT_PART_BINARY_3 = BINARY_CONTENT.get(3);
	private static final byte[] TEXT_PART_BINARY_4 = BINARY_CONTENT.get(4);
	private static final byte[] TEXT_PART_BINARY_5 = BINARY_CONTENT.get(5);
	private static final byte[] TEXT_PART_BINARY_6 = BINARY_CONTENT.get(6);
	private static final byte[] TEXT_PART_BINARY_7 = BINARY_CONTENT.get(7);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String contentPlaceholderText, String content, String error) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		var __jte_html_attribute_0 = contentPlaceholderText;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
			jteOutput.setContext("textarea", "placeholder");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("textarea", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
		jteOutput.setContext("textarea", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
		if (!error.isBlank()) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(error);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String contentPlaceholderText = (String)params.get("contentPlaceholderText");
		String content = (String)params.getOrDefault("content", "");
		String error = (String)params.getOrDefault("error", "");
		render(jteOutput, jteHtmlInterceptor, contentPlaceholderText, content, error);
	}
}
