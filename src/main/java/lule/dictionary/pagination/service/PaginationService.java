package lule.dictionary.pagination.service;

import lule.dictionary.pagination.data.PageRowStore;
import lule.dictionary.pagination.data.PaginationData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.IntStream;

@Service
public final class PaginationService {
    public int pagesTotal(int contentLength) {
        return contentLength / 2000;
    }

    public PaginationData paginationData(int pagesTotal, int currentPage) {
        int maxRowSize = 5;
        int lastPage = pagesTotal + 1;
        int firstPage = 1;
        List<List<Integer>> rows = IntStream.range(firstPage, lastPage)
                .boxed()
                .collect(Collector.of(
                        () -> PageRowStore.of(new ArrayList<>(), new ArrayList<>()),
                        (store, number) -> {
                            store.rowBuffer().add(number);
                            if (store.rowBuffer().size() == maxRowSize || number == pagesTotal) {
                                store.rows().add(List.copyOf(store.rowBuffer()));
                                store.rowBuffer().clear();
                            }
                        },
                        (left, right) -> {
                            left.rows().addAll(right.rows());
                            return left;
                        },
                        Collector.Characteristics.IDENTITY_FINISH
                ))
                .rows();
        List<Integer> currentRow = rows.stream()
                .map(row -> row.stream()
                        .anyMatch(page -> page == currentPage) ? row : List.<Integer>of())
                .filter(Predicate.not(List::isEmpty))
                .findFirst()
                .orElse(List.of());
        int firstPageOfRow = currentRow.getFirst();
        return PaginationData.builder()
                .currentPage(currentPage)
                .pages(pagesTotal)
                .rows(rows)
                .currentRow(currentRow)
                .firstPageOfRow(firstPageOfRow)
                .build();
    }
}