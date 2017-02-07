package ru.levry.contacts.store.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import ru.levry.contacts.ContactsException;
import ru.levry.contacts.data.Contact;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author levry
 */
public class JacksonContactsReader implements ContactsReader {
    protected final File file;
    protected final ObjectMapper objectMapper;

    public static ContactsReader jsonReader(File file) {
        return new JacksonContactsReader(file, new ObjectMapper());
    }

    public static ContactsReader xmlReader(File file) {
        XmlMapper objectMapper = new XmlMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new JacksonContactsReader(file, objectMapper) {
            @Override
            protected Collection<Contact> readContacts() throws IOException {
                Contacts contacts = objectMapper.readValue(file, Contacts.class);
                return Optional.ofNullable(contacts.contacts).orElseGet(ArrayList::new);
            }

            @Override
            protected void writeContacts(Collection<Contact> contacts) throws IOException {
                objectMapper.writeValue(file, new Contacts(contacts));
            }
        };
    }

    private JacksonContactsReader(File file, ObjectMapper objectMapper) {
        this.file = file;
        this.objectMapper = objectMapper;
    }

    @Override
    public Collection<Contact> read() {
        try {
            return readContacts();
        } catch (IOException e) {
            throw new ContactsException("Error on read file '" + file + "': " + e.getMessage(), e);
        }
    }

    protected Collection<Contact> readContacts() throws IOException {
        return objectMapper.readValue(file, new TypeReference<List<Contact>>() {});
    }


    @Override
    public void write(Collection<Contact> contacts) {
        try {
            writeContacts(contacts);
        } catch (IOException e) {
            throw new ContactsException("Error on write file '" + file + "': " + e.getMessage(), e);
        }
    }

    protected void writeContacts(Collection<Contact> contacts) throws IOException {
        objectMapper.writeValue(file, contacts);
    }

    @JacksonXmlRootElement(localName = "contacts")
    private static class Contacts {

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "contact")
        Collection<Contact> contacts;

        public Contacts() {
        }

        public Contacts(Collection<Contact> contacts) {
            this.contacts = contacts;
        }
    }


}
