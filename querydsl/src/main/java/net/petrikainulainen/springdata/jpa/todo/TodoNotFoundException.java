package net.petrikainulainen.springdata.jpa.todo;

/**
 * This exception is thrown when a todo entry is not found by
 * using the given id.
 *
 * @author Petri Kainulainen
 */
public class TodoNotFoundException extends RuntimeException {

    private final Long id;

    public TodoNotFoundException(Long id) {
        super();
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
