package gg.jte.generated.precompiled.document;
@SuppressWarnings("unchecked")
public final class JteurlformGenerated {
	public static final String JTE_NAME = "document/url-form.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,18,18,18,18,18,18,18,18,18,18,18,18,18,20,20,21,21,21,22,22,24,24,24,0,1,2,2,2,2};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JteurlformGenerated.class, "JteurlformGenerated.bin", 485,14,1,1,64,48,16,18);
	private static final byte[] TEXT_PART_BINARY_0 = BINARY_CONTENT.get(0);
	private static final byte[] TEXT_PART_BINARY_1 = BINARY_CONTENT.get(1);
	private static final byte[] TEXT_PART_BINARY_2 = BINARY_CONTENT.get(2);
	private static final byte[] TEXT_PART_BINARY_3 = BINARY_CONTENT.get(3);
	private static final byte[] TEXT_PART_BINARY_4 = BINARY_CONTENT.get(4);
	private static final byte[] TEXT_PART_BINARY_5 = BINARY_CONTENT.get(5);
	private static final byte[] TEXT_PART_BINARY_6 = BINARY_CONTENT.get(6);
	private static final byte[] TEXT_PART_BINARY_7 = BINARY_CONTENT.get(7);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String urlPlaceholderText, String error, String content) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		var __jte_html_attribute_0 = urlPlaceholderText;
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
		String urlPlaceholderText = (String)params.get("urlPlaceholderText");
		String error = (String)params.getOrDefault("error", "");
		String content = (String)params.getOrDefault("content", "");
		render(jteOutput, jteHtmlInterceptor, urlPlaceholderText, error, content);
	}
}
