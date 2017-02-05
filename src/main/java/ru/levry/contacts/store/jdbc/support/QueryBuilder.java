package ru.levry.contacts.store.jdbc.support;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author levry
 */
public class QueryBuilder implements Criteria {
    private final String sql;
    private final List<String> expressions = new ArrayList<>();
    private final List<Object> arguments = new ArrayList<>();
    private final List<String> orders = new ArrayList<>();

    public QueryBuilder(String sql) {
        this.sql = sql;
    }

    @Override
    public void like(String property, String value) {
        by(property, "LIKE", value + "%");
    }

    @Override
    public void eq(String property, Object value) {
        by(property, "=", value);
    }

    private void by(String property, String op, Object value) {
        expressions.add(property + " " + op + " ?");
        arguments.add(value);
    }

    public void orderAsc(String property) {
        orderBy(property, "ASC");
    }

    public void orderDesc(String property) {
        orderBy(property, "DESC");
    }

    private void orderBy(String property, String order) {
        orders.add(property + " " + order);
    }

    public String getSql() {
        StringBuilder buf = new StringBuilder(sql);
        appendWhere(buf);
        appendOrderBy(buf);
        return buf.toString();
    }

    public Object[] getArgs() {
        return arguments.toArray();
    }

    private StringBuilder appendWhere(StringBuilder sql) {
        if(!expressions.isEmpty()) {
            StringJoiner joiner = new StringJoiner(" AND ");
            expressions.forEach(joiner::add);
            sql.append(" WHERE ").append(joiner);
        }
        return sql;
    }

    private StringBuilder appendOrderBy(StringBuilder sql) {
        if(!orders.isEmpty()) {
            StringJoiner joiner = new StringJoiner(", ");
            orders.forEach(joiner::add);
            sql.append(" ORDER BY ").append(joiner);
        }
        return sql;
    }
}
