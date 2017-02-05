package ru.levry.contacts.store.jdbc.support

import spock.lang.Specification

/**
 * @author levry
 */
class QueryBuilderSpec extends Specification {
    def "where empty criteria"() {
        given:
        def builder = new QueryBuilder('SELECT c.id, c.fullName FROM Contacts AS c')

        expect:
        builder.sql == 'SELECT c.id, c.fullName FROM Contacts AS c'
        builder.args as List == []
    }

    def "where like"() {
        given:
        def builder = new QueryBuilder('SELECT c.id, c.fullName FROM Contacts AS c')

        when:
        builder.like('c.fullName', 'Norris')

        then:
        builder.sql == 'SELECT c.id, c.fullName FROM Contacts AS c WHERE c.fullName LIKE ?'
        builder.args as List == ['Norris%']
    }

    def "where eq"() {
        given:
        def builder = new QueryBuilder('SELECT c.id, c.fullName FROM Contacts AS c')

        when:
        builder.eq('c.fullName', 'Norris')

        then:
        builder.sql == 'SELECT c.id, c.fullName FROM Contacts AS c WHERE c.fullName = ?'
        builder.args as List == ['Norris']
    }

    def "order by asc"() {
        given:
        def builder = new QueryBuilder('SELECT id, fullName FROM Contacts')

        when:
        builder.orderAsc('id')

        then:
        builder.sql == 'SELECT id, fullName FROM Contacts ORDER BY id ASC'
    }

    def "order by desc"() {
        given:
        def builder = new QueryBuilder('SELECT id, fullName FROM Contacts')

        when:
        builder.orderDesc('fullName')

        then:
        builder.sql == 'SELECT id, fullName FROM Contacts ORDER BY fullName DESC'
    }

    def "order by multiple fields"() {
        given:
        def builder = new QueryBuilder('SELECT id, fullname FROM Contacts')

        when:
        builder.orderAsc('id')
        builder.orderDesc('fullname')

        then:
        builder.sql == 'SELECT id, fullname FROM Contacts ORDER BY id ASC, fullname DESC'
    }

    def "where and order"() {
        given:
        def builder = new QueryBuilder('SELECT id, fullname FROM Contacts')

        when:
        builder.like('fullname', 'Chuck')
        builder.orderAsc('id')

        then:
        builder.sql == 'SELECT id, fullname FROM Contacts WHERE fullname LIKE ? ORDER BY id ASC'
        builder.args as List == ['Chuck%']
    }
}
