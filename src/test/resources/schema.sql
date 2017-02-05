CREATE TABLE Contacts (
  id INT identity(1, 1) primary key not null,
  lastName VARCHAR(255),
  firstName VARCHAR(255),
  middleName VARCHAR(255),
  comment VARCHAR(500)
);

CREATE TABLE Phones (
  contact_ INT NOT NULL,
  phone CHAR(10) NOT NULL,
  CONSTRAINT PK_Phones PRIMARY KEY (contact_, phone),
  CONSTRAINT FK_Phones_contract FOREIGN KEY (contact_) REFERENCES Contacts(id) ON DELETE CASCADE
);