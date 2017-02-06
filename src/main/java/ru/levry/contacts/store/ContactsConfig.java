package ru.levry.contacts.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.validation.annotation.Validated;
import ru.levry.contacts.ContactsException;
import ru.levry.contacts.data.ContactsStore;
import ru.levry.contacts.store.reader.ContactsReader;
import ru.levry.contacts.store.reader.JacksonContactsReader;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

/**
 * @author levry
 */
@ConfigurationProperties(prefix = "contacts")
@Validated
public class ContactsConfig {

    enum Store {
        db(ContactsConfig::createJdbcContacts),
        json(ContactsConfig::createJsonContacts),
        xml(ContactsConfig::createXmlContacts);

        private final Function<ContactsConfig, ContactsStore> contactsStoreFactory;

        Store(Function<ContactsConfig, ContactsStore> contactsStoreFactory) {
            this.contactsStoreFactory = contactsStoreFactory;
        }

        ContactsStore createContactsStore(ContactsConfig config) {
            return contactsStoreFactory.apply(config);
        }
    }

    @Autowired
    private ResourceLoader resourceLoader;

    @NotNull
    private Store store;
    @NotNull
    private String url;
    private String username;
    private String password;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static ContactsStore createJdbcContacts(ContactsConfig config) {
        // TODO create JdbcContactsStore
        return null;
    }

    private static ContactsStore createJsonContacts(ContactsConfig config) {
        return createContactsFromResource(config, JacksonContactsReader::jsonReader);
    }

    private static ContactsStore createXmlContacts(ContactsConfig config) {
        return createContactsFromResource(config, JacksonContactsReader::xmlReader);
    }

    private static ContactsStore createContactsFromResource(ContactsConfig config, Function<File, ContactsReader> readerSupplier) {
        try {
            File file = config.getFile();
            ContactsReader reader = readerSupplier.apply(file);
            return new ResourceContactsStoreAdapter(reader);
        } catch (IOException e) {
            throw new ContactsException("Cannot create a contacts store: " + e.getMessage(), e);
        }
    }

    private File getFile() throws IOException {
        return resourceLoader.getResource(url).getFile();
    }
}
