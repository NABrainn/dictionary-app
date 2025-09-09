package lule.dictionary.documents.data.documentSubmission;

public sealed interface SubmissionStrategy permits ContentSubmissionStrategy, UrlSubmissionStrategy {
}
