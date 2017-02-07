package ru.levry.contacts.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author levry
 */
@ConfigurationProperties("contacts")
@Validated
public class ContactsProperties {

    enum ContactsStoreType {
        db,
        json,
        xml,
        mem
    }

    @NotNull
    private ContactsStoreType store;
    private String filename;

    public ContactsStoreType getStore() {
        return store;
    }

    public void setStore(ContactsStoreType store) {
        this.store = store;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
