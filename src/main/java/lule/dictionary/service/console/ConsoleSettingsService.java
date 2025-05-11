package lule.dictionary.service.console;

import lule.dictionary.functionalInterface.Printer;
import org.springframework.stereotype.Service;

@Service
public class ConsoleSettingsService implements Printer {
    @Override
    public void print(String text) {
        System.out.println(text);
    }
}
