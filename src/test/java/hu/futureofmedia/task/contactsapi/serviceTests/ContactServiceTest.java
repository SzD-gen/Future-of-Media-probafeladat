package hu.futureofmedia.task.contactsapi.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hu.futureofmedia.task.contactsapi.exceptions.BadResourceException;;

import hu.futureofmedia.task.contactsapi.dto.ContactDTO;
import hu.futureofmedia.task.contactsapi.dto.ContactInput;
import hu.futureofmedia.task.contactsapi.dto.DetailedContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.exceptions.ResourceNotFoundException;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class ContactServiceTest {

  @MockBean
  private ContactRepository contactRepository;

  @Autowired
  private ContactService contactService;

  @Test
  @DisplayName("page of contacts returns DTOS test")
  public void getPageOfDTOsTest() {
    Company company = new Company();
    Contact contact1 = new Contact(1,
        "John Doe",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        company,
        "comment",
        Contact.statuses.active,
        LocalDateTime.now(),
        LocalDateTime.now());
    Contact contact2 = new Contact(2,
        "Jane Doe",
        "Janes company",
        "aenyaino@gmailco.ml",
        "+3653987356",
        company,
        "comment",
        Contact.statuses.active,
        LocalDateTime.now(),
        LocalDateTime.now());
    List<Contact> list = new ArrayList<>();
    list.add(contact1);
    list.add(contact2);
    Page<Contact> page = new PageImpl<>(list);
    ContactDTO contactDTO1 = new ContactDTO(1,
        "John Doe",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356");
    ContactDTO contactDTO2 = new ContactDTO(2,
        "Jane Doe",
        "Janes company",
        "aenyaino@gmailco.ml",
        "+3653987356");
    List<ContactDTO> dtoList = new ArrayList<>();
    dtoList.add(contactDTO1);
    dtoList.add(contactDTO2);
    Page<ContactDTO> expectedPage = new PageImpl<>(dtoList);

    when(contactRepository.findAllSortByStatusOrderByLastNameAscFirstNameAsc(
        Mockito.any(Contact.statuses.class), Mockito.any(
            Pageable.class))).thenReturn(page);

    Page<ContactDTO> actualPage = contactService.getPageOfClients(1);
    assertEquals(actualPage.getContent().get(1).getId(), expectedPage.getContent().get(1).getId());
    verify(contactRepository, times(1)).findAllSortByStatusOrderByLastNameAscFirstNameAsc(
        Mockito.any(Contact.statuses.class), Mockito.any(
            Pageable.class));
  }

  @Test
  @DisplayName("getting details by id test")
  public void getDetailByIdTest() {
    Company company = new Company();
    DetailedContactDTO detailedContactDTO = new DetailedContactDTO("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        "comment",
        LocalDateTime.now(),
        LocalDateTime.now());
    Contact contact1 = new Contact(1,
        "John Doe",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        company,
        "comment",
        Contact.statuses.active,
        LocalDateTime.now(),
        LocalDateTime.now());
    when(contactRepository.existsById(1)).thenReturn(true);
    when(contactRepository.getById(1)).thenReturn(contact1);
    DetailedContactDTO actualDTO = contactService.findById(1);
    assertEquals(detailedContactDTO.getCreated(), actualDTO.getCreated());
    verify(contactRepository, times(1)).getById(1);
    verify(contactRepository, times(1)).existsById(1);
  }

  @Test
  @DisplayName("exception when id invalid test")
  public void exceptionWhenBadIdTest() {
    when(contactRepository.existsById(1)).thenReturn(false);
    Exception exception =
        Assertions.assertThrows(ResourceNotFoundException.class, () -> contactService.findById(1));
    assertEquals("No contact with this id", exception.getMessage());
    verify(contactRepository, times(1)).existsById(1);
  }

  @Test
  @DisplayName("create contact test")
  public void createContactTest() throws Exception{
    ContactInput contactInput = new ContactInput("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        "comment");
    Company company = new Company();
    Contact contact1 = new Contact(1,
        "John Doe",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        company,
        "comment",
        Contact.statuses.active,
        LocalDateTime.now(),
        LocalDateTime.now());
    when(contactRepository.save(Mockito.any(Contact.class))).thenReturn(contact1);
    when(contactRepository.existsById(1)).thenReturn(true);
    when(contactRepository.findById(1)).thenReturn(Optional.of(contact1));

    Contact actualContact = contactService.createContact( contactInput);
    assertEquals(contact1, actualContact);
    verify(contactRepository, times(1)).save(Mockito.any(Contact.class));
  }
  @Test
  @DisplayName("create throws exception when emali invalid")
  public void createInvalidEmailTest() {
    ContactInput contactInput = new ContactInput("Doe",
        "John",
        "Johns company",
        "aenyaino@gma",
        "+3672987356",
        "comment");
    Exception exception =
        Assertions.assertThrows(BadResourceException.class, () -> contactService.createContact(contactInput));
    assertEquals("Invalid e-mail", exception.getMessage());
  }
  @Test
  @DisplayName("create throws exception when phone invalid")
  public void createInvalidPhoneTest() {
    ContactInput contactInput = new ContactInput("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987",
        "comment");
    Exception exception =
        Assertions.assertThrows(BadResourceException.class, () -> contactService.createContact(contactInput));
    assertEquals("Invalid phone number", exception.getMessage());
  }

  @Test
  @DisplayName("update contact test")
  public void updateContactTest() throws Exception{
    ContactInput contactInput = new ContactInput("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        "comment");
    Company company = new Company();
    Contact contact1 = new Contact(1,
        "John Doe",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        company,
        "comment",
        Contact.statuses.active,
        LocalDateTime.now(),
        LocalDateTime.now());
    when(contactRepository.save(Mockito.any(Contact.class))).thenReturn(contact1);
    when(contactRepository.existsById(1)).thenReturn(true);
    when(contactRepository.findById(1)).thenReturn(Optional.of(contact1));

    Contact actualContact = contactService.updateContact(1, contactInput);
    assertEquals(contact1, actualContact);
    verify(contactRepository, times(1)).save(Mockito.any(Contact.class));
    verify(contactRepository, times(1)).existsById(1);
    verify(contactRepository, times(1)).findById(1);
  }

  @Test
  @DisplayName("update when id does not exist")
  public void updateWhenBadIdTest() {
    ContactInput contactInput = new ContactInput("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        "comment");
    when(contactRepository.existsById(1)).thenReturn(false);
    Exception exception =
        Assertions.assertThrows(ResourceNotFoundException.class, () -> contactService.updateContact(1, contactInput));
    assertEquals("No contact with given ID", exception.getMessage());
  }
  @Test
  @DisplayName("update throws exception when emali invalid")
  public void updateInvalidEmailTest() {
    ContactInput contactInput = new ContactInput("Doe",
        "John",
        "Johns company",
        "aenyaino@gma",
        "+3672987356",
        "comment");
    when(contactRepository.existsById(1)).thenReturn(true);
    Exception exception =
        Assertions.assertThrows(BadResourceException.class, () -> contactService.updateContact(1, contactInput));
    assertEquals("Invalid e-mail", exception.getMessage());
  }
  @Test
  @DisplayName("update throws exception when phone invalid")
  public void updateInvalidPhoneTest() {
    ContactInput contactInput = new ContactInput("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987",
        "comment");
    when(contactRepository.existsById(1)).thenReturn(true);
    Exception exception =
        Assertions.assertThrows(BadResourceException.class, () -> contactService.updateContact(1, contactInput));
    assertEquals("Invalid phone number", exception.getMessage());
  }

  @Test
  @DisplayName("deletion test")
  public void deletionTest() {
    Company company = new Company();
    DetailedContactDTO detailedContactDTO = new DetailedContactDTO("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        "comment",
        LocalDateTime.now(),
        LocalDateTime.now());
    Contact contact1 = new Contact(1,
        "John Doe",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        company,
        "comment",
        Contact.statuses.active,
        LocalDateTime.now(),
        LocalDateTime.now());
    when(contactRepository.existsById(1)).thenReturn(true);
    when(contactRepository.save(Mockito.any(Contact.class))).thenReturn(contact1);
    when(contactRepository.getById(1)).thenReturn(contact1);
    DetailedContactDTO actualDTO = contactService.deleteContact(1);
    assertEquals(detailedContactDTO.getCreated(), actualDTO.getCreated());
    verify(contactRepository, times(1)).existsById(1);
    verify(contactRepository, times(1)).getById(1);
    verify(contactRepository, times(1)).save(Mockito.any(Contact.class));
  }

  @Test
  @DisplayName("deletion with bad id test")
  public void deletionWithBadIdTest() {
    when(contactRepository.existsById(1)).thenReturn(false);
    Exception exception =
        Assertions.assertThrows(ResourceNotFoundException.class, () -> contactService.deleteContact(1));
    assertEquals("No client with given id", exception.getMessage());
  }
}
