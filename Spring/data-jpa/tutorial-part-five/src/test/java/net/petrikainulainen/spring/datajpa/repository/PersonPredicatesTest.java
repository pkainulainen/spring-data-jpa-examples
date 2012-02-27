package net.petrikainulainen.spring.datajpa.repository;

import com.mysema.query.types.expr.BooleanExpression;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Petri Kainulainen
 */
public class PersonPredicatesTest {
    
    private static final String SEARCH_TERM = "Foo";
    private static final String EXPECTED_PREDICATE_STRING = "person.lastName like foo%";

    @Test
    public void lastNameLike() {
        BooleanExpression predicate = PersonPredicates.lastNameIsLike(SEARCH_TERM);
        String predicateAsString = predicate.toString();
        assertEquals(EXPECTED_PREDICATE_STRING, predicateAsString);
    }
}
