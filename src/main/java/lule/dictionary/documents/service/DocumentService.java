package lule.dictionary.documents.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.documents.data.*;
import lule.dictionary.documents.data.attribute.*;
import lule.dictionary.documents.data.documentProcessing.*;
import lule.dictionary.documents.data.documentSubmission.DocumentFormType;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;
import lule.dictionary.documents.data.entity.Document;
import lule.dictionary.documents.data.exception.DocumentServiceException;
import lule.dictionary.documents.data.parseDocument.*;
import lule.dictionary.documents.data.request.*;
import lule.dictionary.documents.data.documentSubmission.DocumentFormWithContent;
import lule.dictionary.documents.data.documentSubmission.DocumentFormWithUrl;
import lule.dictionary.documents.data.request.loadDocument.*;
import lule.dictionary.documents.data.response.LoadDocumentResponse;
import lule.dictionary.documents.data.response.ReadDocumentResponse;
import lule.dictionary.documents.data.response.ReloadWithPhraseResponse;
import lule.dictionary.documents.data.response.ReloadWithWordResponse;
import lule.dictionary.language.service.Language;
import lule.dictionary.pagination.service.PaginationService;
import lule.dictionary.result.data.Err;
import lule.dictionary.result.data.Ok;
import lule.dictionary.result.data.Result;
import lule.dictionary.documents.data.repository.DocumentRepository;
import lule.dictionary.jsoup.service.JsoupService;
import lule.dictionary.pagination.data.PaginationData;
import lule.dictionary.translations.data.entity.Translation;
import lule.dictionary.translations.data.request.ExtractPhrasesRequest;
import lule.dictionary.translations.data.request.FindTranslationsInDocumentRequest;
import lule.dictionary.translations.service.TranslationService;
import lule.dictionary.userProfiles.data.OwnerInfo;
import lule.dictionary.userProfiles.data.UserProfile;
import lule.dictionary.userProfiles.service.NavService;
import lule.dictionary.validation.data.Constraint;
import lule.dictionary.validation.data.rule.NotEmpty;
import lule.dictionary.validation.data.ValidationException;
import lule.dictionary.validation.data.rule.Size;
import lule.dictionary.validation.service.Validator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final Validator validator;
    private final JsoupService jsoupService;
    private final TranslationService translationService;
    private final DocumentProcessor documentProcessor;
    private final DocumentSanitizer documentSanitizer;
    private final DocumentsLocalizationService documentsLocalization;
    private final NavService navService;
    private final PaginationService paginationService;

    @Transactional
    public Result<Integer> createDocument(CreateDocumentRequest request) {
        UserProfile principal = (UserProfile) request.authentication().getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        Map<DocumentLocalizationKey, String> localization = documentsLocalization.get(principal.userInterfaceLanguage());
        DocumentFormType documentFormType = switch (request.documentFormType()) {
            case "url_form" -> DocumentFormWithUrl.of(request.title(), request.url(), localization.get(DocumentLocalizationKey.SPACE_FOR_URL));
            case "content_form" -> DocumentFormWithContent.of(request.title(), request.content(), localization.get(DocumentLocalizationKey.SPACE_FOR_CONTENT));
            default -> throw new IllegalStateException("Unexpected value: " + request.documentFormType());
        };
        return switch (documentFormType) {
            case DocumentFormWithUrl documentFormWithUrl -> {
                Result<?> result = validator.validate(
                        Constraint.of("title", NotEmpty.of(request.title()), switch (uiLanguage) {
                            case PL -> "Tytuł nie może być pusty";
                            case EN -> "Title cannot be empty";
                            case IT -> "Il titolo non può essere vuoto";
                            case NO -> "Tittelen kan ikke være tom";
                        }),
                        Constraint.of("title", Size.of(request.title(), 0, 500), switch (uiLanguage) {
                            case PL -> "Tytuł nie może być dłuższy niż 500 znaków";
                            case EN -> "Title cannot be longer than 500 characters";
                            case IT -> "Il titolo non può essere più lungo di 500 caratteri";
                            case NO -> "Tittelen kan ikke være lenger enn 500 tegn";
                        }),
                        Constraint.of("url", NotEmpty.of(request.url()), switch (uiLanguage) {
                            case PL -> "URL nie może być pusty";
                            case EN -> "URL cannot be empty";
                            case IT -> "L'URL non può essere vuoto";
                            case NO -> "URL-en kan ikke være tom";
                        }),
                        Constraint.of("url", Size.of(request.url(), 0, 500), switch (uiLanguage) {
                            case PL -> "URL nie może być dłuższy niż 500 znaków";
                            case EN -> "URL cannot be longer than 500 characters";
                            case IT -> "L'URL non può essere più lungo di 500 caratteri";
                            case NO -> "URL-en kan ikke være lenger enn 500 tegn";
                        })
                );
                yield switch (result) {
                    case Ok<?> ignored -> {
                        String documentContent = jsoupService.fetchDocument(documentFormWithUrl.url()).wholeText();
                        String processedDocument = documentProcessor.write(documentContent);
                        Document document = Document.builder()
                                .id(-1)
                                .title(documentFormWithUrl.title())
                                .contentBlob(processedDocument)
                                .url(documentFormWithUrl.url())
                                .sourceLanguage(principal.sourceLanguage())
                                .targetLanguage(principal.targetLanguage())
                                .owner(principal.getUsername())
                                .contentLength(processedDocument.length())
                                .build();
                        int documentId = documentRepository.create(document).orElseThrow();
                        yield Ok.of(documentId);
                    }
                    case Err<?> v -> v.throwable() instanceof ValidationException validationException ?
                            Err.of(new DocumentServiceException(DocumentFormAttribute.of(documentFormType, localization), validationException.getViolations())) :
                            Err.of(new RuntimeException());
                };

            }
            case DocumentFormWithContent contentSubmission -> {
                Result<?> result = validator.validate(
                        Constraint.of("title", NotEmpty.of(request.title()), switch (uiLanguage) {
                            case PL -> "Tytuł nie może być pusty";
                            case EN -> "Title cannot be empty";
                            case IT -> "Il titolo non può essere vuoto";
                            case NO -> "Tittelen kan ikke være tom";
                        }),
                        Constraint.of("title", Size.of(request.title(), 0, 500), switch (uiLanguage) {
                            case PL -> "Tytuł nie może być dłuższy niż 500 znaków";
                            case EN -> "Title cannot be longer than 500 characters";
                            case IT -> "Il titolo non può essere più lungo di 500 caratteri";
                            case NO -> "Tittelen kan ikke være lenger enn 500 tegn";
                        }),
                        Constraint.of("content", NotEmpty.of(request.content()), switch (uiLanguage) {
                            case PL -> "Treść nie może być pusta";
                            case EN -> "Content cannot be empty";
                            case IT -> "Il contenuto non può essere vuoto";
                            case NO -> "Innholdet kan ikke være tomt";
                        }),
                        Constraint.of("content", Size.of(request.content(), 0, 100000), switch (uiLanguage) {
                            case PL -> "Treść nie może być dłuższa niż 100 000 znaków";
                            case EN -> "Content cannot be longer than 100,000 characters";
                            case IT -> "Il contenuto non può essere più lungo di 100.000 caratteri";
                            case NO -> "Innholdet kan ikke være lenger enn 100 000 tegn";
                        })
                );
                yield switch (result) {
                    case Ok<?> ignored -> {
                        String content = contentSubmission.content();
                        Document document = Document.builder()
                                .id(-1)
                                .title(contentSubmission.title())
                                .contentBlob(content)
                                .url("")
                                .sourceLanguage(principal.sourceLanguage())
                                .targetLanguage(principal.targetLanguage())
                                .owner(principal.getUsername())
                                .contentLength(content.length())
                                .build();
                        int documentId = documentRepository.create(document).orElseThrow();
                        yield Ok.of(documentId);
                    }
                    case Err<?> v -> v.throwable() instanceof ValidationException validationException ?
                            Err.of(new DocumentServiceException(DocumentFormAttribute.of(documentFormType, localization), validationException.getViolations())) :
                            Err.of(new RuntimeException());
                };
            }
        };
    }

    public DocumentListAttribute findMany(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Language uiLanguage = principal.userInterfaceLanguage();
        Language targetLanguage = principal.targetLanguage();
        String username = principal.getUsername();

        List<DocumentWithTranslationData> documents = documentRepository.findByOwnerAndTargetLanguage(username, targetLanguage);
        Map<DocumentLocalizationKey, String> localization = documentsLocalization.get(uiLanguage);
        boolean isNavbarOpen = navService.isNavbarOpen(authentication);
        return DocumentListAttribute.of(documents, localization, isNavbarOpen);
    }

    public Result<ReadDocumentResponse> loadDocumentContent(@NonNull ReadDocumentRequest request, @NonNull Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        int documentId = request.documentDetails().documentId();
        int currentPage = request.documentDetails().page();

        Language sourceLanguage = principal.sourceLanguage();
        Language targetLanguage = principal.targetLanguage();
        Result<Document> result = documentRepository.findById(documentId, currentPage)
                .map(found -> documentSanitizer.validateRequestedPage(DocumentPageDetails.of(
                        currentPage,
                        paginationService.pagesTotal(found.contentLength()),
                        found
                )))
                .orElse(Err.of(new RuntimeException("Failed to fetch document")));
        return switch (result) {
            case Ok<Document> ok -> {
                Document document = ok.value();
                String contentBlob = document.contentBlob();
                String owner = document.owner();

                Map<String, Translation> translations = translationService.findTranslations(FindTranslationsInDocumentRequest.of(contentBlob, owner));
                Phrases phrases = Phrases.of(translationService.findPhrases(ExtractPhrasesRequest.of(contentBlob, owner)));

                TranslationInfo translationInfo = TranslationInfo.of(translations, phrases);
                OwnerInfo ownerInfo = OwnerInfo.of(sourceLanguage, targetLanguage, owner);

                ParseDocument parseDocumentRequest = switch (request) {
                    case LoadDocumentRequest ignored2 -> ParseWithoutSelection.of(translationInfo, ownerInfo, contentBlob);
                    case ReloadDocumentRequest reloadDocumentRequest -> switch (reloadDocumentRequest) {
                        case ReloadWithPhrase reloadWithPhrase -> ParseWithPhraseSelection.of(
                                translationInfo,
                                ownerInfo,
                                contentBlob,
                                (SelectedPhraseDetails) reloadWithPhrase.selectedUnitInfo()
                        );
                        case ReloadWithWord reloadWithWord -> ParseWithWordSelection.of(
                                translationInfo,
                                ownerInfo,
                                contentBlob,
                                (SelectedWordDetails) reloadWithWord.selectedUnitInfo()
                        );
                    };
                };
                List<DocumentUnit> processedContent = documentProcessor.read(parseDocumentRequest);
                List<Paragraph> paragraphs = documentProcessor.asParagraphs(processedContent);
                DocumentContentData contentData = DocumentContentData.builder()
                        .title(document.title())
                        .content(!paragraphs.isEmpty() ? paragraphs : List.of(Paragraph.of(0, 0, processedContent)))
                        .translations(translations)
                        .documentId(documentId)
                        .build();
                PaginationData paginationData = paginationService.paginationData(
                        paginationService.pagesTotal(document.contentLength()),
                        currentPage
                );
                boolean isNavbarOpen = navService.hideNavbar(authentication);
                Optional<SelectedUnit> optionalSelectedUnit = processedContent.stream()
                        .filter(unit -> unit instanceof SelectedUnit)
                        .map(unit -> (SelectedUnit) unit)
                        .findFirst();

                yield switch (request) {
                    case LoadDocumentRequest ignored1 -> Ok.of(LoadDocumentResponse.of(contentData, paginationData, isNavbarOpen));
                    case ReloadDocumentRequest ignored -> optionalSelectedUnit
                            .map(unit -> switch (unit) {
                                case SelectedPhraseUnit selectedPhraseUnit -> Ok.of(ReloadWithPhraseResponse.of(contentData, paginationData, selectedPhraseUnit));
                                case SelectedWordUnit selectedWordUnit -> Ok.of(ReloadWithWordResponse.of(contentData, paginationData, selectedWordUnit));
                            })
                            .orElseThrow();
                };
            }
            case Err<Document> v -> Err.of(v.throwable());
        };
    }

    public Map<DocumentLocalizationKey, String> getDocumentFormLocalization(Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        return documentsLocalization.get(principal.userInterfaceLanguage());
    }

    public DocumentFormAttribute getDocumentForm(String strategy, Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        Map<DocumentLocalizationKey, String> localization = documentsLocalization.get(principal.userInterfaceLanguage());
        DocumentFormType documentFormType = switch (strategy) {
            case "url_form" -> DocumentFormWithUrl.of("", "", localization.get(DocumentLocalizationKey.SPACE_FOR_URL));
            case "content_form" -> DocumentFormWithContent.of("", "", localization.get(DocumentLocalizationKey.SPACE_FOR_CONTENT));
            default -> throw new IllegalStateException("Unexpected value: " + strategy);
        };
        return DocumentFormAttribute.of(documentFormType, localization);
    }
}
