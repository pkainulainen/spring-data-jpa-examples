package net.petrikainulainen.springdata.jpa.todo;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Petri Kainulainen
 */
final class TodoMapper {

    static List<TodoDTO> mapEntitiesIntoDTOs(List<Todo> entities) {
        return entities.stream()
                .map(TodoMapper::mapEntityIntoDTO)
                .collect(toList());
    }

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
