package net.petrikainulainen.spring.datajpa.repository;

import net.petrikainulainen.spring.datajpa.model.Person;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * Declares the custom methods added to the person repository.
 * @author Petri Kainulainen
 */
public interface PaginatingPersonRepository {

    /**
     * Finds all persons stored in the database.
     * @return
     */
    public List<Person> findAllPersons();

    /**
     * Finds the count of persons stored in the database.
     * @param searchTerm
     * @return
     */
    public long findPersonCount(String searchTerm);

    /**
     * Finds persons for the requested page whose last name starts with the given search term.
     * @param searchTerm    The used search term.
     * @param page  The number of the requested page.
     * @return  A list of persons belonging to the requested page.
     */
    public List<Person> findPersonsForPage(String searchTerm, int page);
}
