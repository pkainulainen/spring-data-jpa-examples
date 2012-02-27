package net.petrikainulainen.spring.datajpa.repository;

import com.mysema.query.types.expr.BooleanExpression;
import net.petrikainulainen.spring.datajpa.model.Person;
import net.petrikainulainen.spring.datajpa.model.QPerson;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * A class which is used to create Querydsl predicates.
 * @author Petri Kainulainen
 */
public class PersonPredicates {

    public static BooleanExpression lastNameIsLike(final String searchTerm) {
        QPerson person = QPerson.person;
        return person.lastName.like(getLikePattern(searchTerm));
    }

    private static String getLikePattern(final String searchTerm) {
        StringBuilder pattern = new StringBuilder();
        pattern.append(searchTerm.toLowerCase());
        pattern.append("%");
        return pattern.toString();
    }
}
