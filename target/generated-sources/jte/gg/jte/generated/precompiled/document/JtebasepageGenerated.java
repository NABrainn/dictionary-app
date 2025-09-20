package gg.jte.generated.precompiled.document;
import lule.dictionary.controllerAdvice.data.BaseAttribute;
import lule.dictionary.controllerAdvice.data.NavbarAttribute;
import lule.dictionary.documents.data.request.DocumentAttribute;
@SuppressWarnings("unchecked")
public final class JtebasepageGenerated {
	public static final String JTE_NAME = "document/base-page.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,4,4,4,4,4,4,11,11,12,12,13,16,17,18,18,18,19,19,19,4,5,6,6,6,6};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtebasepageGenerated.class, "JtebasepageGenerated.bin", 100,5,5,1);
	private static final byte[] TEXT_PART_BINARY_0 = BINARY_CONTENT.get(0);
	private static final byte[] TEXT_PART_BINARY_1 = BINARY_CONTENT.get(1);
	private static final byte[] TEXT_PART_BINARY_2 = BINARY_CONTENT.get(2);
	private static final byte[] TEXT_PART_BINARY_3 = BINARY_CONTENT.get(3);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, NavbarAttribute navbarAttribute, BaseAttribute baseAttribute, DocumentAttribute attribute) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		gg.jte.generated.precompiled.JtebaseGenerated.render(jteOutput, jteHtmlInterceptor, null, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
				gg.jte.generated.precompiled.navbar.JtenavbarGenerated.render(jteOutput, jteHtmlInterceptor, baseAttribute, navbarAttribute.withIsProfileOpen(false), false);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
				gg.jte.generated.precompiled.document.JtecontentGenerated.render(jteOutput, jteHtmlInterceptor, attribute);
			}
		}, baseAttribute);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		NavbarAttribute navbarAttribute = (NavbarAttribute)params.get("navbarAttribute");
		BaseAttribute baseAttribute = (BaseAttribute)params.get("baseAttribute");
		DocumentAttribute attribute = (DocumentAttribute)params.get("attribute");
		render(jteOutput, jteHtmlInterceptor, navbarAttribute, baseAttribute, attribute);
	}
}
