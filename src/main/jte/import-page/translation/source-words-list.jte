@import java.util.List

@param List<String> sourceWords

@for(String word : sourceWords)
    <input name="sourceWords" type="hidden" value="${word}">
    <span class="text-accent">${word}</span>
@endfor
