package net.petrikainulainen.springdata.jpa.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Petri Kainulainen
 */
@Repository
final class TodoRepositoryImpl implements CustomTodoRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoRepositoryImpl.class);

    private static final String SEARCH_TODO_ENTRIES = "SELECT id, title FROM todos t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%')) " +
            "ORDER BY t.title ASC";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    TodoRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TodoSearchResultDTO> findBySearchTerm(String searchTerm) {
        LOGGER.info("Finding todo entries by using search term: {}", searchTerm);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("searchTerm", searchTerm);

        List<TodoSearchResultDTO> searchResults = jdbcTemplate.query(SEARCH_TODO_ENTRIES,
                queryParams,
                new BeanPropertyRowMapper<>(TodoSearchResultDTO.class)
        );

        LOGGER.info("Found {} todo entries", searchResults.size());

        return searchResults;
    }
}
