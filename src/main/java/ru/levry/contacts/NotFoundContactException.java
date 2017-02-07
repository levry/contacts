package ru.levry.contacts;

/**
 * @author levry
 */
public class NotFoundContactException extends ContactsException {
    public NotFoundContactException(Long id) {
        super("Not found contact by id: " + id);
    }
}
