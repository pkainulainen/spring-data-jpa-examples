package net.petrikainulainen.spring.datajpa.model;

import net.petrikainulainen.spring.datajpa.model.Person;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * A  meta model class used to create type safe queries from person
 * information.
 * @author Petri Kainulainen
 */
@StaticMetamodel(Person.class)
public class Person_ {
    public static volatile SingularAttribute<Person, String> lastName;
}
