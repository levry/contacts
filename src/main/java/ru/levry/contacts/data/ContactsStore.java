package ru.levry.contacts.data;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * @author levry
 */
public interface ContactsStore {

    boolean exists(long id);

    void add(Contact contact);

    Optional<Contact> get(long id);

    void update(long id, Contact contact);

    void remove(long id);

    void putPhones(long id, Set<String> phones);

    void addPhone(long id, String phone);

    void removePhone(long id, String phone);

    Collection<Contact> findBy(ContactsSearch search);

}