package gg.jte.generated.precompiled.document;
import lule.dictionary.documents.data.selectable.Word;
import lule.dictionary.documents.data.selectable.Phrase;
import lule.dictionary.documents.data.request.DocumentAttribute;
import gg.jte.support.ForSupport;
import lule.dictionary.translations.data.Familiarity;
import java.util.regex.Pattern;
@SuppressWarnings("unchecked")
public final class JtecontentGenerated {
	public static final String JTE_NAME = "document/content.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,5,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,9,9,9,10,10,11,11,17,17,17,17,17,17,17,17,17,25,25,25,25,44,44,49,49,49,50,50,54,54,55,55,56,56,58,58,59,59,60,60,61,61,62,64,64,64,65,65,66,66,67,72,72,72,72,72,72,73,76,76,76,76,77,85,86,86,87,87,88,95,96,96,97,105,106,106,107,107,108,108,109,117,118,118,119,119,120,120,121,126,127,127,128,128,130,130,137,144,146,146,146,7,7,7,7};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtecontentGenerated.class, "JtecontentGenerated.bin", 1,1,1,64,19,1,292,1076,139,16,93,13,17,73,25,25,29,29,29,33,37,37,37,33,37,41,37,41,37,33,29,33,29,25,29,29,21,17,36,147,19);
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
	private static final byte[] TEXT_PART_BINARY_22 = BINARY_CONTENT.get(22);
	private static final byte[] TEXT_PART_BINARY_23 = BINARY_CONTENT.get(23);
	private static final byte[] TEXT_PART_BINARY_24 = BINARY_CONTENT.get(24);
	private static final byte[] TEXT_PART_BINARY_25 = BINARY_CONTENT.get(25);
	private static final byte[] TEXT_PART_BINARY_26 = BINARY_CONTENT.get(26);
	private static final byte[] TEXT_PART_BINARY_27 = BINARY_CONTENT.get(27);
	private static final byte[] TEXT_PART_BINARY_28 = BINARY_CONTENT.get(28);
	private static final byte[] TEXT_PART_BINARY_29 = BINARY_CONTENT.get(29);
	private static final byte[] TEXT_PART_BINARY_30 = BINARY_CONTENT.get(30);
	private static final byte[] TEXT_PART_BINARY_31 = BINARY_CONTENT.get(31);
	private static final byte[] TEXT_PART_BINARY_32 = BINARY_CONTENT.get(32);
	private static final byte[] TEXT_PART_BINARY_33 = BINARY_CONTENT.get(33);
	private static final byte[] TEXT_PART_BINARY_34 = BINARY_CONTENT.get(34);
	private static final byte[] TEXT_PART_BINARY_35 = BINARY_CONTENT.get(35);
	private static final byte[] TEXT_PART_BINARY_36 = BINARY_CONTENT.get(36);
	private static final byte[] TEXT_PART_BINARY_37 = BINARY_CONTENT.get(37);
	private static final byte[] TEXT_PART_BINARY_38 = BINARY_CONTENT.get(38);
	private static final byte[] TEXT_PART_BINARY_39 = BINARY_CONTENT.get(39);
	private static final byte[] TEXT_PART_BINARY_40 = BINARY_CONTENT.get(40);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, DocumentAttribute attribute) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		var translations = attribute.documentContentData().translations();
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		var content = attribute.documentContentData().content();
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
		var documentId = attribute.documentContentData().documentId();
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
		var __jte_html_attribute_0 = attribute.documentContentData().documentId();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
			jteOutput.setContext("form", "data-document-id");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("form", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
		jteOutput.setContext("form", "hx-vals");
		jteOutput.writeUserContent(documentId);
		jteOutput.setContext("form", null);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
		if (attribute.paginationData().currentPageNumber() == 1) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(attribute.documentContentData().title());
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
		var cleanWordPattern = Pattern.compile("[^\\p{L}\\p{N}\\s-]");
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
		for (var paragraphEntry :  ForSupport.of(content)) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
			var paragraph = paragraphEntry.get();
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
			for (var selectableEntry : ForSupport.of(paragraphEntry.get().selectables())) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
				var selectable = selectableEntry.get();
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
				if (selectable instanceof Word) {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
					var word = ((Word) selectable).value();
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
					var cleanWord = cleanWordPattern.matcher(word).replaceAll("")
                                                    .replace("-", "")
                                                    .toLowerCase();
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_18);
					if (!cleanWord.isEmpty()) {
						jteOutput.writeBinaryContent(TEXT_PART_BINARY_19);
						if (translations.get(cleanWord) != null) {
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_20);
							String wordBgColor = switch(translations.get(cleanWord).familiarity()){
                                        case UNKNOWN -> "bg-accent";
                                        case RECOGNIZED -> "bg-accent/80";
                                        case FAMILIAR -> "bg-accent/60";
                                        default -> "bg-primary";
                                    };
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_21);
							String wordTextColor = switch(translations.get(cleanWord).familiarity()){
                                        case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                                        default -> "text-neutral";
                                    };
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_22);
							gg.jte.generated.precompiled.document.JtewordGenerated.render(jteOutput, jteHtmlInterceptor, selectable.id(), documentId, word, cleanWord, wordBgColor, wordTextColor, true, false, translations.get(cleanWord).familiarity(), true, false);
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_23);
						} else {
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_24);
							if (cleanWord.length() <= 50) {
								jteOutput.writeBinaryContent(TEXT_PART_BINARY_25);
								gg.jte.generated.precompiled.document.JtewordGenerated.render(jteOutput, jteHtmlInterceptor, selectable.id(), documentId, word, cleanWord, "bg-accent", "text-primary", true, false, Familiarity.UNKNOWN, false, false);
								jteOutput.writeBinaryContent(TEXT_PART_BINARY_26);
							} else {
								jteOutput.writeBinaryContent(TEXT_PART_BINARY_27);
								gg.jte.generated.precompiled.document.JtewordGenerated.render(jteOutput, jteHtmlInterceptor, selectable.id(), documentId, word, cleanWord, "bg-primary", "text-neutral", false, false, Familiarity.IGNORED, false, false);
								jteOutput.writeBinaryContent(TEXT_PART_BINARY_28);
							}
							jteOutput.writeBinaryContent(TEXT_PART_BINARY_29);
						}
						jteOutput.writeBinaryContent(TEXT_PART_BINARY_30);
					} else {
						jteOutput.writeBinaryContent(TEXT_PART_BINARY_31);
						gg.jte.generated.precompiled.document.JtewordGenerated.render(jteOutput, jteHtmlInterceptor, selectable.id(), documentId, word, cleanWord, "bg-primary", "text-neutral", false, false, Familiarity.IGNORED, false, false);
						jteOutput.writeBinaryContent(TEXT_PART_BINARY_32);
					}
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_33);
				} else {
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_34);
					var phrase = ((Phrase) selectable);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_35);
					gg.jte.generated.precompiled.translation.JteupdatephraseGenerated.render(jteOutput, jteHtmlInterceptor, phrase, cleanWordPattern, selectable.id(), documentId, translations);
					jteOutput.writeBinaryContent(TEXT_PART_BINARY_36);
				}
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_37);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_38);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_39);
		gg.jte.generated.precompiled.document.JtepaginatorGenerated.render(jteOutput, jteHtmlInterceptor, attribute.documentContentData().documentId(), attribute.paginationData().currentPageNumber(), attribute.paginationData().numberOfPages(), attribute.documentContentData().documentId(), attribute.paginationData().rows(), attribute.paginationData().currentRowNumber(), attribute.paginationData().firstPageOfRowNumber());
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_40);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		DocumentAttribute attribute = (DocumentAttribute)params.get("attribute");
		render(jteOutput, jteHtmlInterceptor, attribute);
	}
}
