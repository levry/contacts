package ru.levry.contacts.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.levry.contacts.NotFoundContactException;
import ru.levry.contacts.data.Contact;
import ru.levry.contacts.data.ContactsSearch;
import ru.levry.contacts.data.ContactsStore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author levry
 */
// TODO методы для работы с телефонами отдельного контакта
@RestController
@RequestMapping("/contacts")
public class ContactsController {

    private final ContactsStore contactsStore;

    public ContactsController(ContactsStore contactsStore) {
        this.contactsStore = contactsStore;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundContactException.class)
    public Object handleNotFoundContact(NotFoundContactException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());
        return error;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Contact get(@PathVariable Long id) {
        Optional<Contact> contact = contactsStore.get(id);
        return contact.orElseThrow(() -> new NotFoundContactException(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Contact create(@RequestBody Contact contact) {
        contactsStore.add(contact);
        return contact;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Contact edit(@PathVariable Long id, @RequestBody Contact contact) {
        contactsStore.update(id, contact);
        return contact;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void remove(@PathVariable Long id) {
        contactsStore.remove(id);
    }

    @RequestMapping(value = "/{id}/phones", method = RequestMethod.POST)
    public void addPhone(@PathVariable Long id, @RequestParam String phone) {
        contactsStore.addPhone(id, phone);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}/phones", method = RequestMethod.DELETE)
    public void removePhone(@PathVariable Long id, @RequestParam String phone) {
        contactsStore.removePhone(id, phone);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Contact> search(@ModelAttribute ContactsSearch search) {
        return contactsStore.findBy(search);
    }

}
