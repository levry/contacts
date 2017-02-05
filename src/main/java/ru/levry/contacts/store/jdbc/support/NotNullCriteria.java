package ru.levry.contacts.store.jdbc.support;

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
        if(null != value && !"".equals(value)) {
            criteria.like(property, value);
        }
    }

    @Override
    public void eq(String property, Object value) {
        if(null != value) {
            criteria.eq(property, value);
        }
    }
}
