package lule.dictionary.documents.data.strategy;

public sealed interface SubmissionStrategy permits ContentSubmissionStrategy, UrlSubmissionStrategy {
}
