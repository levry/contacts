package ru.levry.contacts.web;

import org.springframework.web.bind.annotation.*;
import ru.levry.contacts.data.Contact;
import ru.levry.contacts.data.ContactSearch;
import ru.levry.contacts.data.ContactsStore;

import java.util.Collection;

/**
 * @author levry
 */
@RestController
@RequestMapping("/contacts")
public class ContactsController {

    private final ContactsStore contactsStore;

    public ContactsController(ContactsStore contactsStore) {
        this.contactsStore = contactsStore;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Contact get(@PathVariable Long id) {
        return contactsStore.get(id);
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

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Contact> search(@ModelAttribute ContactSearch search) {
        return contactsStore.findBy(search);
    }

}
