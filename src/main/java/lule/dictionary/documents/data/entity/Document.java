package lule.dictionary.documents.data.entity;

import lule.dictionary.language.service.Language;

public interface Document {
    String owner();
    String title();
    String url();
    String pageContent();
    int totalContentLength();
    Language sourceLanguage();
    Language targetLanguage();
}
