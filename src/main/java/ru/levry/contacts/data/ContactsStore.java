package ru.levry.contacts.data;

import java.util.Collection;

/**
 * @author levry
 */
// TODO реализации для json, xml
public interface ContactsStore {

    void add(Contact contact);

    Contact get(long id);

    void update(long id, Contact contact);

    void remove(long id);

    Collection<Contact> findBy(ContactsSearch search);

}