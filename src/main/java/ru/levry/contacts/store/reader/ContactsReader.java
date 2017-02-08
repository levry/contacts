package ru.levry.contacts.store.reader;

import ru.levry.contacts.data.Contact;

import java.util.Collection;

/**
 * Метод получения/сохранения контактов (файловые источники)
 *
 * @author levry
 */
public interface ContactsReader {

    Collection<Contact> read();

    void write(Collection<Contact> contacts);

}
