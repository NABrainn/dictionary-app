@import lule.dictionary.dto.application.attribute.ProfilePanelAttribute

@param ProfilePanelAttribute profilePanelAttribute
@param boolean isProfileOpen

@if(isProfileOpen)
    @template.navbar.language-picker(
        targetLanguage = profilePanelAttribute.targetLanguage(),
        languages = profilePanelAttribute.languageDataList()
    )
    @template.navbar.stats(
        wordsLearned = profilePanelAttribute.wordsLearned(),
        dailyStreak = profilePanelAttribute.dailyStreak(),
        wordsLearnedText = profilePanelAttribute.wordsLearnedText(),
        daysSingularText = profilePanelAttribute.daysSingularText(),
        daysPluralText = profilePanelAttribute.daysPluralText()
    )
    @template.navbar.logout-btn(
        text = profilePanelAttribute.logoutBtnText()
    )
@endif