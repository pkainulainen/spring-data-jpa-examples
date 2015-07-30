package net.petrikainulainen.springdata.jpa.todo;

import org.springframework.data.jpa.domain.Specification;

/**
 * This specification builder class provides static methods that are used
 * to create <code>Specification&lt;Todo&gt;</code> objects which specify
 * the search criteria of dynamic database queries.
 *
 * @author Petri Kainulainen
 */
final class TodoSpecifications {

    /**
     * Prevent instantiation.
     */
    private TodoSpecifications() {}

    /**
     * Creates the search criteria that returns all todo entries whose title or description
     * contains the given search term. The search is case insensitive.
     *
     * If the search term is <code>null</code> or empty, the created search criteria will return
     * all todo entries.
     *
     * @param searchTerm The used search term.
     * @return
     */
    static Specification<Todo> titleOrDescriptionContainsIgnoreCase(String searchTerm) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);
            return cb.or(
                    cb.like(cb.lower(root.<String>get(Todo_.title)), containsLikePattern),
                    cb.like(cb.lower(root.<String>get(Todo_.description)), containsLikePattern)
            );
        };
    }

    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        }
        else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
}
