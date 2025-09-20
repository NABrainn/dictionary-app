package gg.jte.generated.precompiled.navbar;
import lule.dictionary.controllerAdvice.data.BaseAttribute;
import lule.dictionary.controllerAdvice.data.NavbarAttribute;
@SuppressWarnings("unchecked")
public final class JtenavbarGenerated {
	public static final String JTE_NAME = "navbar/navbar.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,7,7,7,8,8,9,9,10,10,11,11,12,12,13,13,14,14,15,15,21,24,25,25,26,26,27,27,28,28,29,39,40,40,41,43,44,44,46,46,49,51,53,53,55,55,55,3,4,5,5,5,5};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtenavbarGenerated.class, "JtenavbarGenerated.bin", 1,1,1,5,5,1,5,5,1,142,9,13,17,13,13,9,13,9,16,54,20,8);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BaseAttribute baseAttribute, NavbarAttribute navbarAttribute, boolean isProfileToggleBtnVisible) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		var navButtonsSize = "";
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		var authButtonsSize = "";
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
		if (isProfileToggleBtnVisible) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
			navButtonsSize = "basis-2/5";
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
			authButtonsSize = "basis-2/5";
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
		} else {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
			navButtonsSize = "basis-1/2";
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
			authButtonsSize = "basis-1/2";
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
		gg.jte.generated.precompiled.navbar.JtenavbuttonsGenerated.render(jteOutput, jteHtmlInterceptor, navbarAttribute.lessonsBtnText(), navbarAttribute.vocabularyBtnText(), navButtonsSize);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
		if (baseAttribute.isAuthenticated()) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
			if (isProfileToggleBtnVisible) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
				gg.jte.generated.precompiled.navbar.JtepaneltogglebtnGenerated.render(jteOutput, jteHtmlInterceptor);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
			gg.jte.generated.precompiled.navbar.JtesettingsGenerated.render(jteOutput, jteHtmlInterceptor, navbarAttribute.settingsText(), navbarAttribute.languageText(), navbarAttribute.uiText(), navbarAttribute.translationsText(), navbarAttribute.logoutText(), authButtonsSize, navbarAttribute.userInterfaceLanguage(), navbarAttribute.translationLanguage(), navbarAttribute.languageDataList(), baseAttribute._csrf().getToken());
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
		} else {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
			gg.jte.generated.precompiled.navbar.JteloginbtnGenerated.render(jteOutput, jteHtmlInterceptor, navbarAttribute.loginBtnText(), authButtonsSize);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_18);
		if (baseAttribute.isAuthenticated()) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_19);
			gg.jte.generated.precompiled.navbar.JteprofilepanelGenerated.render(jteOutput, jteHtmlInterceptor, navbarAttribute, navbarAttribute.isProfileOpen());
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_20);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_21);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BaseAttribute baseAttribute = (BaseAttribute)params.get("baseAttribute");
		NavbarAttribute navbarAttribute = (NavbarAttribute)params.get("navbarAttribute");
		boolean isProfileToggleBtnVisible = (boolean)params.getOrDefault("isProfileToggleBtnVisible", true);
		render(jteOutput, jteHtmlInterceptor, baseAttribute, navbarAttribute, isProfileToggleBtnVisible);
	}
}
