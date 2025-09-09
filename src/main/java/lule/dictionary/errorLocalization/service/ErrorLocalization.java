package lule.dictionary.errorLocalization.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lule.dictionary.auth.data.request.LoginRequest;
import lule.dictionary.auth.data.request.SignupRequest;
import lule.dictionary.documents.data.documentSubmission.ContentSubmissionStrategy;
import lule.dictionary.documents.data.documentSubmission.UrlSubmissionStrategy;
import lule.dictionary.language.service.Language;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Service
public class ErrorLocalization {

    private Map<String, String> constrainViolationMessages;

    public Map<String, String> getMessageByViolation(ConstraintViolation<?> violation, Language language) {
        String beanClass = violation.getRootBeanClass().getSimpleName();
        String field = violation.getPropertyPath().toString();
        String constraint = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
        String key = String.format("%s.%s.%s.%s", beanClass, field, constraint, language);
        return Map.of(field, this.constrainViolationMessages.getOrDefault(key, "message localization not implemented"));
    }

    @PostConstruct
    private void initMessages() throws NoSuchFieldException {
        this.constrainViolationMessages = new HashMap<>();
        initLoginRequestMessages();
        initSignupRequestMessages();
        initUrlSubmissionMessages();
        initContentSubmissionMessages();
    }

    private void initContentSubmissionMessages() throws NoSuchFieldException {
        Class<?> clazz = UrlSubmissionStrategy.class;

        Map<String, String> englishMessages = Map.of(
                createConstraintViolationKey(clazz, "title", NotBlank.class, Language.EN), "Title cannot be blank",
                createConstraintViolationKey(clazz, "title", Size.class, Language.EN), "Title must be between 10 and 200 characters",
                createConstraintViolationKey(clazz, "url", NotNull.class, Language.EN), "URL cannot be null",
                createConstraintViolationKey(clazz, "url", Size.class, Language.EN), "URL must be 200 characters or less",
                createConstraintViolationKey(clazz, "url", URL.class, Language.EN), "URL must be a valid HTTPS URL");

        Map<String, String> norwegianMessages = Map.of(
                createConstraintViolationKey(clazz, "title", NotBlank.class, Language.NO), "Tittelen kan ikke være tom",
                createConstraintViolationKey(clazz, "title", Size.class, Language.NO), "Tittelen må være mellom 10 og 200 tegn",
                createConstraintViolationKey(clazz, "url", NotNull.class, Language.NO), "URL kan ikke være null",
                createConstraintViolationKey(clazz, "url", Size.class, Language.NO), "URL må være 200 tegn eller færre",
                createConstraintViolationKey(clazz, "url", URL.class, Language.NO), "URL må være en gyldig HTTPS-URL");

        Map<String, String> italianMessages = Map.of(
                createConstraintViolationKey(clazz, "title", NotBlank.class, Language.IT), "Il titolo non può essere vuoto",
                createConstraintViolationKey(clazz, "title", Size.class, Language.IT), "Il titolo deve essere compreso tra 10 e 200 caratteri",
                createConstraintViolationKey(clazz, "url", NotNull.class, Language.IT), "L'URL non può essere nullo",
                createConstraintViolationKey(clazz, "url", Size.class, Language.IT), "L'URL deve essere di 200 caratteri o meno",
                createConstraintViolationKey(clazz, "url", URL.class, Language.IT), "L'URL deve essere un URL HTTPS valido");

        Map<String, String> polishMessages = Map.of(
                createConstraintViolationKey(clazz, "title", NotBlank.class, Language.PL), "Tytuł nie może być pusty",
                createConstraintViolationKey(clazz, "title", Size.class, Language.PL), "Tytuł musi mieć od 10 do 200 znaków",
                createConstraintViolationKey(clazz, "url", NotNull.class, Language.PL), "URL nie może być null",
                createConstraintViolationKey(clazz, "url", Size.class, Language.PL), "URL musi mieć 200 znaków lub mniej",
                createConstraintViolationKey(clazz, "url", URL.class, Language.PL), "URL musi być poprawnym adresem HTTPS");

        this.constrainViolationMessages.putAll(englishMessages);
        this.constrainViolationMessages.putAll(norwegianMessages);
        this.constrainViolationMessages.putAll(italianMessages);
        this.constrainViolationMessages.putAll(polishMessages);
    }

