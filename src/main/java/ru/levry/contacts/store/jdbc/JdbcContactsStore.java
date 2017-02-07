package ru.levry.contacts.store.jdbc;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;
import ru.levry.contacts.data.Contact;
import ru.levry.contacts.data.ContactsSearch;
import ru.levry.contacts.data.ContactsStore;
import ru.levry.contacts.store.jdbc.support.ContactRowMapper;
import ru.levry.contacts.store.jdbc.support.Criteria;
import ru.levry.contacts.store.jdbc.support.QueryBuilder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author levry
 */
public class JdbcContactsStore extends JdbcDaoSupport implements ContactsStore {

    private final RowMapper<Contact> rowMapper;

    public JdbcContactsStore(DataSource dataSource) {
        this(dataSource, new ContactRowMapper());
    }

    public JdbcContactsStore(DataSource dataSource, RowMapper<Contact> rowMapper) {
        setDataSource(dataSource);
        this.rowMapper = rowMapper;
    }

    @Override
    public void add(Contact contact) {
        Number id = insertContact(contact);
        contact.setId(id.longValue());
        insertPhones(id, contact.getPhones());
    }

    private Number insertContact(Contact contact) {
        return executeInsert("INSERT INTO Contacts (lastName, firstName, middleName, comment) VALUES (?, ?, ?, ?)", "id", ps -> {
                ps.setString(1, contact.getLastName());
                ps.setString(2, contact.getFirstName());
                ps.setString(3, contact.getMiddleName());
                ps.setString(4, contact.getComment());
            });
    }

    private void insertPhones(Number id, Set<String> phones) {
        if(!CollectionUtils.isEmpty(phones)) {
            List<Object[]> args = phones.stream()
                    .map(p -> new Object[] {id, p})
                    .collect(Collectors.toList());
            getJdbcTemplate().batchUpdate("INSERT INTO Phones (contact_, phone) VALUES (?, ?)", args);
        }
    }

    @Override
    public Optional<Contact> get(long id) {
        List<Contact> contacts = getJdbcTemplate().query(
            "SELECT c.id AS id, c.lastName AS lastName, c.firstName AS firstName, c.middleName AS middleName, " +
                "c.comment AS comment, p.phone AS phone " +
            "FROM Contacts AS c " +
                "LEFT JOIN Phones p ON p.contact_ = c.id " +
            "WHERE c.id = ?", getListResultSetExtractor(), id);
        return Optional.ofNullable(DataAccessUtils.singleResult(contacts));
    }

    @Override
    public void update(long id, Contact contact) {
        getJdbcTemplate().update("UPDATE Contacts SET lastName = ?, firstName = ?, middleName = ?, comment = ? WHERE id = ?",
                contact.getLastName(), contact.getFirstName(), contact.getMiddleName(), contact.getComment(), id);

        if (!CollectionUtils.isEmpty(contact.getPhones())) {
            getJdbcTemplate().update("DELETE FROM Phones WHERE contact_ = ?", id);
            insertPhones(id, contact.getPhones());
        }
    }

    @Override
    public void remove(long id) {
        getJdbcTemplate().update("DELETE FROM Contacts WHERE id = ?", id);
    }

    @Override
    public void addPhone(long id, String phone) {
        try {
            getJdbcTemplate().update("INSERT INTO Phones (contact_, phone) VALUES (?, ?)", id, phone);
        } catch (DuplicateKeyException e) {
            logger.warn("Duplicate phone '" + phone + "' for contact " + id + ": " + e.getMessage());
        }
    }

    @Override
    public void removePhone(long id, String phone) {
        getJdbcTemplate().update("DELETE FROM Phones WHERE contact_ = ? AND phone = ?", id, phone);
    }

    @Override
    public Collection<Contact> findBy(ContactsSearch search) {
        QueryBuilder builder = new QueryBuilder(
            "SELECT c.id AS id, c.lastName AS lastName, c.firstName AS firstName, c.middleName AS middleName, " +
                "c.comment AS comment, p.phone AS phone " +
            "FROM Contacts AS c " +
                "LEFT JOIN Phones p ON p.contact_ = c.id"
        );
        builder.orderAsc("c.id");

        Criteria filters = builder.notNull();
        filters.like("c.lastName", search.getLastName());
        filters.like("c.firstName", search.getFirstName());

        return getJdbcTemplate().query(builder.getSql(), builder.getArgs(), getListResultSetExtractor());
    }

    private ResultSetExtractor<List<Contact>> getListResultSetExtractor() {
        return rs -> {
            List<Contact> contacts = new ArrayList<>();
            Contact contact = new Contact();
            while (rs.next()) {
                long id = rs.getLong("id");

                if(!Objects.equals(id, contact.getId())) {
                    contact = rowMapper.mapRow(rs, 0);
                    contacts.add(contact);
                } else {
                    Optional.ofNullable(rs.getString("phone")).ifPresent(contact::addPhone);
                }
            }
            return contacts;
        };
    }

    private Number executeInsert(String insertSql, String keyColumn, PreparedStatementSetter pss) {
        KeyHolder generatedKeys = new GeneratedKeyHolder();
        getJdbcTemplate().update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql, new String[] {keyColumn} );
            pss.setValues(ps);
            return ps;
        }, generatedKeys);

        return generatedKeys.getKey();
    }
}
