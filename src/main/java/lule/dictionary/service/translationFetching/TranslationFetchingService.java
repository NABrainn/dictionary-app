package lule.dictionary.service.translationFetching;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.service.language.Language;
import lule.dictionary.service.translateAPI.TranslationFetcher;
import lule.dictionary.service.translateAPI.google.GoogleTranslateService;
import lule.dictionary.service.translateAPI.lingvanex.LingvanexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@Service
@Slf4j
public class TranslationFetchingService {

    private final LingvanexService lingvanexService;
    private final GoogleTranslateService googleService;
    private final ExecutorService executor;

    @Autowired
    public TranslationFetchingService(LingvanexService lingvanexService, GoogleTranslateService googleService) {
        this.lingvanexService = lingvanexService;
        this.googleService = googleService;
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public List<String> fetchTranslationsAsync(Language sourceLanguage, Language targetLanguage, String targetWord) {
        List<TranslationFetcher> sources = List.of(googleService, lingvanexService);

        List<CompletableFuture<List<String>>> futures = sources.stream()
                .map(source -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return source.translate(sourceLanguage, targetLanguage, targetWord);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }, executor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .orTimeout(1500, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> null)
                .join();

        return futures.stream()
                .filter(CompletableFuture::isDone)
                .filter(future -> !future.isCompletedExceptionally())
                .flatMap(future -> {
                    try {
                        return future.get().stream();
                    } catch (InterruptedException | ExecutionException e) {
                        log.warn("An exception occured while fetching translations: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .limit(3)
                .distinct()
                .toList();
    }
}
