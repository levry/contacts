package ru.levry.contacts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import ru.levry.contacts.config.ContactsConfiguration;

/**
 * @author levry
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Import(ContactsConfiguration.class)
public class ContactsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactsApplication.class, args);
	}

}
