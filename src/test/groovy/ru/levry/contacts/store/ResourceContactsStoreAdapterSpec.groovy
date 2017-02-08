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

    def "check exists contact"() {
        def contacts = [
                new Contact(id: 2L, lastName: 'Norris'),
                new Contact(id: 15L, lastName: 'Statham'),
                new Contact(id: 90L, lastName: 'Chan')
        ]
        def reader = Stub(ContactsReader) {
            read() >> contacts
        }
        ResourceContactsStoreAdapter adapter = new ResourceContactsStoreAdapter(reader)

        expect:
        adapter.exists(2L)
        adapter.exists(15L)
        adapter.exists(90L)
        !adapter.exists(1L)
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
        def found = adapter.get(contact.id)

        then:
        found.isPresent()
        found.get() == contact
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
        def updated = adapter.get(contact.id).get()

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
        !adapter.get(2L).isPresent()
    }

    def "remove phone of contact"() {
        given:
        def contact = new Contact(
                id: 10L,
                lastName: 'lastName',
                firstName: 'firstName',
                middleName: 'middleName',
                comment: 'comment',
                phones: ['001122', 'removed']
        )
        def reader = Stub(ContactsReader) {
            read() >> [contact]
        }
        def adapter = new ResourceContactsStoreAdapter(reader)

        when:
        adapter.removePhone(10L, 'removed')

        then:
        contact.phones == ['001122'] as Set
    }

    def "add phone to contact"() {
        given:
        def contact = new Contact(
                id: 10L,
                lastName: 'lastName',
                phones: ['001122']
        )
        def reader = Stub(ContactsReader) {
            read() >> [contact]
        }
        def adapter = new ResourceContactsStoreAdapter(reader)

        when:
        adapter.addPhone(10L, 'new phone')

        then:
        contact.phones == ['001122', 'new phone'] as Set
    }

    def "put phones to contact"() {
        given:
        def contact = new Contact(
                id: 10L,
                lastName: 'lastName',
                phones: ['001122']
        )
        def reader = Stub(ContactsReader) {
            read() >> [contact]
        }
        def adapter = new ResourceContactsStoreAdapter(reader)

        when:
        adapter.putPhones(10L, ['22', '33'] as Set)

        then:
        contact.phones == ['22','33'] as Set
    }
}
