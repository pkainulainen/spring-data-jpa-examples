package net.petrikainulainen.spring.datajpa.controller;

import net.petrikainulainen.spring.datajpa.dto.PersonDTO;
import net.petrikainulainen.spring.datajpa.model.Person;
import net.petrikainulainen.spring.datajpa.service.PersonNotFoundException;
import net.petrikainulainen.spring.datajpa.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Petri Kainulainen
 */
@Controller
@SessionAttributes("person")
public class PersonController extends AbstractController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
    
    protected static final String ERROR_MESSAGE_KEY_DELETED_PERSON_WAS_NOT_FOUND = "error.message.deleted.not.found";
    protected static final String ERROR_MESSAGE_KEY_EDITED_PERSON_WAS_NOT_FOUND = "error.message.edited.not.found";
    
    protected static final String FEEDBACK_MESSAGE_KEY_PERSON_CREATED = "feedback.message.person.created";
    protected static final String FEEDBACK_MESSAGE_KEY_PERSON_DELETED = "feedback.message.person.deleted";
    protected static final String FEEDBACK_MESSAGE_KEY_PERSON_EDITED = "feedback.message.person.edited";
    
    protected static final String MODEL_ATTIRUTE_PERSON = "person";
    protected static final String MODEL_ATTRIBUTE_PERSONS = "persons";
    
    protected static final String PERSON_ADD_FORM_VIEW = "person/create";
    protected static final String PERSON_EDIT_FORM_VIEW = "person/edit";
    protected static final String PERSON_LIST_VIEW = "person/list";
    
    protected static final String REQUEST_MAPPING_LIST = "/";
    
    @Resource
    private PersonService personService;

    /**
     * Processes delete person requests.
     * @param id    The id of the deleted person.
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/person/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id, RedirectAttributes attributes) {
        LOGGER.debug("Deleting person with id: " + id);

        try {
            Person deleted = personService.delete(id);
            addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_PERSON_DELETED, deleted.getName());
        } catch (PersonNotFoundException e) {
            LOGGER.debug("No person found with id: " + id);
            addErrorMessage(attributes, ERROR_MESSAGE_KEY_DELETED_PERSON_WAS_NOT_FOUND);
        }

        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }

    /**
     * Processes create person requests.
     * @param model
     * @return  The name of the create person form view.
     */
    @RequestMapping(value = "/person/create", method = RequestMethod.GET) 
    public String showCreatePersonForm(Model model) {
        LOGGER.debug("Rendering create person form");
        
        model.addAttribute(MODEL_ATTIRUTE_PERSON, new PersonDTO());

        return PERSON_ADD_FORM_VIEW;
    }

    /**
     * Processes the submissions of create person form.
     * @param created   The information of the created persons.
     * @param bindingResult
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/person/create", method = RequestMethod.POST)
    public String submitCreatePersonForm(@Valid @ModelAttribute(MODEL_ATTIRUTE_PERSON) PersonDTO created, BindingResult bindingResult, RedirectAttributes attributes) {
        LOGGER.debug("Create person form was submitted with information: " + created);

        if (bindingResult.hasErrors()) {
            return PERSON_ADD_FORM_VIEW;
        }
                
        Person person = personService.create(created);

        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_PERSON_CREATED, person.getName());

        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }

    /**
     * Processes edit person requests.
     * @param id    The id of the edited person.
     * @param model
     * @param attributes
     * @return  The name of the edit person form view.
     */
    @RequestMapping(value = "/person/edit/{id}", method = RequestMethod.GET)
    public String showEditPersonForm(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        LOGGER.debug("Rendering edit person form for person with id: " + id);
        
        Person person = personService.findById(id);
        if (person == null) {
            LOGGER.debug("No person found with id: " + id);
            addErrorMessage(attributes, ERROR_MESSAGE_KEY_EDITED_PERSON_WAS_NOT_FOUND);
            return createRedirectViewPath(REQUEST_MAPPING_LIST);            
        }

        model.addAttribute(MODEL_ATTIRUTE_PERSON, constructFormObject(person));
        
        return PERSON_EDIT_FORM_VIEW;
    }

    /**
     * Processes the submissions of edit person form.
     * @param updated   The information of the edited person.
     * @param bindingResult
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/person/edit", method = RequestMethod.POST)
    public String submitEditPersonForm(@Valid @ModelAttribute(MODEL_ATTIRUTE_PERSON) PersonDTO updated, BindingResult bindingResult, RedirectAttributes attributes) {
        LOGGER.debug("Edit person form was submitted with information: " + updated);
        
        if (bindingResult.hasErrors()) {
            LOGGER.debug("Edit person form contains validation errors. Rendering form view.");
            return PERSON_EDIT_FORM_VIEW;
        }
        
        try {
            Person person = personService.update(updated);
            addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_PERSON_EDITED, person.getName());
        } catch (PersonNotFoundException e) {
            LOGGER.debug("No person was found with id: " + updated.getId());
            addErrorMessage(attributes, ERROR_MESSAGE_KEY_EDITED_PERSON_WAS_NOT_FOUND);
        }
        
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }
    
    private PersonDTO constructFormObject(Person person) {
        PersonDTO formObject = new PersonDTO();
        
        formObject.setId(person.getId());
        formObject.setFirstName(person.getFirstName());
        formObject.setLastName(person.getLastName());
        
        return formObject;
    }

    /**
     * Processes requests to home page which lists all available persons.
     * @param model
     * @return  The name of the person list view.
     */
    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String showList(Model model) {
        LOGGER.debug("Rendering person list page");

        List<Person> persons = personService.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_PERSONS, persons);

        return PERSON_LIST_VIEW;
    }

    /**
     * This setter method should only be used by unit tests
     * @param personService
     */
    protected void setPersonService(PersonService personService) {
        this.personService = personService;
    }
}
