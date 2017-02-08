package ru.levry.contacts.store.jdbc.support;

import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author levry
 */
public class NotNullCriteria implements Criteria {
    private final Criteria criteria;

    public NotNullCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public void like(String property, String value) {
        if(!StringUtils.isEmpty(value)) {
            criteria.like(property, value);
        }
    }

    @Override
    public void eq(String property, Object value) {
        Optional.ofNullable(value).ifPresent(v -> criteria.eq(property, v));
    }
}
