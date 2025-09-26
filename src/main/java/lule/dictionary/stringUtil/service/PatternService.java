package lule.dictionary.stringUtil.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PatternService {
    public String removeSpecialCharacters(@NonNull String input) {
        return Pattern.compile("[^\\p{L}0-9 ]").matcher(input).replaceAll("");
    }

    public String replaceNewline(@NonNull String input, String replacementString) {
        return Pattern.compile("\n").matcher(input).replaceAll(replacementString);
    }

    public String replaceNewlinesWithSpaces(@NonNull String input) {
        return Pattern.compile("\n+").matcher(input).replaceAll(" ");
    }

    public String replaceAllNewlines(@NonNull String input, char replacementChar, int quantity) {
        return Pattern.compile("\n+").matcher(input).replaceAll(Stream.generate(() -> replacementChar)
                .limit(quantity)
                .map(String::valueOf)
                .collect(Collectors.joining()));
    }
}
