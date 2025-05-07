package lule.dictionary.entity.factory;

import lule.dictionary.entity.Import;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

public class ImportFactory {
    public static Import create(String title, String content, String url, Language sourceLanguage, Language targetLanguage, String owner) {
        return new Import(title, content, url, sourceLanguage, targetLanguage, owner);
    }
}
