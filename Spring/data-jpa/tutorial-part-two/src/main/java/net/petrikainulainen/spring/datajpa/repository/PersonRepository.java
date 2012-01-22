package net.petrikainulainen.spring.datajpa.repository;

import net.petrikainulainen.spring.datajpa.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Petri Kainulainen
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
