package ru.levry.contacts.config;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.levry.contacts.data.ContactsStore;
import ru.levry.contacts.store.ListContactsStore;
import ru.levry.contacts.store.ResourceContactsStoreAdapter;
import ru.levry.contacts.store.jdbc.JdbcContactsStore;
import ru.levry.contacts.store.reader.ContactsReader;
import ru.levry.contacts.store.reader.JacksonContactsReader;

import javax.sql.DataSource;

/**
 * @author levry
 */
@Configuration
@EnableConfigurationProperties(ContactsProperties.class)
public class ContactsConfiguration {

    @ConditionalOnProperty(name = "contacts.store", havingValue = "mem")
    @Bean
    public ContactsStore memoryContactStore() {
        return new ListContactsStore(new ConcurrentHashMap<>());
    }

    @ConditionalOnProperty(name = "contacts.store", havingValue = "json")
    @Bean(destroyMethod = "writeContacts")
    public ResourceContactsStoreAdapter jsonContactsStore(ContactsProperties props) throws Exception {
        ContactsReader reader = JacksonContactsReader.jsonReader(props.getFile());
        return new ResourceContactsStoreAdapter(reader, ListContactsStore::concurrentList);
    }

    @ConditionalOnProperty(name = "contacts.store", havingValue = "xml")
    @Bean(destroyMethod = "writeContacts")
    public ResourceContactsStoreAdapter xmlContactsStore(ContactsProperties props) throws Exception {
        ContactsReader reader = JacksonContactsReader.xmlReader(props.getFile());
        return new ResourceContactsStoreAdapter(reader, ListContactsStore::concurrentList);
    }

    @ConditionalOnProperty(name = "contacts.store", havingValue = "db")
    @Import(DataSourceAutoConfiguration.class)
    static class JdbcContactsConfig {
        @Bean
        public ContactsStore dbContactStore(DataSource dataSource) {
            return new JdbcContactsStore(dataSource);
        }
    }

}
