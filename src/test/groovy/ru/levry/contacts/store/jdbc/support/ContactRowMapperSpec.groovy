package ru.levry.contacts.store.jdbc.support

import spock.lang.Specification

import java.sql.ResultSet
import java.sql.SQLException

/**
 * @author levry
 */
class ContactRowMapperSpec extends Specification {

    def "map row"() {
        given:
        def rs = Stub(ResultSet) {
            getLong('id') >> 23L
            getString('lastName') >>'lastName'
            getString('firstName') >> 'firstName'
            getString('middleName') >> 'middleName'
            getString('comment') >> 'comment'
            getString('phone') >> '123'
        }
        def rowMapper = new ContactRowMapper()

        when:
        def contact = rowMapper.mapRow(rs, 0)

        then:
        contact.id == 23L
        contact.lastName == 'lastName'
        contact.firstName == 'firstName'
        contact.middleName == 'middleName'
        contact.comment == 'comment'
        contact.phones as Set == ['123'] as Set
    }

    def "map row without phone column"() {
        given:
        def rs = Stub(ResultSet) {
            getLong('id') >> 23L
            getString('lastName') >>'lastName'
            getString('firstName') >> 'firstName'
            getString('middleName') >> 'middleName'
            getString('comment') >> 'comment'
            getString('phone') >> { throw new SQLException('Unknown column') }
        }
        def rowMapper = new ContactRowMapper()

        when:
        def contact = rowMapper.mapRow(rs, 0)

        then:
        contact.id == 23L
        contact.lastName == 'lastName'
        contact.firstName == 'firstName'
        contact.middleName == 'middleName'
        contact.comment == 'comment'
        contact.phones == null
    }

    def "map row with null phone"() {
        given:
        def rs = Stub(ResultSet) {
            getLong('id') >> 23L
            getString('lastName') >>'lastName'
            getString('firstName') >> 'firstName'
            getString('middleName') >> 'middleName'
            getString('comment') >> 'comment'
            getString('phone') >> null
        }
        def rowMapper = new ContactRowMapper()

        when:
        def contact = rowMapper.mapRow(rs, 0)

        then:
        contact.id == 23L
        contact.lastName == 'lastName'
        contact.firstName == 'firstName'
        contact.middleName == 'middleName'
        contact.comment == 'comment'
        contact.phones == null
    }
}
