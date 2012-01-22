package net.petrikainulainen.spring.datajpa.service;

import net.petrikainulainen.spring.datajpa.dto.PersonDTO;
import net.petrikainulainen.spring.datajpa.model.Person;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
public interface PersonService {
    
    public Person create(PersonDTO created);

    public Person delete(Long personId) throws PersonNotFoundException;
    
    public List<Person> findAll();
    
    public Person findById(Long id);
    
    public Person update(PersonDTO updated) throws PersonNotFoundException;
}