    private void initUrlSubmissionMessages() throws NoSuchFieldException {
        Class<?> clazz = ContentSubmissionStrategy.class;

        Map<String, String> englishMessages = Map.of(
                createConstraintViolationKey(clazz, "title", NotBlank.class, Language.EN), "Title cannot be blank",
                createConstraintViolationKey(clazz, "title", Size.class, Language.EN), "Title must be between 10 and 200 characters",
                createConstraintViolationKey(clazz, "content", NotBlank.class, Language.EN), "Content cannot be empty",
                createConstraintViolationKey(clazz, "content", Size.class, Language.EN), "Content must be 1000000 characters or less");

        Map<String, String> norwegianMessages = Map.of(
                createConstraintViolationKey(clazz, "title", NotBlank.class, Language.NO), "Tittelen kan ikke være tom",
                createConstraintViolationKey(clazz, "title", Size.class, Language.NO), "Tittelen må være mellom 10 og 200 tegn",
                createConstraintViolationKey(clazz, "content", NotBlank.class, Language.NO), "Innholdet kan ikke være tomt",
                createConstraintViolationKey(clazz, "content", Size.class, Language.NO), "Innholdet må være 1000000 tegn eller færre");

        Map<String, String> italianMessages = Map.of(
                createConstraintViolationKey(clazz, "title", NotBlank.class, Language.IT), "Il titolo non può essere vuoto",
                createConstraintViolationKey(clazz, "title", Size.class, Language.IT), "Il titolo deve essere compreso tra 10 e 200 caratteri",
                createConstraintViolationKey(clazz, "content", NotBlank.class, Language.IT), "Il contenuto non può essere vuoto",
                createConstraintViolationKey(clazz, "content", Size.class, Language.IT), "Il contenuto deve essere di 1000000 caratteri o meno");

        Map<String, String> polishMessages = Map.of(
                createConstraintViolationKey(clazz, "title", NotBlank.class, Language.PL), "Tytuł nie może być pusty",
                createConstraintViolationKey(clazz, "title", Size.class, Language.PL), "Tytuł musi mieć od 10 do 200 znaków",
                createConstraintViolationKey(clazz, "content", NotBlank.class, Language.PL), "Treść nie może być pusta",
                createConstraintViolationKey(clazz, "content", Size.class, Language.PL), "Treść musi mieć 1000000 znaków lub mniej");

        this.constrainViolationMessages.putAll(englishMessages);
        this.constrainViolationMessages.putAll(norwegianMessages);
        this.constrainViolationMessages.putAll(italianMessages);
        this.constrainViolationMessages.putAll(polishMessages);
    }

    private void initSignupRequestMessages() throws NoSuchFieldException {
        Class<?> clazz = SignupRequest.class;
        Map<String, String> englishMessages = Map.of(
                createConstraintViolationKey(clazz, "login", NotBlank.class, Language.EN), "Login cannot be blank",
                createConstraintViolationKey(clazz, "login", Pattern.class, Language.EN), "Login must contain only letters",
                createConstraintViolationKey(clazz, "email", NotBlank.class, Language.EN), "Email cannot be empty",
                createConstraintViolationKey(clazz, "email", Size.class, Language.EN), "Email must be between 8 and 150 characters",
                createConstraintViolationKey(clazz, "password", NotBlank.class, Language.EN), "Password cannot be blank",
                createConstraintViolationKey(clazz, "password", Size.class, Language.EN), "Password must be between 8 and 200 characters"
        );

        Map<String, String> norwegianMessages = Map.of(
                createConstraintViolationKey(clazz, "login", NotBlank.class, Language.NO), "Innlogging kan ikke være tom",
                createConstraintViolationKey(clazz, "login", Pattern.class, Language.NO), "Innlogging må kun inneholde bokstaver",
                createConstraintViolationKey(clazz, "email", NotBlank.class, Language.NO), "E-post kan ikke være tom",
                createConstraintViolationKey(clazz, "email", Size.class, Language.NO), "E-post må være mellom 8 og 150 tegn",
                createConstraintViolationKey(clazz, "password", NotBlank.class, Language.NO), "Passord kan ikke være tom",
                createConstraintViolationKey(clazz, "password", Size.class, Language.NO), "Passord må være mellom 8 og 200 tegn"
        );

        Map<String, String> italianMessages = Map.of(
                createConstraintViolationKey(clazz, "login", NotBlank.class, Language.IT), "Il login non può essere vuoto",
                createConstraintViolationKey(clazz, "login", Pattern.class, Language.IT), "Il login deve contenere solo lettere",
                createConstraintViolationKey(clazz, "email", NotBlank.class, Language.IT), "L'email non può essere vuota",
                createConstraintViolationKey(clazz, "email", Size.class, Language.IT), "L'email deve essere compresa tra 8 e 150 caratteri",
                createConstraintViolationKey(clazz, "password", NotBlank.class, Language.IT), "La password non può essere vuota",
                createConstraintViolationKey(clazz, "password", Size.class, Language.IT), "La password deve essere compresa tra 8 e 200 caratteri"
        );

        Map<String, String> polishMessages = Map.of(
                createConstraintViolationKey(clazz, "login", NotBlank.class, Language.PL), "Login nie może być pusty",

                createConstraintViolationKey(clazz, "login", Pattern.class, Language.PL), "Login musi zawierać tylko litery",
                createConstraintViolationKey(clazz, "email", NotBlank.class, Language.PL), "Email nie może być pusty",
                createConstraintViolationKey(clazz, "email", Size.class, Language.PL), "Email musi mieć od 8 do 150 znaków",
                createConstraintViolationKey(clazz, "password", NotBlank.class, Language.PL), "Hasło nie może być puste",
                createConstraintViolationKey(clazz, "password", Size.class, Language.PL), "Hasło musi mieć od 8 do 200 znaków"
        );

        this.constrainViolationMessages.putAll(englishMessages);
        this.constrainViolationMessages.putAll(norwegianMessages);
        this.constrainViolationMessages.putAll(italianMessages);
        this.constrainViolationMessages.putAll(polishMessages);
    }

