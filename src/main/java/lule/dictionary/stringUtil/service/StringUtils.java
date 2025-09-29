package lule.dictionary.stringUtil.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class StringUtils {
    public int getCharQuantity(@NonNull String input, char charToCount) {
        return (int) input.chars().filter(ch -> ch == charToCount).count();
    }
}