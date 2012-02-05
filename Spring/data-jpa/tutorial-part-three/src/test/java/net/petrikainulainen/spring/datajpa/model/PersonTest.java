package net.petrikainulainen.spring.datajpa.model;

import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Petri Kainulainen
 */
public class PersonTest {

    private static final String FIRST_NAME = "Foo";
    private static final String FIRST_NAME_UPDATED = "Foo1";
    private static final String LAST_NAME = "Bar";
    private static final String LAST_NAME_UPDATED = "Bar1";

    @Test
    public void build() {
        Person built = Person.getBuilder(FIRST_NAME, LAST_NAME).build();
        
        assertEquals(FIRST_NAME, built.getFirstName());
        assertEquals(LAST_NAME, built.getLastName());
        assertEquals(0, built.getVersion());

        assertNull(built.getCreationTime());
        assertNull(built.getModificationTime());
        assertNull(built.getId());
    }
    
    @Test
    public void getName() {
        Person built = Person.getBuilder(FIRST_NAME, LAST_NAME).build();
        
        String expectedName = constructName(FIRST_NAME, LAST_NAME);
        assertEquals(expectedName, built.getName());
    }
    
    private String constructName(String firstName, String lastName) {
        StringBuilder name = new StringBuilder();
        
        name.append(firstName);
        name.append(" ");
        name.append(lastName);
        
        return name.toString();
    }
    
    @Test
    public void prePersist() {
        Person built = Person.getBuilder(FIRST_NAME, LAST_NAME).build();
        built.prePersist();
        
        Date creationTime = built.getCreationTime();
        Date modificationTime = built.getModificationTime();
        
        assertNotNull(creationTime);
        assertNotNull(modificationTime);
        assertEquals(creationTime, modificationTime);
    }
    
    @Test
    public void preUpdate() {
        Person built = Person.getBuilder(FIRST_NAME, LAST_NAME).build();
        built.prePersist();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //Back to work
        }

        built.preUpdate();
        
        Date creationTime = built.getCreationTime();
        Date modificationTime = built.getModificationTime();
        
        assertNotNull(creationTime);
        assertNotNull(modificationTime);
        assertTrue(modificationTime.after(creationTime));
    }
    
    @Test
    public void update() {
        Person built = Person.getBuilder(FIRST_NAME, LAST_NAME).build();
        built.update(FIRST_NAME_UPDATED, LAST_NAME_UPDATED);
        
        assertEquals(FIRST_NAME_UPDATED, built.getFirstName());
        assertEquals(LAST_NAME_UPDATED, built.getLastName());
    }
}
