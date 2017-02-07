package ru.levry.contacts.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import ru.levry.contacts.ContactsException;
import ru.levry.contacts.data.ContactsStore;
import ru.levry.contacts.store.ListContactsStore;
import ru.levry.contacts.store.ResourceContactsStoreAdapter;
import ru.levry.contacts.store.jdbc.JdbcContactsStore;
import ru.levry.contacts.store.reader.ContactsReader;
import ru.levry.contacts.store.reader.JacksonContactsReader;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

/**
 * @author levry
 */
@Configuration
@EnableConfigurationProperties(ContactsProperties.class)
public class ContactsConfiguration {

    @Autowired
    private ResourceLoader resourceLoader;

    @ConditionalOnProperty(name = "contacts.store", havingValue = "mem")
    @Bean
    public ContactsStore memoryContactStore() {
        return new ListContactsStore();
    }

    @ConditionalOnProperty(name = "contacts.store", havingValue = "json")
    @Bean
    public ContactsStore jsonContactsStore(ContactsProperties props) {
        return createContactsFromResource(props.getFilename(), JacksonContactsReader::jsonReader);
    }

    @ConditionalOnProperty(name = "contacts.store", havingValue = "xml")
    @Bean
    public ContactsStore xmlContactsStore(ContactsProperties props) {
        return createContactsFromResource(props.getFilename(), JacksonContactsReader::xmlReader);
    }

    @ConditionalOnProperty(name = "contacts.store", havingValue = "db")
    @Import(DataSourceAutoConfiguration.class)
    static class JdbcContactsConfig {
        @Bean
        public ContactsStore dbContactStore(DataSource dataSource) {
            return new JdbcContactsStore(dataSource);
        }
    }

    private ContactsStore createContactsFromResource(String filename, Function<File, ContactsReader> readerSupplier) {
        try {
            File file = getFile(filename);
            ContactsReader reader = readerSupplier.apply(file);
            return new ResourceContactsStoreAdapter(reader);
        } catch (IOException e) {
            throw new ContactsException("Cannot create a contacts store: " + e.getMessage(), e);
        }
    }

    private File getFile(String filename) throws IOException {
        return resourceLoader.getResource(filename).getFile();
    }
}
