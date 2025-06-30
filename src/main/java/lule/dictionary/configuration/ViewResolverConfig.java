package lule.dictionary.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import java.util.List;

@Configuration
public class ViewResolverConfig {
    @Bean
    public ViewResolver debuggingViewResolver(List<ViewResolver> resolvers) {
        return (viewName, locale) -> {
            System.out.println(">>> Resolving view: " + viewName);
            for (ViewResolver r : resolvers) {
                var view = r.resolveViewName(viewName, locale);
                System.out.println(" - " + r.getClass().getSimpleName() + " -> " + (view != null ? view : "null"));
            }
            return null;
        };
    }
}

