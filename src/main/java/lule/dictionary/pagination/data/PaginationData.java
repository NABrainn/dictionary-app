package lule.dictionary.pagination.data;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record PaginationData(int currentPage,
                             int pages,
                             @NonNull List<List<Integer>> rows,
                             @NonNull List<Integer> currentRow,
                             int firstPageOfRow) {
    @Override
    public List<List<Integer>> rows() {
        return List.copyOf(rows);
    }
}
