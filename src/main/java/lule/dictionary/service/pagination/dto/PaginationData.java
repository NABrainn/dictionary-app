package lule.dictionary.service.pagination.dto;

import java.util.List;

public record PaginationData(int pageNumber,
                             int numberOfPages,
                             List<List<Integer>> rows,
                             int currentRowNumber,
                             int firstPageOfRowNumber) {
    @Override
    public List<List<Integer>> rows() {
        return List.copyOf(rows);
    }
}
