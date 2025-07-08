package lule.dictionary.service.pagination;

import lombok.Getter;
import lule.dictionary.service.pagination.dto.PaginationData;
import lule.dictionary.service.pagination.enumeration.IterationType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Getter
public class PaginationService {

    private final int MAX_ROW_SIZE = 7;
    private final int DIVISOR = 2000;

    public List<List<Integer>> getRows(int numberOfPages) {
        if(numberOfPages <= 0) throw new IllegalArgumentException("Number of pages cannot be below 0");
        return produceRows(
                getQuantity(numberOfPages),
                getMinRowSize(numberOfPages, MAX_ROW_SIZE),
                getMAX_ROW_SIZE()
        );
    }

    public int getCurrentRow(int currentPage, int maxRowSize) {
        return (currentPage - 1) / maxRowSize;
    }

    public int getFirstPageOfRow(int numberOfPages, int currentPage) {
        return getRows(numberOfPages)
                .get(getCurrentRow(currentPage, MAX_ROW_SIZE))
                .getFirst();
    }

    public int getNumberOfPages(int length) {
        return (int) Math.ceil((double) length / DIVISOR);
    }


    public PaginationData ofPaginationData(int pageNumber, int numberOfPages, List<List<Integer>> rows, int currentRowNumber, int firstPageOfRowNumber) {
        return new PaginationData(pageNumber, numberOfPages, rows, currentRowNumber, firstPageOfRowNumber);
    }

    private int getMinRowSize(int numberOfPages, int maxRowSize) {
        if(numberOfPages % maxRowSize == 0) return maxRowSize;
        return numberOfPages % maxRowSize;
    }

    private int getQuantity(int numberOfPages) {
        return (int) Math.ceil((double) numberOfPages / MAX_ROW_SIZE);
    }

    private List<List<Integer>> produceRows(int quantity, int minRowSize, int maxRowSize) {
        List<List<Integer>> rows = new ArrayList<>();

        for(int i = 1; i <= quantity; i++) {
            IterationType type =
                    (i == quantity) ? IterationType.LAST :
                    (i == 1) ? IterationType.FIRST :
                    IterationType.DEFAULT;
            List<Integer> range = switch (type) {
                case FIRST -> IntStream.range(i, maxRowSize + 1)
                        .boxed()
                        .toList();
                case LAST -> IntStream.range(maxRowSize * (i - 1) + 1, (maxRowSize * (i - 1) + 1) + minRowSize)
                        .boxed()
                        .toList();
                case DEFAULT -> IntStream.range(maxRowSize * (i - 1) + 1, maxRowSize * i + 1)
                        .boxed()
                        .toList();
            };
            rows.add(range);
        }
        return List.copyOf(rows);
    }
}
