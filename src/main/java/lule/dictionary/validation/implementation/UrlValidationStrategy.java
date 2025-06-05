package lule.dictionary.validation.implementation;

import lule.dictionary.validation.StringValidationStrategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlValidationStrategy implements StringValidationStrategy {
    @Override
    public boolean validate(String input) {
        String validUrl = "^(https?://)?([\\w\\-]+\\.)+[\\w\\-]+(/[\\w\\-./?%&=]*)?$";
        Pattern pattern = Pattern.compile(validUrl);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }
}
