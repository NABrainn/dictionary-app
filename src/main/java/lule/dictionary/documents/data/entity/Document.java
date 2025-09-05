package lule.dictionary.documents.data.entity;

import lule.dictionary.userProfiles.data.UserProfileSettings;

public interface Document extends DocumentDetails, UserProfileSettings {
    String owner();
}
