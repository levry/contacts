package ru.levry.contacts.store;

import ru.levry.contacts.data.Contact;
import ru.levry.contacts.data.ContactSearch;
import ru.levry.contacts.data.ContactsStore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author levry
 */
public class ListContactsStore implements ContactsStore {

    private final AtomicLong counter = new AtomicLong();
    private final Map<Long, Contact> contacts;

    public ListContactsStore() {
        this(new HashMap<>());
    }

    public ListContactsStore(Map<Long, Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public void add(Contact contact) {
        long id = counter.incrementAndGet();
        contact.setId(id);
        contacts.put(id, contact);
    }

    @Override
    public Contact get(long id) {
        return contacts.get(id);
    }

    @Override
    public void update(long id, Contact contact) {
        contacts.put(id, contact);
    }

    @Override
    public void remove(long id) {
        contacts.remove(id);
    }

    @Override
    public Collection<Contact> findBy(ContactSearch search) {
        return contacts.values();
    }
}
