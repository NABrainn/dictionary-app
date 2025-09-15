package lule.dictionary.stringUtil.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PatternService {
    public String removeSpecialCharacters(@NonNull String input) {
        return Pattern.compile("[^\\p{L}0-9 ]").matcher(input).replaceAll("");
    }

    public String replaceNewlinesWithSpaces(@NonNull String input) {
        return Pattern.compile("\n+").matcher(input).replaceAll(" ");
    }

    public boolean isValidEmail(@NonNull String input) {
        return Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                .matcher(input)
                .matches();
    }
}
