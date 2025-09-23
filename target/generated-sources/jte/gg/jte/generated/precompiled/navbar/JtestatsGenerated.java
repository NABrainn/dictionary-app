package gg.jte.generated.precompiled.navbar;
@SuppressWarnings("unchecked")
public final class JtestatsGenerated {
	public static final String JTE_NAME = "navbar/stats.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,13,13,13,13,14,14,14,16,16,23,23,23,24,24,24,26,26,33,33,33,34,34,34,36,36,37,37,37,0,1,3,4,5,5,5,5};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtestatsGenerated.class, "JtestatsGenerated.bin", 171,23,24,175,27,28,147,23,24,7);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, int wordsLearned, int dailyStreak, String wordsLearnedText, String daysSingularText, String daysPluralText) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(wordsLearnedText);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(wordsLearned);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
		if (dailyStreak == 1) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(daysSingularText);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(dailyStreak);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
		} else {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(daysPluralText);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(dailyStreak);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		int wordsLearned = (int)params.get("wordsLearned");
		int dailyStreak = (int)params.get("dailyStreak");
		String wordsLearnedText = (String)params.get("wordsLearnedText");
		String daysSingularText = (String)params.get("daysSingularText");
		String daysPluralText = (String)params.get("daysPluralText");
		render(jteOutput, jteHtmlInterceptor, wordsLearned, dailyStreak, wordsLearnedText, daysSingularText, daysPluralText);
	}
}
