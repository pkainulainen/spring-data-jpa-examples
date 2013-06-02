package net.petrikainulainen.spring.datajpa.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Petri Kainulainen
 */
public class SearchDTO {

    private int pageIndex;

    private String searchTerm;

    public SearchDTO() {

    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
