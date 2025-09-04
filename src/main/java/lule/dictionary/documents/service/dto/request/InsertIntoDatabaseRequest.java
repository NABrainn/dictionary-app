package lule.dictionary.documents.service.dto.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.userProfiles.data.CustomUserDetails;

@Builder
public record InsertIntoDatabaseRequest(@NonNull String title,
                                        @NonNull String url,
                                        @NonNull String content,
                                        @NonNull CustomUserDetails userDetails) {
}
