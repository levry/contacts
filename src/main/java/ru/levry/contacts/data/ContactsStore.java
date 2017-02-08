package ru.levry.contacts.data;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Основные методы работы с контактами
 *
 * @author levry
 */
public interface ContactsStore {

    /**
     * Проверяет существование контакта
     */
    boolean exists(long id);

    /**
     * Добавляет новый контакт
     */
    void add(Contact contact);

    /**
     * Извлекает контакт по идентификатору
     */
    Optional<Contact> get(long id);

    /**
     * Обновляет контакт
     */
    void update(long id, Contact contact);

    /**
     * Удалеяет контакт
     * @param id
     */
    void remove(long id);

    /**
     * Перезаписывает номера для контакта
     */
    void putPhones(long id, Set<String> phones);

    /**
     * Добавляет новый номер контакту
     */
    void addPhone(long id, String phone);

    /**
     * Удалеяет номер контакта
     */
    void removePhone(long id, String phone);

    /**
     * Поиск контактов
     */
    Collection<Contact> findBy(ContactsSearch search);

}