    private void initLoginRequestMessages() throws NoSuchFieldException {
        Class<?> clazz = LoginRequest.class;
        Map<String, String> englishMessages = Map.of(
                createConstraintViolationKey(clazz, "login", NotBlank.class, Language.EN), "Login cannot be blank",
                createConstraintViolationKey(clazz, "login", Size.class, Language.EN), "Login must be between 8 and 50 characters",
                createConstraintViolationKey(clazz, "login", Pattern.class, Language.EN), "Login must contain only letters",
                createConstraintViolationKey(clazz, "password", NotBlank.class, Language.EN), "Password cannot be blank",
                createConstraintViolationKey(clazz, "password", Size.class, Language.EN), "Password must be between 8 and 200 characters"
        );

        Map<String, String> norwegianMessages = Map.of(
                createConstraintViolationKey(clazz, "login", NotBlank.class, Language.NO), "Innlogging kan ikke være tom",
                createConstraintViolationKey(clazz, "login", Size.class, Language.NO), "Innlogging må være mellom 8 og 50 tegn",
                createConstraintViolationKey(clazz, "login", Pattern.class, Language.NO), "Innlogging må kun inneholde bokstaver",
                createConstraintViolationKey(clazz, "password", NotBlank.class, Language.NO), "Passord kan ikke være tom",
                createConstraintViolationKey(clazz, "password", Size.class, Language.NO), "Passord må være mellom 8 og 200 tegn"
        );

        Map<String, String> italianMessages = Map.of(
                createConstraintViolationKey(clazz, "login", NotBlank.class, Language.IT), "Il login non può essere vuoto",
                createConstraintViolationKey(clazz, "login", Size.class, Language.IT), "Il login deve essere compreso tra 8 e 50 caratteri",
                createConstraintViolationKey(clazz, "login", Pattern.class, Language.IT), "Il login deve contenere solo lettere",
                createConstraintViolationKey(clazz, "password", NotBlank.class, Language.IT), "La password non può essere vuota",
                createConstraintViolationKey(clazz, "password", Size.class, Language.IT), "La password deve essere compresa tra 8 e 200 caratteri"
        );

        Map<String, String> polishMessages = Map.of(
                createConstraintViolationKey(clazz, "login", NotBlank.class, Language.PL), "Login nie może być pusty",
                createConstraintViolationKey(clazz, "login", Size.class, Language.PL), "Login musi mieć od 8 do 50 znaków",
                createConstraintViolationKey(clazz, "login", Pattern.class, Language.PL), "Login musi zawierać tylko litery",
                createConstraintViolationKey(clazz, "password", NotBlank.class, Language.PL), "Hasło nie może być puste",
                createConstraintViolationKey(clazz, "password", Size.class, Language.PL), "Hasło musi mieć od 8 do 200 znaków"
        );

        this.constrainViolationMessages.putAll(englishMessages);
        this.constrainViolationMessages.putAll(norwegianMessages);
        this.constrainViolationMessages.putAll(italianMessages);
        this.constrainViolationMessages.putAll(polishMessages);
    }



    private <T extends Annotation> String createConstraintViolationKey(Class<?> violatedBean, String violatedField, Class<T> constraintDescriptor, Language language) throws NoSuchFieldException {
        return getBeanClass(violatedBean) + "." + getField(violatedBean, violatedField) + "." + getAnnotation(violatedBean, violatedField, constraintDescriptor) + "." + getLanguage(language);
    }

    private String getLanguage(Language language) {
        return language.name();
    }

    private <T extends Annotation> String getAnnotation(Class<?> clazz, String field, Class<T> annotation) throws NoSuchFieldException {
        if(clazz.getDeclaredField(field).getAnnotation(annotation) != null) {
            return clazz.getDeclaredField(field).getAnnotation(annotation).annotationType().getSimpleName();
        }
        throw new NullPointerException("Undeclared annotation identified: \n" + annotation + " \nat field: " + clazz.getDeclaredField(field));
    }

    private String getField(Class<?> clazz, String violatedField) throws NoSuchFieldException {
        return clazz.getDeclaredField(violatedField).getName();
    }

    private String getBeanClass(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
