package ru.levry.contacts

import ru.levry.contacts.data.Contact

/**
 * @author levry
 */
class ContactsTestUtils {
    static void testContact(Contact expected, Contact actual) {
        assert actual.id == expected.id
        assert actual.lastName == expected.lastName
        assert actual.firstName == expected.firstName
        assert actual.middleName == expected.middleName
        assert actual.comment == expected.comment
        assert actual.phones as Set == expected.phones as Set
    }
}
