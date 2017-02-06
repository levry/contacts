package ru.levry.contacts.store

import ru.levry.contacts.data.Contact
import ru.levry.contacts.data.ContactsSearch
import spock.lang.Specification

/**
 * @author levry
 */
class ListContactsStoreSpec extends Specification {

    def "should be set id on add contact"() {
        given:
        def contactsStore = new ListContactsStore()

        Contact contact = new Contact(
                lastName: 'lastName'
        )

        when:
        contactsStore.add(contact)

        then:
        contact.id != null
    }

    def "should be save all fields on add contact"() {
        given:
        def contactsStore = new ListContactsStore()
        Contact contact = new Contact(
                lastName: 'lastName',
                firstName: 'firstName',
                middleName: 'middleName',
                comment: 'comment',
                phones: ['phone1', 'phone2']
        )

        when:
        contactsStore.add(contact)

        then:
        contact.id != null
        contact.lastName == 'lastName'
        contact.firstName == 'firstName'
        contact.middleName == 'middleName'
        contact.comment == 'comment'
        contact.phones as Set == ['phone1', 'phone2'] as Set
    }

    def "should be return contact on get by id"() {
        given:
        def contactsStore = new ListContactsStore(
                [23L: new Contact(id: 23L, lastName: 'lastName')]
        )

        when:
        def contact = contactsStore.get(23L)

        then:
        contact != null
        contact.id == 23L
        contact.lastName == 'lastName'
    }

    def "should by update fields"() {
        given:
        def contactsStore = new ListContactsStore(
                [2L: new Contact(
                        id: 2L,
                        lastName: 'lastName',
                        firstName: 'firstName',
                        middleName: 'middleName',
                        comment: 'comment',
                        phones: ['phone1']
                )]
        )

        when:
        contactsStore.update(2L, new Contact(
                lastName: 'lastName update',
                middleName: 'middleName update',
                firstName: 'firstName update',
                comment: 'comment update',
                phones: ['phone2', 'phone3']
        ))

        then:
        def contract = contactsStore.get(2L)
        contract.lastName == 'lastName update'
        contract.firstName == 'firstName update'
        contract.middleName == 'middleName update'
        contract.comment == 'comment update'
        contract.phones as Set == ['phone2', 'phone3'] as Set
    }

    def "remove contact by id"() {
        given:
        def contactsStore = new ListContactsStore(
                [4L: new Contact(
                        id: 4L,
                        lastName: 'lastName'
                )]
        )

        when:
        contactsStore.remove(4L)

        then:
        contactsStore.get(4L) == null
    }

    def "search contacts by lastName"() {
        given:
        def contactsStore = new ListContactsStore(
            [
                1L: new Contact(id: 1L, lastName: 'Van Damme'),
                2L: new Contact(id: 2L, lastName: 'Norris'),
                3L: new Contact(id: 3L, lastName: 'Stallone'),
                4L: new Contact(id: 4L, lastName: 'Neill'),
            ]
        )

        when:
        def contacts = contactsStore.findBy(new ContactsSearch(lastName: 'n'))

        then:
        contacts
        contacts*.id == [2L, 4L]
    }

    def "search contacts by firstName"() {
        given:
        def contactsStore = new ListContactsStore(
                [
                        1L: new Contact(id: 1L, firstName: 'Jean-Clode'),
                        2L: new Contact(id: 2L, firstName: 'Chuck'),
                        3L: new Contact(id: 3L, firstName: 'Silverster'),
                        4L: new Contact(id: 4L, firstName: 'Sam'),
                ]
        )

        when:
        def contacts = contactsStore.findBy(new ContactsSearch(firstName: 'c'))

        then:
        contacts
        contacts*.id == [2L]
    }

    def "convert from list"() {
        given:
        def list = [
                new Contact(id: 3),
                new Contact(id: 14)
        ]

        when:
        def contacts = ListContactsStore.contactsList(list)

        then:
        contacts.size() == 2
        contacts.currentCounter() == 14
    }
}
