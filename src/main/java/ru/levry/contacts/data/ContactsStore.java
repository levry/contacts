package ru.levry.contacts.data;

import java.util.Collection;
import java.util.Optional;

/**
 * @author levry
 */
public interface ContactsStore {

    void add(Contact contact);

    Optional<Contact> get(long id);

    void update(long id, Contact contact);

    void remove(long id);

    void addPhone(long id, String phone);

    void removePhone(long id, String phone);

    Collection<Contact> findBy(ContactsSearch search);

}