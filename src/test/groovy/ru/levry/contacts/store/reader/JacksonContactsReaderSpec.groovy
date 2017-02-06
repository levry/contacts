package ru.levry.contacts.store.reader

import org.springframework.util.ResourceUtils
import ru.levry.contacts.data.Contact
import spock.lang.Specification

import static ru.levry.contacts.ContactsTestUtils.testContact

/**
 * @author levry
 */
class JacksonContactsReaderSpec extends Specification {

    private static final Contact CHUCK = new Contact(
            id: 1,
            lastName: 'Norris',
            firstName: 'Chuck',
            comment: 'this is chuck',
            phones: ['12345678', '87654321', 'telephone']
    )

    private static final Contact STALONE = new Contact(
            id: 2,
            lastName: 'Stalone',
            firstName: 'Silvester',
            middleName: 'S',
            phones: []
    )

    private static final Contact ARNOLD = new Contact(
            id: 3,
            lastName: 'Schwarzenegger',
            firstName: 'Arnold',
            middleName: 'S',
            comment: 'terminator'
    )

    private static final Contact JEAN = new Contact(
            id: 10,
            lastName: 'Van Damme',
            firstName: 'Jean-Clode',
            comment: 'kickboxer',
            phones: ['phone1']
    )


    def "read contacts from json"() {
        given:
        def file = ResourceUtils.getFile('classpath:contacts.json')
        def reader = JacksonContactsReader.jsonReader(file)

        when:
        List<Contact> contacts = reader.read()

        then:
        contacts
        contacts.size() == 4
        testContact(CHUCK, contacts.find { it.id == CHUCK.id })
        testContact(STALONE, contacts.find { it.id == STALONE.id })
        testContact(ARNOLD, contacts.find { it.id == ARNOLD.id })
        testContact(JEAN, contacts.find { it.id == JEAN.id })
    }

    def "write contacts to json"() {
        given:
        def file = new File('contacts-test.json')
        file.createNewFile()

        def reader = JacksonContactsReader.jsonReader(file)
        def contacts = [
                new Contact(
                        id: 2L,
                        lastName: 'Lee',
                        firstName: 'Jet',
                        middleName: 'J',
                        comment: 'comment',
                        phones: ['000111']
                ),
                new Contact(
                        id: 3L,
                        lastName: 'Statham',
                        firstName: 'Jason',
                        phones: ['112233', '332211']
                )
        ]

        when:
        reader.write(contacts)

        then:
        file.text == '[{"id":2,"lastName":"Lee","firstName":"Jet","middleName":"J","comment":"comment","phones":["000111"]},{"id":3,"lastName":"Statham","firstName":"Jason","middleName":null,"comment":null,"phones":["112233","332211"]}]'

        cleanup:
        file.delete()
    }

    def "read contacts from xml"() {
        given:
        def file = ResourceUtils.getFile("classpath:contacts.xml")
        def reader = JacksonContactsReader.xmlReader(file)

        when:
        def contacts = reader.read()

        then:
        contacts.size() == 2
        testContact(CHUCK, contacts.find { it.id == CHUCK.id })
        testContact(ARNOLD, contacts.find { it.id == ARNOLD.id })
    }

    def "write contacts to xml"() {
        given:
        def file = new File('contacts-test.xml')
        file.createNewFile()

        def reader = JacksonContactsReader.xmlReader(file)
        def contacts = [
                new Contact(
                        id: 2L,
                        lastName: 'Lee',
                        firstName: 'Jet',
                        middleName: 'J',
                        comment: 'comment',
                        phones: ['000111']
                )
        ]

        when:
        reader.write(contacts)

        then:
        // TODO rename xml elements
        file.text == '<ArrayList><item><id>2</id><lastName>Lee</lastName><firstName>Jet</firstName><middleName>J</middleName><comment>comment</comment><phones><phones>000111</phones></phones></item></ArrayList>'
//        file.text == '<contacts><item><id>2</id><lastName>Lee</lastName><firstName>Jet</firstName><middleName>J</middleName><comment>comment</comment><phones><phones>000111</phones></phones></item></contacts>'

        cleanup:
        file.delete()
    }
}
