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
        def opt = contactsStore.get(23L)
        def contact = opt.get()

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
        def contract = contactsStore.get(2L).get()
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
        !contactsStore.get(4L).isPresent()
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

    def "remove phone"() {
        given:
        def contact = new Contact(id: 12L, phones: ['112233', 'to remove'])
        def contacts = ListContactsStore.contactsList([contact])

        when:
        contacts.removePhone(12L, 'to remove')

        then:
        contact.phones == ['112233'] as Set
    }

    def "should not throws if remove phone for unknown contact"() {
        given:
        def contacts = ListContactsStore.contactsList([])

        when:
        contacts.removePhone(22L, 'remove')

        then:
        noExceptionThrown()
    }

    def "add phone to contact"() {
        given:
        def contact = new Contact(id: 1L, phones: ['223344'])
        def contacts = ListContactsStore.contactsList([contact])

        when:
        contacts.addPhone(1L, 'new phone')

        then:
        contact.phones == ['223344', 'new phone'] as Set
    }

    def "should not throws if add phone to unknown contact"() {
        given:
        def contacts = ListContactsStore.contactsList([])

        when:
        contacts.addPhone(505L, 'phone')

        then:
        noExceptionThrown()
    }

}
