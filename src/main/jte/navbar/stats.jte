@param int wordsLearned
@param int dailyStreak

@param String wordsLearnedText
@param String daysSingularText
@param String daysPluralText
<div class="flex gap-4">
    <span
    class="
    md:text-lg
    text-lg font-bold text-neutral text-accent">🧠${wordsLearnedText}: ${wordsLearned}</span>
    @if(dailyStreak == 1)
        <span
        class="
        md:text-lg
        text-lg font-bold text-neutral text-accent">💪${daysSingularText}: ${dailyStreak}</span>
    @else
    <span
    class="
    md:text-lg
    text-lg font-bold text-neutral text-accent">💪${daysPluralText}: ${dailyStreak}</span>
    @endif
</div>