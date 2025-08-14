@import lule.dictionary.dto.application.attribute.NavbarAttribute

@param NavbarAttribute navbarAttribute
@param boolean isProfileOpen

@if(isProfileOpen)
    <div class="profile-panel-transition flex bg-secondary justify-center gap-4 pb-4">
        @template.navbar.language-picker(
            selectedLanguage = navbarAttribute.targetLanguage(),
            languages = navbarAttribute.languageDataList()
        )
        @template.navbar.stats(
            wordsLearned = navbarAttribute.wordsLearned(),
            dailyStreak = navbarAttribute.dailyStreak(),
            wordsLearnedText = navbarAttribute.wordsLearnedText(),
            daysSingularText = navbarAttribute.daysSingularText(),
            daysPluralText = navbarAttribute.daysPluralText()
        )
    </div>
@endif
