package gg.jte.generated.precompiled.navbar;
import lule.dictionary.controllerAdvice.data.NavbarAttribute;
@SuppressWarnings("unchecked")
public final class JteprofilepanelGenerated {
	public static final String JTE_NAME = "navbar/profile-panel.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,2,2,2,2,2,2,2,5,5,5,7,11,12,18,20,20,21,21,21,2,3,3,3,3};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JteprofilepanelGenerated.class, "JteprofilepanelGenerated.bin", 1,96,9,12,1);
	private static final byte[] TEXT_PART_BINARY_0 = BINARY_CONTENT.get(0);
	private static final byte[] TEXT_PART_BINARY_1 = BINARY_CONTENT.get(1);
	private static final byte[] TEXT_PART_BINARY_2 = BINARY_CONTENT.get(2);
	private static final byte[] TEXT_PART_BINARY_3 = BINARY_CONTENT.get(3);
	private static final byte[] TEXT_PART_BINARY_4 = BINARY_CONTENT.get(4);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, NavbarAttribute navbarAttribute, boolean isProfileOpen) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		if (isProfileOpen) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
			gg.jte.generated.precompiled.navbar.JtelanguagepickerGenerated.render(jteOutput, jteHtmlInterceptor, navbarAttribute.targetLanguage(), navbarAttribute.languageDataList(), "target");
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
			gg.jte.generated.precompiled.navbar.JtestatsGenerated.render(jteOutput, jteHtmlInterceptor, navbarAttribute.wordsLearned(), navbarAttribute.dailyStreak(), navbarAttribute.wordsLearnedText(), navbarAttribute.daysSingularText(), navbarAttribute.daysPluralText());
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		NavbarAttribute navbarAttribute = (NavbarAttribute)params.get("navbarAttribute");
		boolean isProfileOpen = (boolean)params.get("isProfileOpen");
		render(jteOutput, jteHtmlInterceptor, navbarAttribute, isProfileOpen);
	}
}
