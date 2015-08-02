package net.petrikainulainen.springdata.jpa.todo;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a mapper class that is used to transform {@link Todo} objects
 * into {@link TodoDTO} objects.
 * @author Petri Kainulainen
 */
final class TodoMapper {

    /**
     * Transforms the list of {@link Todo} objects given as a method parameter
     * into a list of {@link TodoDTO} objects and returns the created list.
     *
     * @param entities
     * @return
     */
    static List<TodoDTO> mapEntitiesIntoDTOs(Iterable<Todo> entities) {
        List<TodoDTO> dtos = new ArrayList<>();

        entities.forEach(e -> dtos.add(mapEntityIntoDTO(e)));

        return dtos;
    }

    /**
     * Transforms the {@link Todo} object given as a method parameter into a
     * {@link TodoDTO} object and returns the created object.
     *
     * @param entity
     * @return
     */
    static TodoDTO mapEntityIntoDTO(Todo entity) {
        TodoDTO dto = new TodoDTO();

        dto.setCreatedByUser(entity.getCreatedByUser());
        dto.setCreationTime(entity.getCreationTime());
        dto.setDescription(entity.getDescription());
        dto.setId(entity.getId());
        dto.setModifiedByUser(entity.getModifiedByUser());
        dto.setModificationTime(entity.getModificationTime());
        dto.setTitle(entity.getTitle());

        return dto;
    }
}
