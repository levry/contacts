package ru.levry.contacts.store.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ru.levry.contacts.ContactsException;
import ru.levry.contacts.data.Contact;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author levry
 */
public class JacksonContactsReader implements ContactsReader {
    private final File file;
    private final ObjectMapper objectMapper;

    public static ContactsReader jsonReader(File file) {
        return new JacksonContactsReader(file, new ObjectMapper());
    }

    public static ContactsReader xmlReader(File file) {
        XmlMapper objectMapper = new XmlMapper();
        return new JacksonContactsReader(file, objectMapper);
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
            objectMapper.writeValue(file, contacts);
        } catch (IOException e) {
            throw new ContactsException("Error on write file '" + file + "': " + e.getMessage(), e);
        }
    }
}
