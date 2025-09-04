package lule.dictionary.documents.service.dto.strategy;

public sealed interface SubmissionStrategy permits ContentSubmission, UrlSubmission {
}
