package ru.levry.contacts.store

import ru.levry.contacts.data.Contact
import ru.levry.contacts.data.ContactsSearch
import ru.levry.contacts.store.reader.ContactsReader
import spock.lang.Specification

/**
 * @author levry
 */
class ResourceContactsStoreAdapterSpec extends Specification {

    def "find contacts"() {
        given:

        def contacts = [
                new Contact(id: 2, lastName: 'Norris'),
                new Contact(id: 15, lastName: 'Statham'),
                new Contact(id: 90, lastName: 'Chan')
        ]
        def reader = Stub(ContactsReader) {
            read() >> contacts
        }
        ResourceContactsStoreAdapter adapter = new ResourceContactsStoreAdapter(reader)

        when:
        def foundContacts = adapter.findBy(new ContactsSearch())

        then:
        foundContacts
        foundContacts as Set == contacts as Set
    }

    def "get contact"() {
        given:

        def contact = new Contact(
                id: 11,
                lastName: 'Chan',
                firstName: 'Jackie',
                phones: ['001122']
        )
        def reader = Stub(ContactsReader) {
            read() >> [contact]
        }
        ResourceContactsStoreAdapter adapter = new ResourceContactsStoreAdapter(reader)

        when:
        def founded = adapter.get(contact.id)

        then:
        founded == contact
    }

    def "add contact"() {
        given:
        def reader = Stub(ContactsReader) {
            read() >> []
        }
        def adapter = new ResourceContactsStoreAdapter(reader)
        def contact = new Contact(firstName: 'Chuck')

        when:
        adapter.add(contact)

        then:
        contact.id != null
    }

    def "update contact"() {
        given:
        def contact = new Contact(
                id: 12L,
                lastName: 'lastName',
                firstName: 'firstName',
                middleName: 'middleName',
                comment: 'comment',
                phones: ['001122']
        )
        def reader = Stub(ContactsReader) {
            read() >> [contact]
        }
        def adapter = new ResourceContactsStoreAdapter(reader)

        when:
        adapter.update(12L, new Contact(
                lastName: 'lastName updated',
                firstName: 'firstName updated',
                middleName: 'middleName updated',
                comment: 'comment updated',
                phones: ['221100']
        ))
        def updated = adapter.get(contact.id)

        then:
        updated.lastName == 'lastName updated'
        updated.firstName == 'firstName updated'
        updated.middleName == 'middleName updated'
        updated.comment == 'comment updated'
        updated.phones as Set == ['221100'] as Set
    }

    def "remove contact"() {
        given:
        def contact = new Contact(id: 2L)
        def reader = Stub(ContactsReader) {
            read() >> [contact]
        }
        def adapter = new ResourceContactsStoreAdapter(reader)

        when:
        adapter.remove(2L)

        then:
        adapter.get(2L) == null
    }
}
