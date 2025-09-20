package gg.jte.generated.precompiled.navbar;
@SuppressWarnings("unchecked")
public final class JteloginbtnGenerated {
	public static final String JTE_NAME = "navbar/login-btn.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,0,0,3,3,3,3,3,11,11,11,24,24,24,0,1,1,1,1};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JteloginbtnGenerated.class, "JteloginbtnGenerated.bin", 30,213,1145);
	private static final byte[] TEXT_PART_BINARY_0 = BINARY_CONTENT.get(0);
	private static final byte[] TEXT_PART_BINARY_1 = BINARY_CONTENT.get(1);
	private static final byte[] TEXT_PART_BINARY_2 = BINARY_CONTENT.get(2);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String text, String size) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		jteOutput.setContext("div", "class");
		jteOutput.writeUserContent(size);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(text);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String text = (String)params.get("text");
		String size = (String)params.get("size");
		render(jteOutput, jteHtmlInterceptor, text, size);
	}
}
