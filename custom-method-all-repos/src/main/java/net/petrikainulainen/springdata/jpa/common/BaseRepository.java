package net.petrikainulainen.springdata.jpa.common;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Optional;

/**
 * This interface is the base interface that must be extended by all Spring Data JPA
 * repositories of our example application. It also declares the custom methods that
 * are added into every Spring Data JPA repository.
 *
 * @author Petri Kainulainen
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

    /**
     * Deletes a managed entity.
     * @param id    The id of the deleted entity.
     * @return      an {@code Optional} that contains the deleted entity. If there
     *              is no entity that has the given id, this method returns an empty {@code Optional}.
     */
    Optional<T> deleteById(ID id);
}
