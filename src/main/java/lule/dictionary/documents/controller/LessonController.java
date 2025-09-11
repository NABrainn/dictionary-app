package lule.dictionary.documents.controller;

import lombok.RequiredArgsConstructor;
import lule.dictionary.documents.data.attribute.DocumentListAttribute;
import lule.dictionary.documents.service.DocumentService;
import lule.dictionary.documents.data.request.FindByTargetLanguageRequest;
import lule.dictionary.userProfiles.data.UserProfile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LessonController {

    private final DocumentService documentService;

    @GetMapping({"/lessons", "/lessons/"})
    public String lessonsPage(Model model,
                              Authentication authentication) {
        UserProfile principal = (UserProfile) authentication.getPrincipal();
        DocumentListAttribute documents = documentService.findMany(FindByTargetLanguageRequest.of(principal.getUsername(), principal.sourceLanguage(), principal.targetLanguage()));
        model.addAttribute("attribute", documents);
        return "document-list-page/documents";
    }
}
