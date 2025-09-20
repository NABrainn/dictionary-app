package gg.jte.generated.precompiled.document;
import gg.jte.support.ForSupport;
import java.util.List;
@SuppressWarnings("unchecked")
public final class JtepaginatorGenerated {
	public static final String JTE_NAME = "document/paginator.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,11,11,11,15,15,15,15,15,15,15,15,19,19,23,23,23,23,23,23,23,23,27,27,28,28,29,29,32,32,32,32,32,32,32,32,37,37,37,40,40,43,43,43,43,43,43,43,43,48,48,48,51,51,52,52,54,54,58,58,58,58,58,58,58,58,62,62,66,66,66,66,66,66,66,66,70,70,70,70,3,4,5,6,7,8,9,9,9,9};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtepaginatorGenerated.class, "JtepaginatorGenerated.bin", 1,102,13,93,112,13,93,1,5,67,13,236,49,67,13,234,49,1,2,102,13,93,112,13,85);
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
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, int id, int currentPage, int pages, int importId, List<List<Integer>> rows, int currentRow, int firstPageOfRow) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		if (currentPage > 1) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
			jteOutput.setContext("div", "hx-get");
			jteOutput.writeUserContent(importId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
			jteOutput.setContext("div", "hx-get");
			jteOutput.writeUserContent(currentPage - 1);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
		} else {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
			jteOutput.setContext("div", "hx-get");
			jteOutput.writeUserContent(importId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
			jteOutput.setContext("div", "hx-get");
			jteOutput.writeUserContent(currentPage + 1);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
		for (var row : ForSupport.of(rows.get(currentRow))) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
			if (currentPage == row.get()) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_9);
				jteOutput.setContext("form", "hx-get");
				jteOutput.writeUserContent(id);
				jteOutput.setContext("form", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_10);
				jteOutput.setContext("form", "hx-get");
				jteOutput.writeUserContent(row.get());
				jteOutput.setContext("form", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_11);
				jteOutput.setContext("button", null);
				jteOutput.writeUserContent(row.get());
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_12);
			} else {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_13);
				jteOutput.setContext("form", "hx-get");
				jteOutput.writeUserContent(id);
				jteOutput.setContext("form", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_14);
				jteOutput.setContext("form", "hx-get");
				jteOutput.writeUserContent(row.get());
				jteOutput.setContext("form", null);
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_15);
				jteOutput.setContext("button", null);
				jteOutput.writeUserContent(row.get());
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_16);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_17);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_18);
		if (currentPage != pages) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_19);
			jteOutput.setContext("div", "hx-get");
			jteOutput.writeUserContent(importId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_20);
			jteOutput.setContext("div", "hx-get");
			jteOutput.writeUserContent(currentPage + 1);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_21);
		} else {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_22);
			jteOutput.setContext("div", "hx-get");
			jteOutput.writeUserContent(importId);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_23);
			jteOutput.setContext("div", "hx-get");
			jteOutput.writeUserContent(currentPage + 1);
			jteOutput.setContext("div", null);
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_24);
		}
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		int id = (int)params.get("id");
		int currentPage = (int)params.get("currentPage");
		int pages = (int)params.get("pages");
		int importId = (int)params.get("importId");
		List<List<Integer>> rows = (List<List<Integer>>)params.get("rows");
		int currentRow = (int)params.get("currentRow");
		int firstPageOfRow = (int)params.get("firstPageOfRow");
		render(jteOutput, jteHtmlInterceptor, id, currentPage, pages, importId, rows, currentRow, firstPageOfRow);
	}
}
