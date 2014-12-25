package net.petrikainulainen.springdata.jpa.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Petri Kainulainen
 */
@Service
final class RepositoryTodoService implements TodoCrudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTodoService.class);

    private final TodoRepository repository;

    @Autowired
    RepositoryTodoService(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TodoDTO> findAll() {
        LOGGER.info("Finding all todo entries.");

        List<Todo> todoEntries = repository.findAll();

        LOGGER.info("Found {} todo entries", todoEntries.size());

        return transformIntoDTOs(todoEntries);
    }

    private List<TodoDTO> transformIntoDTOs(List<Todo> entities) {
        return entities.stream()
                .map(this::transformIntoDTO)
                .collect(toList());
    }

    private TodoDTO transformIntoDTO(Todo entity) {
        TodoDTO dto = new TodoDTO();

        dto.setCreationTime(entity.getCreationTime());
        dto.setDescription(entity.getDescription());
        dto.setId(entity.getId());
        dto.setModificationTime(entity.getModificationTime());
        dto.setTitle(entity.getTitle());

        return dto;
    }

}
