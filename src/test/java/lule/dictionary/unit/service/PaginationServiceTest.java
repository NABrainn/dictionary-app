package lule.dictionary.unit.service;

import lule.dictionary.pagination.service.PaginationService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PaginationServiceTest {

    private final PaginationService paginationService;

    public PaginationServiceTest() {
        this.paginationService = new PaginationService();
    }

    @Test
    void shouldCreateArrayOfRows1() {
        var integers = paginationService.getRows(23);
        System.out.println(integers);
        assertEquals(4, integers.size());
        assertEquals(List.of(22, 23), integers.getLast());
    }

    @Test
    void shouldCreateArrayOfRows2() {
        var integers = paginationService.getRows(4);
        assertEquals(1, integers.size());
        assertEquals(List.of(1, 2, 3, 4), integers.getLast());
    }

    @Test
    void shouldCreateArrayOfRows420() {
        var integers = paginationService.getRows(420);
        assertEquals(60, integers.size());
        assertEquals(List.of(414, 415, 416, 417, 418, 419, 420), integers.getLast());
        assertEquals(1, paginationService.getFirstPageOfRow(420, 7));
        assertEquals(8, paginationService.getFirstPageOfRow(420, 8));
        assertEquals(204, paginationService.getFirstPageOfRow(420, 210));
    }
    @Test
    void shouldCreateArrayOfRows_empty() {
        assertThrows(IllegalArgumentException.class, () -> paginationService.getRows(0));
    }

}
