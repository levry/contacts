package ru.levry.contacts.data

import spock.lang.Specification

/**
 * @author levry
 */
class ContactSpec extends Specification {

    def "equals contacts"() {
        expect:
        new Contact(id: 2L) == new Contact(id: 2L)
        new Contact(id: 2L) != new Contact(id: 1L)
        new Contact(id: 3L) != new Object()
    }

    def "add phone if phones is null"() {
        given:
        def contact = new Contact(phones: null)

        when:
        contact.addPhone('phone')

        then:
        contact.phones as Set == ['phone'] as Set
    }

    def "add phone if phones is not null"() {
        given:
        def contact = new Contact(phones: new HashSet<String>())

        when:
        contact.addPhone('phone')

        then:
        contact.phones as Set == ['phone'] as Set
    }

    def "add phone if phones is not empty"() {
        given:
        def contact = new Contact(phones: ['phone1'] as Set)

        when:
        contact.addPhone('phone2')

        then:
        contact.phones as Set == ['phone1', 'phone2'] as Set
    }

    def "remove phone if phones is null"() {
        given:
        def contact = new Contact()

        when:
        contact.removePhone('phone')

        then:
        noExceptionThrown()
    }

    def "remove phone"() {
        given:
        def contact = new Contact(phones: ['123', 'removed'] as Set)

        when:
        contact.removePhone('removed')

        then:
        contact.phones == ['123'] as Set
    }
}
