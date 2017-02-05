package ru.levry.contacts.data

import spock.lang.Specification

/**
 * @author levry
 */
class ContactsSearchSpec extends Specification {
    def "empty search"() {
        given:
        def search = new ContactsSearch(lastName: lastName, firstName: firstName)

        expect:
        search.empty == empty

        where:
        lastName | firstName || empty
        null     | null      || true
        ''       | null      || true
        null     | ''        || true
        'l'      | null      || false
        'l'      | ''        || false
        null     | 'f'       || false
        ''       | 'f'       || false
        'l'      | 'f'       || false
    }
}
