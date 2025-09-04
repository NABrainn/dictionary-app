package lule.dictionary.validation.service;

public interface ValidationService {
    <T> void validate(T ob) throws ValidationServiceException;
}
