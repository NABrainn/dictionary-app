package lule.dictionary.service.jsoup.exception;

public class InvalidUriException extends RuntimeException {

    public InvalidUriException(String invalidOrEmptyUrl) {
        super(invalidOrEmptyUrl);
    }
}
