package lule.dictionary.stringUtil.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PatternService {
    public String removeSpecialCharacters(String input) {
        return Pattern.compile("[^\\p{L}0-9 ]").matcher(input).replaceAll("");
    }

    public String replaceNewlinesWithSpaces(@NonNull String input) {
        return Pattern.compile("\n+").matcher(input).replaceAll(" ");
    }
}
