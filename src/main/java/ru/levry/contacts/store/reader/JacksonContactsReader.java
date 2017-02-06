package ru.levry.contacts.store.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import ru.levry.contacts.ContactsException;
import ru.levry.contacts.data.Contact;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        return new JacksonContactsReader(file, new XmlMapper()) {
            @Override
            protected void writeContacts(List<Contact> contacts) throws IOException {
                objectMapper.writeValue(file, new Contacts(contacts));
            }
        };
    }

    private JacksonContactsReader(File file, ObjectMapper objectMapper) {
        this.file = file;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Contact> read() {
        try {
            return objectMapper.readValue(file, new TypeReference<List<Contact>>() {});
        } catch (IOException e) {
            throw new ContactsException("Error on read file '" + file + "': " + e.getMessage(), e);
        }
    }

    @Override
    public void write(List<Contact> contacts) {
        try {
            writeContacts(contacts);
        } catch (IOException e) {
            throw new ContactsException("Error on write file '" + file + "': " + e.getMessage(), e);
        }
    }

    protected void writeContacts(List<Contact> contacts) throws IOException {
        objectMapper.writeValue(file, contacts);
    }

    @JacksonXmlRootElement(localName = "contacts")
    private static class Contacts {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "contact")
        List<Contact> contacts;

        public Contacts(List<Contact> contacts) {
            this.contacts = contacts;
        }
    }


}
