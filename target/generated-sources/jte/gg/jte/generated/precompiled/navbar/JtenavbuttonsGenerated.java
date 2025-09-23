package gg.jte.generated.precompiled.navbar;
@SuppressWarnings("unchecked")
public final class JtenavbuttonsGenerated {
	public static final String JTE_NAME = "navbar/nav-buttons.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,21,21,21,109,109,109,183,183,183,0,1,2,2,2,2};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtenavbuttonsGenerated.class, "JtenavbuttonsGenerated.bin", 72,547,4976,4343);
	private static final byte[] TEXT_PART_BINARY_0 = BINARY_CONTENT.get(0);
	private static final byte[] TEXT_PART_BINARY_1 = BINARY_CONTENT.get(1);
	private static final byte[] TEXT_PART_BINARY_2 = BINARY_CONTENT.get(2);
	private static final byte[] TEXT_PART_BINARY_3 = BINARY_CONTENT.get(3);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String lessons, String vocabulary, String size) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		jteOutput.setContext("div", "class");
		jteOutput.writeUserContent(size);
		jteOutput.setContext("div", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(lessons);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(vocabulary);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String lessons = (String)params.get("lessons");
		String vocabulary = (String)params.get("vocabulary");
		String size = (String)params.get("size");
		render(jteOutput, jteHtmlInterceptor, lessons, vocabulary, size);
	}
}
