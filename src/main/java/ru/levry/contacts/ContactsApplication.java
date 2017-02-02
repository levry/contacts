package ru.levry.contacts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.levry.contacts.data.ContactsStore;
import ru.levry.contacts.store.ListContactsStore;

@SpringBootApplication
public class ContactsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactsApplication.class, args);
	}

	@Bean
	public ContactsStore contactsStore() {
		return new ListContactsStore();
	}


}
