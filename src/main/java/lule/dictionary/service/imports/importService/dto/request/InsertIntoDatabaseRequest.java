package lule.dictionary.service.imports.importService.dto.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.userProfile.CustomUserDetails;

@Builder
public record InsertIntoDatabaseRequest(@NonNull String title,
                                        @NonNull String url,
                                        @NonNull String content,
                                        @NonNull CustomUserDetails userDetails) {
}
