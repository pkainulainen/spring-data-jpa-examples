package net.petrikainulainen.spring.datajpa.repository;

import com.mysema.query.types.Predicate;
import net.petrikainulainen.spring.datajpa.model.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Petri Kainulainen
 */
public class PaginatingPersonRepositoryImplTest {

    private static final int PAGE_INDEX = 2;
    private static final long PERSON_COUNT = 5;
    private static final String PROPERTY_LASTNAME = "lastName";
    private static final String SEARCH_TERM = "searchTerm";

    private PaginatingPersonRepositoryImpl repository;

    private QueryDslJpaRepository personRepositoryMock;

    @Before
    public void setUp() {
        repository = new PaginatingPersonRepositoryImpl();

        personRepositoryMock = mock(QueryDslJpaRepository.class);
        repository.setPersonRepository(personRepositoryMock);
    }

    @Test
    public void findAllPersons() {
        repository.findAllPersons();

        ArgumentCaptor<Sort> sortArgument = ArgumentCaptor.forClass(Sort.class);
        verify(personRepositoryMock, times(1)).findAll(sortArgument.capture());

        Sort sort = sortArgument.getValue();
        assertEquals(Sort.Direction.ASC, sort.getOrderFor(PROPERTY_LASTNAME).getDirection());
    }

    @Test
    public void findPersonCount() {
        when(personRepositoryMock.count(any(Predicate.class))).thenReturn(PERSON_COUNT);

        long actual = repository.findPersonCount(SEARCH_TERM);

        verify(personRepositoryMock, times(1)).count(any(Predicate.class));
        verifyNoMoreInteractions(personRepositoryMock);

        assertEquals(PERSON_COUNT, actual);
    }

    @Test
    public void findPersonsForPage() {
        List<Person> expected = new ArrayList<Person>();
        Page foundPage = new PageImpl<Person>(expected);

        when(personRepositoryMock.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);

        List<Person> actual = repository.findPersonsForPage(SEARCH_TERM, PAGE_INDEX);

        ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
        verify(personRepositoryMock, times(1)).findAll(any(Predicate.class), pageSpecificationArgument.capture());
        verifyNoMoreInteractions(personRepositoryMock);

        Pageable pageSpecification = pageSpecificationArgument.getValue();

        assertEquals(PAGE_INDEX, pageSpecification.getPageNumber());
        assertEquals(PaginatingPersonRepositoryImpl.NUMBER_OF_PERSONS_PER_PAGE, pageSpecification.getPageSize());
        assertEquals(Sort.Direction.ASC, pageSpecification.getSort().getOrderFor(PROPERTY_LASTNAME).getDirection());

        assertEquals(expected, actual);
    }
}
