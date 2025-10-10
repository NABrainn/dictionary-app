package lule.dictionary.userProfiles.data;

import lule.dictionary.language.service.Language;

public sealed interface ChangeLanguageRequest permits ChangeSourceLanguageRequest, ChangeTargetLanguageRequest, ChangeUiLanguageRequest {
    Language language();
}
