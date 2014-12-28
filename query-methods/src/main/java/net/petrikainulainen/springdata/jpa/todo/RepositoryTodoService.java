package net.petrikainulainen.springdata.jpa.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    @Override
    public TodoDTO create(TodoDTO newTodoEntry) {
        LOGGER.info("Creating a new todo entry by using information: {}", newTodoEntry);

        Todo created = Todo.getBuilder()
                .description(newTodoEntry.getDescription())
                .title(newTodoEntry.getTitle())
                .build();

        created = repository.save(created);
        LOGGER.info("Created a new todo entry: {}", created);

        return transformIntoDTO(created);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public TodoDTO findById(Long id) {
        LOGGER.info("Finding todo entry by using id: {}", id);

        Todo todoEntry = findTodoEntryById(id);
        LOGGER.info("Found todo entry: {}", todoEntry);

        return transformIntoDTO(todoEntry);
    }

    @Transactional
    @Override
    public TodoDTO update(TodoDTO updatedTodoEntry) {
        LOGGER.info("Updating the information of a todo entry by using information: {}", updatedTodoEntry);

        Todo updated = findTodoEntryById(updatedTodoEntry.getId());
        updated.update(updatedTodoEntry.getTitle(), updatedTodoEntry.getDescription());
        LOGGER.info("Updated the information of the todo entry: {}", updated);

        return transformIntoDTO(updated);
    }

    private Todo findTodoEntryById(Long id) {
        Optional<Todo> todoResult = repository.findOne(id);
        return todoResult.orElseThrow(() -> new TodoNotFoundException(id));
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
