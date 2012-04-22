package net.petrikainulainen.spring.datajpa.service;

import net.petrikainulainen.spring.datajpa.dto.PersonDTO;
import net.petrikainulainen.spring.datajpa.model.Person;
import net.petrikainulainen.spring.datajpa.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static net.petrikainulainen.spring.datajpa.repository.PersonSpecifications.lastNameIsLike;

/**
 * This implementation of the PersonService interface communicates with
 * the database by using a Spring Data JPA repository.
 * @author Petri Kainulainen
 */
@Service
public class RepositoryPersonService implements PersonService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryPersonService.class);

    protected static final int NUMBER_OF_PERSONS_PER_PAGE = 5;

    @Resource
    private PersonRepository personRepository;

    @Transactional
    @Override
    public Person create(PersonDTO created) {
        LOGGER.debug("Creating a new person with information: " + created);
        
        Person person = Person.getBuilder(created.getFirstName(), created.getLastName()).build();
        
        return personRepository.save(person);
    }

    @Transactional
    @Override
    public long count(String searchTerm) {
        LOGGER.debug("Getting person count for search term: " + searchTerm);

        //Passes the specification created by PersonSpecifications class to the repository.
        return personRepository.count(lastNameIsLike(searchTerm));
    }

    @Transactional(rollbackFor = PersonNotFoundException.class)
    @Override
    public Person delete(Long personId) throws PersonNotFoundException {
        LOGGER.debug("Deleting person with id: " + personId);
        
        Person deleted = personRepository.findOne(personId);
        
        if (deleted == null) {
            LOGGER.debug("No person found with id: " + personId);
            throw new PersonNotFoundException();
        }
        
        personRepository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Person> findAll() {
        LOGGER.debug("Finding all persons");
        return personRepository.findAll(sortByLastNameAsc());
    }

    @Transactional(readOnly = true)
    @Override
    public Person findById(Long id) {
        LOGGER.debug("Finding person by id: " + id);
        return personRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Person> search(String searchTerm, int pageIndex) {
        LOGGER.debug("Searching persons with search term: " + searchTerm);

        //Passes the specification created by PersonSpecifications class and the page specification to the repository.
        Page requestedPage = personRepository.findAll(lastNameIsLike(searchTerm), constructPageSpecification(pageIndex));

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

    @Transactional(rollbackFor = PersonNotFoundException.class)
    @Override
    public Person update(PersonDTO updated) throws PersonNotFoundException {
        LOGGER.debug("Updating person with information: " + updated);
        
        Person person = personRepository.findOne(updated.getId());
        
        if (person == null) {
            LOGGER.debug("No person found with id: " + updated.getId());
            throw new PersonNotFoundException();
        }
        
        person.update(updated.getFirstName(), updated.getLastName());

        return person;
    }

    /**
     * This setter method should be used only by unit tests.
     * @param personRepository
     */
    protected void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
