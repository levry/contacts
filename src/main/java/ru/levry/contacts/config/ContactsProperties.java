package ru.levry.contacts.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

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
    private Resource filename;

    public ContactsStoreType getStore() {
        return store;
    }

    public void setStore(ContactsStoreType store) {
        this.store = store;
    }

    public Resource getFilename() {
        return filename;
    }

    public void setFilename(Resource filename) {
        this.filename = filename;
    }

    public File getFile() throws IOException {
        return filename.getFile();
    }

}
