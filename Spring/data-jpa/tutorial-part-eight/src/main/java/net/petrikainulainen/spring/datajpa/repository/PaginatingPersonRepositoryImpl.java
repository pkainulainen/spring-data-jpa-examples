package net.petrikainulainen.spring.datajpa.repository;

import com.mysema.query.jpa.impl.JPAQuery;
import net.petrikainulainen.spring.datajpa.model.Person;
import net.petrikainulainen.spring.datajpa.model.QPerson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.utils.JpaClassUtils;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static net.petrikainulainen.spring.datajpa.repository.PersonPredicates.lastNameIsLike;

/**
 * @author Petri Kainulainen
 */
@Repository
public class PaginatingPersonRepositoryImpl implements PaginatingPersonRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaginatingPersonRepositoryImpl.class);

    protected static final int NUMBER_OF_PERSONS_PER_PAGE = 5;

    @PersistenceContext
    private EntityManager entityManager;

    private QueryDslJpaRepository<Person, Long> personRepository;

    public PaginatingPersonRepositoryImpl() {

    }

    @Override
    public long findPersonCount(String searchTerm) {
        LOGGER.debug("Finding person count with search term: " + searchTerm);

        return personRepository.count(lastNameIsLike(searchTerm));
    }

    @Override
    public List<Person> findPersonsForPage(String searchTerm, int page) {
        LOGGER.debug("Finding persons for page " + page + " with search term: " + searchTerm);

        Page requestedPage =  personRepository.findAll(lastNameIsLike(searchTerm), constructPageSpecification(page));

        return requestedPage.getContent();
    }

    /**
     * Returns a new object which specifies the the wanted result page.
     * @param pageIndex The index of the wanted result page
     * @return
     */
    private Pageable constructPageSpecification(int pageIndex) {
        Pageable pageSpecification = new PageRequest(pageIndex, NUMBER_OF_PERSONS_PER_PAGE, sortByLastNameAsc());
        return pageSpecification;
    }

    /**
     * Returns a Sort object which sorts persons in ascending order by using the last name.
     * @return
     */
    private Sort sortByLastNameAsc() {
        return new Sort(Sort.Direction.ASC, "lastName");
    }


    @PostConstruct
    public void init() {
        JpaEntityInformation<Person, Long> personEntityInfo = new JpaMetamodelEntityInformation<Person, Long>(Person.class, entityManager.getMetamodel());
        personRepository = new QueryDslJpaRepository<Person, Long>(personEntityInfo, entityManager);
    }

    /**
     * This setter method should be used only by unit tests
     * @param personRepository
     */
    protected void setPersonRepository(QueryDslJpaRepository<Person, Long> personRepository) {
        this.personRepository = personRepository;
    }
}
