package net.petrikainulainen.spring.datajpa.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A DTO class which is used as a form object in the search form.
 * @author Petri Kainulainen
 */
public class SearchDTO {

    private String searchTerm;

    private SearchType searchType;

    public SearchDTO() {

    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
