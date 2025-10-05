package lule.dictionary.translationFetching.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.language.service.Language;
import lule.dictionary.translationFetching.service.fetcher.GoogleTranslateFetcher;
import lule.dictionary.translationFetching.service.fetcher.LingvanexFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Predicate;

@Service
@Slf4j
public class TranslationFetcherManager {

    private final LingvanexFetcher lingvanexService;
    private final GoogleTranslateFetcher googleService;
    private final ExecutorService executor;

    @Autowired
    public TranslationFetcherManager(LingvanexFetcher lingvanexService, GoogleTranslateFetcher googleService) {
        this.lingvanexService = lingvanexService;
        this.googleService = googleService;
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public List<String> fetchTranslationsAsync(Language sourceLanguage, Language targetLanguage, String targetWord) {
        if(sourceLanguage.equals(targetLanguage)) {
            log.info("Source and target languages are equal, returning input: {}", targetWord);
            return List.of(targetWord);
        }
        List<lule.dictionary.translationFetching.service.fetcher.TranslationFetcher> sources = List.of(googleService, lingvanexService);
        List<CompletableFuture<List<String>>> futures = sources.stream()
                .map(source -> CompletableFuture
                        .supplyAsync(() -> {
                            try {
                                return source.translate(sourceLanguage, targetLanguage, targetWord);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }, executor))
                .toList();

        futures.forEach(future -> CompletableFuture.allOf(future)
                .orTimeout(250, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> null)
                .join());

        return futures.stream()
                .filter(CompletableFuture::isDone)
                .filter(Predicate.not(CompletableFuture::isCompletedExceptionally))
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
