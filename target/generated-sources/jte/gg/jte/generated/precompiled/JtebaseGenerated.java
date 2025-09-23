package gg.jte.generated.precompiled;
import gg.jte.Content;
import lule.dictionary.controllerAdvice.data.BaseAttribute;
@SuppressWarnings("unchecked")
public final class JtebaseGenerated {
	public static final String JTE_NAME = "base.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,8,8,8,9,9,19,19,20,20,34,34,35,35,35,36,36,37,37,38,38,38,39,39,41,41,41,2,3,4,4,4,4};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtebaseGenerated.class, "JtebaseGenerated.bin", 54,13,460,9,520,13,9,9,13,9,20);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Content authView, Content content, BaseAttribute baseAttribute) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		if (baseAttribute != null) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
			if (!baseAttribute.isAuthenticated()) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
		if (authView != null) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
			jteOutput.setContext("body", null);
			jteOutput.writeUserContent(authView);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
		if (content != null) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
			jteOutput.setContext("body", null);
			jteOutput.writeUserContent(content);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Content authView = (Content)params.getOrDefault("authView", null);
		Content content = (Content)params.getOrDefault("content", null);
		BaseAttribute baseAttribute = (BaseAttribute)params.get("baseAttribute");
		render(jteOutput, jteHtmlInterceptor, authView, content, baseAttribute);
	}
}
