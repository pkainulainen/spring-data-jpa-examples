package net.petrikainulainen.spring.datajpa.repository;

import net.petrikainulainen.spring.datajpa.model.Person;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Petri Kainulainen
 */
public interface PaginatingPersonRepository {

    public long findPersonCount(String searchTerm);

    public List<Person> findPersonsForPage(String searchTerm, int page);
}
