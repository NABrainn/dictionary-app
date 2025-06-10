package lule.dictionary.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.service.imports.ImportService;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ImportService importService;
    private final UserProfileService userProfileService;

    @GetMapping({"/", ""})
    public String base(@NonNull Model model, HttpServletResponse response) {
        return "redirect:catalog/";
    }
}