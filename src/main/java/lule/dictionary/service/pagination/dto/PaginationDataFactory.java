package lule.dictionary.service.pagination.dto;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginationDataFactory {
    public PaginationData of(int pageNumber,
                             int numberOfPages,
                             List<List<Integer>> rows,
                             int currentRowNumber,
                             int firstPageOfRowNumber) {
        return new PaginationData(pageNumber, numberOfPages, rows, currentRowNumber, firstPageOfRowNumber);
    }
}
