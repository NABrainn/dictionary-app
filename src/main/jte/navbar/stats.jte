@param int wordsLearned
@param int dailyStreak

@param String wordsLearnedText
@param String daysSingularText
@param String daysPluralText
<div class="flex gap-4">
    <span
    class="
    md:text-lg
    text-lg font-bold text-neutral text-accent">🧠
        <span class="
        md:inline
        hidden">${wordsLearnedText}:</span>
        <span>${wordsLearned}</span>
    </span>
    @if(dailyStreak == 1)
        <span
        class="
        md:text-lg
        text-lg font-bold text-neutral text-accent">💪
            <span class="
            md:inline
            hidden">${daysSingularText}:</span>
            <span>${dailyStreak}</span>
        </span>
    @else
    <span
    class="
    md:text-lg
    text-lg font-bold text-neutral text-accent">💪
        <span class="
        md:inline
        hidden">${daysPluralText}:</span>
        <span>${dailyStreak}</span>
    </span>
    @endif
</div>