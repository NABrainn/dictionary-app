package gg.jte.generated.precompiled.auth;
import lule.dictionary.auth.data.localization.AuthText;
import lule.dictionary.controllerAdvice.data.BaseAttribute;
import lule.dictionary.controllerAdvice.data.NavbarAttribute;
import java.util.Map;
@SuppressWarnings("unchecked")
public final class JteloginGenerated {
	public static final String JTE_NAME = "auth/login.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,10,10,11,11,12,14,25,25,25,27,27,27,33,33,34,34,34,35,35,38,38,38,44,44,45,45,45,46,46,51,51,51,57,57,57,59,59,59,59,59,59,59,59,59,61,61,61,63,63,63,5,6,7,8,8,8,8};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JteloginGenerated.class, "JteloginGenerated.bin", 1,5,319,95,235,52,20,106,245,52,20,210,223,61,8,1,14,2);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BaseAttribute baseAttribute, NavbarAttribute navbarAttribute, Map<AuthText, String> localization, Map<String, String> error) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		gg.jte.generated.precompiled.JtebaseGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
				gg.jte.generated.precompiled.navbar.JtenavbarGenerated.render(jteOutput, jteHtmlInterceptor, baseAttribute, navbarAttribute, true);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(localization.get(AuthText.LOG_IN));
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
				jteOutput.setContext("label", null);
				jteOutput.writeUserContent(localization.get(AuthText.USERNAME));
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
				if (error.get("login") != null) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(error.get("login"));
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
				jteOutput.setContext("label", null);
				jteOutput.writeUserContent(localization.get(AuthText.PASSWORD));
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
				if (error.get("password") != null) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(error.get("password"));
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
				jteOutput.setContext("button", null);
				jteOutput.writeUserContent(localization.get(AuthText.SUBMIT));
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
				jteOutput.setContext("a", null);
				jteOutput.writeUserContent(localization.get(AuthText.CREATE_ACCOUNT));
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
				var __jte_html_attribute_0 = baseAttribute._csrf().getToken();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
			}
		}, null, baseAttribute);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BaseAttribute baseAttribute = (BaseAttribute)params.get("baseAttribute");
		NavbarAttribute navbarAttribute = (NavbarAttribute)params.get("navbarAttribute");
		Map<AuthText, String> localization = (Map<AuthText, String>)params.get("localization");
		Map<String, String> error = (Map<String, String>)params.get("error");
		render(jteOutput, jteHtmlInterceptor, baseAttribute, navbarAttribute, localization, error);
	}
}
