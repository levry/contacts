package ru.levry.contacts.store.jdbc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.jdbc.JdbcTestUtils
import ru.levry.contacts.data.Contact
import ru.levry.contacts.data.ContactsSearch
import ru.levry.contacts.data.ContactsStore
import spock.lang.Specification

import javax.sql.DataSource

import static ru.levry.contacts.ContactsTestUtils.testContact

/**
 * @author levry
 */
@JdbcTest
@TestPropertySource(properties = "contacts.store=db")
@ContextConfiguration
class JdbcContactsStoreSpec extends Specification {

    private static final Contact STALONE = new Contact(
            id: 3L,
            lastName: 'Stalone',
            firstName: 'Silvester',
            comment: 'pantalone'
    )

    private static final Contact STATHAM = new Contact(
            id: 4L,
            lastName: 'Statham',
            firstName: 'Jason',
            comment: 'must be removed',
            phones: ['telephone']
    )

    private static final CHUCK = new Contact(
            id: 2L,
            lastName: 'Norris',
            firstName: 'Chuck',
            middleName: 'M',
            comment: 'this is chuck',
            phones: ['2223334', '3332225']
    )

    @Autowired
    JdbcTemplate jdbcTemplate

    @Autowired
    ContactsStore contactsStore

    def "exists contact"() {
        when:
        def exists = contactsStore.exists(CHUCK.id)

        then:
        exists
    }

    def "should be return false if not exists contact"() {
        when:
        def exists = contactsStore.exists(-1L)

        then:
        !exists
    }

    def "get contact without phones"() {
        when:
        def contact = contactsStore.get(STALONE.id)

        then:
        contact.isPresent()
        testContact(STALONE, contact.get())
    }

    def "get contact with one phone"() {
        when:
        def contact = contactsStore.get(STATHAM.id)

        then:
        contact.isPresent()
        testContact(STATHAM, contact.get())
    }

    def "get contact with more one phones"() {
        when:
        def contact = contactsStore.get(CHUCK.id)

        then:
        contact.isPresent()
        testContact(CHUCK, contact.get())
    }

    def "add contact"() {
        given:
        def contact = new Contact(
                lastName: 'LastName',
                firstName: 'FirstName',
                middleName: 'MiddleName',
                comment: 'Comment',
                phones: ['111111','222222']
        )

        when:
        contactsStore.add(contact)

        then:
        contact.id != null

        when:
        def created = contactsStore.get(contact.id).get()

        then:
        created.firstName == contact.firstName
        created.middleName == contact.middleName
        created.lastName == contact.lastName
        created.comment == contact.comment
        created.phones as Set == contact.phones as Set
    }

    def "update contact"() {
        when:
        contactsStore.update(1L, new Contact(
                lastName: 'Van Damme updated',
                firstName: 'Jean-Clode updated',
                middleName: 'updated',
                comment: 'Volvo updated',
                phones: ['phone']
        ))

        then:
        def updated = contactsStore.get(1L).get()
        updated.lastName == 'Van Damme updated'
        updated.firstName == 'Jean-Clode updated'
        updated.middleName == 'updated'
        updated.comment == 'Volvo updated'
        updated.phones as Set == ['phone'] as Set
    }

    def "remove contact by id"() {
        when:
        contactsStore.remove(4L)

        then:
        JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Contacts", "id = 4") == 0
        JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "Phones", "contact_ = 4") == 0
    }

    def "remove phone of contact"() {
        when:
        contactsStore.removePhone(CHUCK.id, '3332225')

        then:
        readContactPhones(CHUCK.id) == ['2223334'] as Set
    }

    def "add phone to contact"() {
        when:
        contactsStore.addPhone(CHUCK.id, 'new phone')

        then:
        readContactPhones(CHUCK.id) == ['2223334', '3332225', 'new phone'] as Set
    }

    def "put phones to contact"() {
        when:
        contactsStore.putPhones(CHUCK.id, ['111', '333', 'chuckphone'] as Set)

        then:
        readContactPhones(CHUCK.id) == ['111', '333', 'chuckphone'] as Set
    }

    def "should not throws on duplicate phone"() {
        when:
        contactsStore.addPhone(CHUCK.id, '2223334')

        then:
        noExceptionThrown()
        readContactPhones(CHUCK.id) == ['2223334', '3332225'] as Set
    }

    private Set<String> readContactPhones(Long id) {
        jdbcTemplate.queryForList('SELECT phone FROM Phones WHERE contact_ = ?', String, id) as Set
    }

    def "search contacts by lastName"() {
        when:
        def contacts = contactsStore.findBy(new ContactsSearch(lastName: 'S'))

        then:
        contacts
        contacts*.id == [3L, 4L]
        testContact(STALONE, contacts.find { it.id == 3L })
        testContact(STATHAM, contacts.find { it.id == 4L })
    }

    def "search contacts by firstName"() {
        when:
        def contacts = contactsStore.findBy(new ContactsSearch(firstName: 'C'))

        then:
        contacts
        contacts*.id == [2L]
        testContact(CHUCK, contacts.find { it.id == 2L })
    }

    def "search contacts by lastName and firstName"() {
        when:
        def contacts = contactsStore.findBy(new ContactsSearch(lastName: 'S', firstName: 'S'))

        then:
        contacts*.id == [3L]
        testContact(STALONE, contacts.find { it.id == 3L })
    }

    @TestConfiguration
    static class JdbcContractsConfig {

        @Bean
        ContactsStore contactsStore(DataSource dataSource) {
            new JdbcContactsStore(dataSource)
        }
    }
}
