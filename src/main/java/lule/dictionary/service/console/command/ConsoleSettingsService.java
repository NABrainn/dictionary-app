package lule.dictionary.service.console.command;

import org.springframework.stereotype.Service;

@Service
public class ConsoleSettingsService {
    @Override
    public void print(String text) {
        System.out.println(text);
    }
}
