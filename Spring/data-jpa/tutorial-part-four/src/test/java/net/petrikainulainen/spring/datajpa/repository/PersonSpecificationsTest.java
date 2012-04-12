package net.petrikainulainen.spring.datajpa.repository;

import net.petrikainulainen.spring.datajpa.model.Person;
import net.petrikainulainen.spring.datajpa.model.Person_;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Petri Kainulainen
 */
public class PersonSpecificationsTest {
    
    private static final String SEARCH_TERM = "Foo";
    private static final String SEARCH_TERM_LIKE_PATTERN = "foo%";
    
    private CriteriaBuilder criteriaBuilderMock;
    
    private CriteriaQuery criteriaQueryMock;
    
    private Root<Person> personRootMock;

    @Before
    public void setUp() {
        criteriaBuilderMock = mock(CriteriaBuilder.class);
        criteriaQueryMock = mock(CriteriaQuery.class);
        personRootMock = mock(Root.class);
    }

    @Test
    public void lastNameIsLike() {
        Path lastNamePathMock = mock(Path.class);        
        when(personRootMock.get(Person_.lastName)).thenReturn(lastNamePathMock);
        
        Expression lastNameToLowerExpressionMock = mock(Expression.class);
        when(criteriaBuilderMock.lower(lastNamePathMock)).thenReturn(lastNameToLowerExpressionMock);
        
        Predicate lastNameIsLikePredicateMock = mock(Predicate.class);
        when(criteriaBuilderMock.like(lastNameToLowerExpressionMock, SEARCH_TERM_LIKE_PATTERN)).thenReturn(lastNameIsLikePredicateMock);

        Specification<Person> actual = PersonSpecifications.lastNameIsLike(SEARCH_TERM);
        Predicate actualPredicate = actual.toPredicate(personRootMock, criteriaQueryMock, criteriaBuilderMock);
        
        verify(personRootMock, times(1)).get(Person_.lastName);
        verifyNoMoreInteractions(personRootMock);
        
        verify(criteriaBuilderMock, times(1)).lower(lastNamePathMock);
        verify(criteriaBuilderMock, times(1)).like(lastNameToLowerExpressionMock, SEARCH_TERM_LIKE_PATTERN);
        verifyNoMoreInteractions(criteriaBuilderMock);

        verifyZeroInteractions(criteriaQueryMock, lastNamePathMock, lastNameIsLikePredicateMock);

        assertEquals(lastNameIsLikePredicateMock, actualPredicate);
    }
}
