package lule.dictionary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "lule.dictionary")
public class DictionaryApplication {
	public static void main(String... args) throws Exception {
		SpringApplication.run(DictionaryApplication.class, args);
    }
}
