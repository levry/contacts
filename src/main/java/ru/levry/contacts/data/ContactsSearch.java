package ru.levry.contacts.data;

import org.springframework.util.StringUtils;

/**
 * @author levry
 */
public class ContactsSearch {

    private String lastName;
    private String firstName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(lastName) && StringUtils.isEmpty(firstName);
    }
}
