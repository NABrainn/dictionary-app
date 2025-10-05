//package lule.dictionary.documents.data.response;
//
//import lombok.NonNull;
//import lule.dictionary.documents.data.DocumentContentData;
//import lule.dictionary.pagination.data.DocumentPaginationData;
//
//public record ReloadDocumentResponse(@NonNull DocumentContentData contentData,
//                                     @NonNull DocumentPaginationData paginationData,
//                                     int startId,
//                                     @NonNull String text,
//                                     int) {
//    public static ReloadDocumentResponse of(DocumentContentData contentData, DocumentPaginationData paginationData) {
//        return new ReloadDocumentResponse(contentData, paginationData);
//    }
//}