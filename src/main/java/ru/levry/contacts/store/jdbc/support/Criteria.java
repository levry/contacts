package ru.levry.contacts.store.jdbc.support;

/**
 * @author levry
 */
public interface Criteria {
    void like(String property, String value);

    void eq(String property, Object value);

    default Criteria notNull() {
        return new NotNullCriteria(this);
    }
}
