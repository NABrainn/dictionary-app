package lule.dictionary.service.validation;

public interface ValidationService {
    <T> void validate(T ob) throws ValidationServiceException;
}
