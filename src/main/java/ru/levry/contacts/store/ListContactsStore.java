package ru.levry.contacts.store;

import org.springframework.util.StringUtils;
import ru.levry.contacts.data.Contact;
import ru.levry.contacts.data.ContactsSearch;
import ru.levry.contacts.data.ContactsStore;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

/**
 * @author levry
 */
public class ListContactsStore implements ContactsStore {

    private final AtomicLong counter;
    private final Map<Long, Contact> contacts;

    public static ListContactsStore contactsList(List<Contact> list) {
        if(list.isEmpty()) {
            return new ListContactsStore();
        }

        Map<Long, Contact> contacts = list.stream().collect(Collectors.toMap(Contact::getId, Function.identity()));
        long valueCounter = Collections.max(contacts.keySet());
        return new ListContactsStore(contacts, valueCounter);
    }

    public ListContactsStore() {
        this(new HashMap<>());
    }

    public ListContactsStore(Map<Long, Contact> contacts) {
        this.contacts = contacts;
        this.counter = new AtomicLong();
    }

    public ListContactsStore(Map<Long, Contact> contacts, long valueCounter) {
        this.contacts = contacts;
        this.counter = new AtomicLong(valueCounter);
    }

    @Override
    public void add(Contact contact) {
        long id = counter.incrementAndGet();
        contact.setId(id);
        contacts.put(id, contact);
    }

    @Override
    public Contact get(long id) {
        return contacts.get(id);
    }

    @Override
    public void update(long id, Contact contact) {
        contacts.put(id, contact);
    }

    @Override
    public void remove(long id) {
        contacts.remove(id);
    }

    @Override
    public Collection<Contact> findBy(ContactsSearch search) {
        Collection<Contact> values = this.contacts.values();
        if(search.isEmpty()) {
            return values;
        }

        Stream<Contact> stream = values.stream();
        if (!StringUtils.isEmpty(search.getFirstName())) {
            stream = stream.filter(startsWith(Contact::getFirstName, search.getFirstName()));
        }
        if (!StringUtils.isEmpty(search.getLastName())) {
            stream = stream.filter(startsWith(Contact::getLastName, search.getLastName()));
        }
        return stream.collect(Collectors.toList());
    }

    private Predicate<Contact> startsWith(Function<Contact, String> func, String prefix) {
        return c -> {
            String value = func.apply(c);
            return nonNull(value) && value.toLowerCase().startsWith(prefix);
        };
    }

    public int size() {
        return contacts.size();
    }

    public long currentCounter() {
        return counter.get();
    }
}
