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

}
