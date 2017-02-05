package ru.levry.contacts.store.jdbc.support

import spock.lang.Specification

/**
 * @author levry
 */
class NotNullCriteriaSpec extends Specification {
    def "Eq"() {
        given:
        def filters = [:]
        def criteria = Stub(Criteria) {
            eq(_, _) >> { p, v -> filters.property = p; filters.value = v }
        }

        when:
        def notNull = new NotNullCriteria(criteria)
        notNull.eq('city.id', 2)

        then:
        filters.property == 'city.id'
        filters.value == 2
    }

    def "Eq with null value"() {
        given:
        def filters = [:]
        def criteria = Stub(Criteria) {
            eq(_, _) >> { p, v -> filters.property = p; filters.value = v }
        }

        when:
        def notNull = new NotNullCriteria(criteria)
        notNull.eq('city.id', null)

        then:
        filters.isEmpty()
    }

    def "Like not null value"() {
        given:
        def filters = [:]
        def criteria = Stub(Criteria) {
            like(_, _) >> { p, v -> filters.property = p; filters.value = v }
        }

        when:
        def notNull = new NotNullCriteria(criteria)
        notNull.like('prop', 'v')

        then:
        filters.property == 'prop'
        filters.value == 'v'
    }

    def "Like null value"() {
        given:
        def filters = [:]
        def criteria = Stub(Criteria) {
            like(_, _) >> { p, v -> filters.property = p; filters.value = v }
        }

        when:
        def notNull = new NotNullCriteria(criteria)
        notNull.like('prop', null)

        then:
        filters.isEmpty()
    }

    def "Like empty value"() {
        given:
        def filters = [:]
        def criteria = Stub(Criteria) {
            like(_, _) >> { p, v -> filters.property = p; filters.value = v }
        }

        when:
        def notNull = new NotNullCriteria(criteria)
        notNull.like('prop', '')

        then:
        filters.isEmpty()
    }
}
