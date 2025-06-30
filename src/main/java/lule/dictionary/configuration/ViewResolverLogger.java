package lule.dictionary.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ViewResolverLogger {

    private final List<ViewResolver> resolvers;

    @PostConstruct
    public void log() {
        System.out.println(">>> ViewResolvers found:");
        for (ViewResolver r : resolvers) {
            System.out.println(" - " + r.getClass().getName());
        }
    }
}
