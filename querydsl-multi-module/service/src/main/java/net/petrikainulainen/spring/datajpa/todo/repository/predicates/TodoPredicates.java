package net.petrikainulainen.spring.datajpa.todo.repository.predicates;

import com.mysema.query.types.Predicate;
import net.petrikainulainen.spring.datajpa.todo.model.QTodo;

/**
 * @author Petri Kainulainen
 */
public class TodoPredicates {

    public static Predicate titleOrDescriptionContains(String searchTerm) {
        QTodo todo = QTodo.todo;
        return todo.title.containsIgnoreCase(searchTerm)
                .or(todo.description.containsIgnoreCase(searchTerm));
    }
}
