package lule.dictionary.pagination.data;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public record PageRowStore(@NonNull List<List<Integer>> rows,
                           @NonNull ArrayList<Integer> rowBuffer) {
    public static PageRowStore of(List<List<Integer>> rows,
                                  ArrayList<Integer> rowBuffer) {
        return new PageRowStore(rows, rowBuffer);
    }
}