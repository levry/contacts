package ru.levry.contacts.store.jdbc.support;

import org.springframework.jdbc.core.RowMapper;
import ru.levry.contacts.data.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author levry
 */
public class ContactRowMapper implements RowMapper<Contact> {
    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getLong("id"));
        contact.setLastName(rs.getString("lastName"));
        contact.setFirstName(rs.getString("firstName"));
        contact.setMiddleName(rs.getString("middleName"));
        contact.setComment(rs.getString("comment"));
        tryString(rs, "phone").ifPresent(contact::addPhone);
        return contact;
    }

    private Optional<String> tryString(ResultSet rs, String columnLabel) {
        try {
            return Optional.ofNullable(rs.getString(columnLabel));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
