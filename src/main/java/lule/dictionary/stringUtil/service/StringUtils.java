package lule.dictionary.stringUtil.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StringUtils {

    private final PatternService patternService;

    public int getCharQuantity(@NonNull String input, char charToCount) {
        return (int) input.chars().filter(ch -> ch == charToCount).count();
    }

    public String normalize(@NonNull String input) {
        return patternService.removeSpecialCharacters(input.toLowerCase().trim());
    }
}