package ru.levry.contacts.store.reader;

import ru.levry.contacts.data.Contact;

import java.util.List;

/**
 * @author levry
 */
public interface ContactsReader {

    List<Contact> read();

    void write(List<Contact> contacts);

}
