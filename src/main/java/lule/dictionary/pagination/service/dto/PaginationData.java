package lule.dictionary.pagination.service.dto;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record PaginationData(int currentPageNumber,
                             int numberOfPages,
                             @NonNull List<List<Integer>> rows,
                             int currentRowNumber,
                             int firstPageOfRowNumber) {
    @Override
    public List<List<Integer>> rows() {
        return List.copyOf(rows);
    }
}
