package net.petrikainulainen.springdata.jpa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Optional;

/**
 * This class is our "custom" implementation of the {@link org.springframework.data.repository.CrudRepository}
 * interface that replaces the default implementation provided by Spring Data JPA.
 *
 * @author Petri Kainulainen
 */
public class BaseRepositoryImpl <T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>  implements BaseRepository<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepositoryImpl.class);

    private final EntityManager entityManager;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Optional<T> deleteById(ID id) {
        LOGGER.info("Deleting an entity by using id: {}", id);

        T deleted = entityManager.find(this.getDomainClass(), id);
        LOGGER.debug("Deleted entity is: {}", deleted);

        Optional<T> returned = Optional.empty();

        if (deleted != null) {
            entityManager.remove(deleted);
            returned = Optional.of(deleted);
        }

        LOGGER.info("Returning deleted entity: {}", returned);

        return returned;
    }
}
