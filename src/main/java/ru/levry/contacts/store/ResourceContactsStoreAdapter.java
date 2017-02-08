package ru.levry.contacts.store;

import ru.levry.contacts.data.Contact;
import ru.levry.contacts.data.ContactsSearch;
import ru.levry.contacts.data.ContactsStore;
import ru.levry.contacts.store.reader.ContactsReader;

import java.util.*;

/**
 * @author levry
 */
public class ResourceContactsStoreAdapter implements ContactsStore {

    private final ContactsReader contactsReader;
    private final ListContactsStore contactsStore;

    public ResourceContactsStoreAdapter(ContactsReader reader) {
        contactsReader = reader;
        contactsStore = readContacts();
    }

    private ListContactsStore readContacts() {
        Collection<Contact> list = contactsReader.read();
        return ListContactsStore.contactsList(list);
    }

    @Override
    public boolean exists(long id) {
        return contactsStore.exists(id);
    }

    @Override
    public void add(Contact contact) {
        contactsStore.add(contact);
    }

    @Override
    public Optional<Contact> get(long id) {
        return contactsStore.get(id);
    }

    @Override
    public void update(long id, Contact contact) {
        contactsStore.update(id, contact);
    }

    @Override
    public void remove(long id) {
        contactsStore.remove(id);
    }

    @Override
    public void putPhones(long id, Set<String> phones) {
        contactsStore.putPhones(id, phones);
    }

    @Override
    public void addPhone(long id, String phone) {
        contactsStore.addPhone(id, phone);
    }

    @Override
    public void removePhone(long id, String phone) {
        contactsStore.removePhone(id, phone);
    }

    @Override
    public Collection<Contact> findBy(ContactsSearch search) {
        return contactsStore.findBy(search);
    }

    public void writeContacts() {
        contactsReader.write(contactsStore.findAll());
    }
}
