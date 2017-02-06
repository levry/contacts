package ru.levry.contacts;

/**
 * @author levry
 */
public class ContactsException extends RuntimeException {

    public ContactsException(String s) {
        super(s);
    }

    public ContactsException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
