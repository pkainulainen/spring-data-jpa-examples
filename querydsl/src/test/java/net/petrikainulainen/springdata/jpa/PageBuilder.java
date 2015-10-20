package net.petrikainulainen.springdata.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petri Kainulainen
 */
public class PageBuilder<T> {

    private List<T> elements = new ArrayList<>();
    private Pageable pageRequest;
    private int totalElements;

    public PageBuilder() {}

    public PageBuilder<T> elements(List<T> elements) {
        this.elements = elements;
        return this;
    }

    public PageBuilder<T> pageRequest(Pageable pageRequest) {
        this.pageRequest = pageRequest;
        return this;
    }

    public PageBuilder<T> totalElements(int totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public Page<T> build() {
        return new PageImpl<T>(elements, pageRequest, totalElements);
    }
}
