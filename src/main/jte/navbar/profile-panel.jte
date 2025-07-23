@import lule.dictionary.dto.application.attribute.ProfilePanelAttribute

@param ProfilePanelAttribute profilePanelAttribute
@param boolean isProfileOpen

@if(isProfileOpen)
    <div class="profile-panel-transition flex bg-secondary justify-center gap-4 pb-4">
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
    </div>
@endif
