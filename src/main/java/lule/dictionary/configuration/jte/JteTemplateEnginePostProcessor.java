package lule.dictionary.configuration.jte;

import gg.jte.TemplateEngine;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class JteTemplateEnginePostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof TemplateEngine templateEngine) {
            System.out.println("found: " + templateEngine);
            templateEngine.setTrimControlStructures(true);
        }
        return bean;
    }
}
