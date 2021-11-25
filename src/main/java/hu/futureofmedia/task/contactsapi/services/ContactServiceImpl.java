package hu.futureofmedia.task.contactsapi.services;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import hu.futureofmedia.task.contactsapi.dto.ContactDTO;
import hu.futureofmedia.task.contactsapi.dto.ContactInput;
import hu.futureofmedia.task.contactsapi.dto.DetailedContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.exceptions.BadResourceException;
import hu.futureofmedia.task.contactsapi.exceptions.ResourceNotFoundException;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.transaction.Transactional;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private CompanyRepository companyRepository;

  public Page<ContactDTO> getPageOfClients(int pageNumber) {

    Pageable pageable = PageRequest.of(pageNumber - 1, 10);
    Page<Contact> clientPage =
        contactRepository.findAllSortByStatusOrderByLastNameAscFirstNameAsc(Contact.statuses.active,
            pageable);
    return clientPage.map(this::convertToDTO);
  }

  public DetailedContactDTO findById(Integer id) {
    Objects.requireNonNull(id);
    if (!contactRepository.existsById(id)) {
      throw new ResourceNotFoundException("No contact with this id");
    }
    return convertToDetailedDTO(contactRepository.getById(id));
  }

  @Transactional
  public Contact createContact(ContactInput input) throws Exception {
    Objects.requireNonNull(input);
    if (!eMaliIsValid(input.getEMail())) {
      throw new BadResourceException("Invalid e-mail");
    }
    if (!input.getPhoneNumber().isEmpty() && !phoneIsValid(input.getPhoneNumber())) {
      throw new BadResourceException("Invalid phone number");
    } else {
      Contact contact = inputToEntity(input);
      Contact savedContact = contactRepository.save(contact);
      return savedContact;
    }
  }

  @Transactional
  public Contact updateContact(Integer id, ContactInput input) throws Exception {
    Objects.requireNonNull(input);
    Objects.requireNonNull(id);
    if (contactRepository.existsById(id)) {
      if (!eMaliIsValid(input.getEMail())) {
        throw new BadResourceException("Invalid e-mail");
      }
      if (!input.getPhoneNumber().isEmpty() && !phoneIsValid(input.getPhoneNumber())) {
        throw new BadResourceException("Invalid phone number");
      } else {
        Contact savedContact = inputToEntity(input);
        LocalDateTime created = contactRepository.findById(id).get().getCreated();
        savedContact.setLastChanged(LocalDateTime.now());
        savedContact.setCreated(created);
        savedContact = contactRepository.save(savedContact);
        return savedContact;
      }
    } else {
      throw new ResourceNotFoundException("No contact with given ID");
    }
  }

  @Transactional
  public DetailedContactDTO deleteContact(Integer id) {
    Objects.requireNonNull(id);
    if (contactRepository.existsById(id)) {
      Contact contact = contactRepository.getById(id);
      contact.setStatus(Contact.statuses.deleted);
      Contact savedContact = contactRepository.save(contact);
      return convertToDetailedDTO(savedContact);
    } else {
      throw new ResourceNotFoundException("No client with given id");
    }
  }

  private ContactDTO convertToDTO(Contact contact) {
    return new ContactDTO(contact.getId(),
        contact.getLastName() + " " + contact.getFirstName(),
        contact.getCompany().getName(),
        contact.getEMail(),
        contact.getPhoneNumber());
  }

  public DetailedContactDTO convertToDetailedDTO(Contact contact) {
    return new DetailedContactDTO(contact.getLastName(),
        contact.getFirstName(),
        contact.getCompany().getName(),
        contact.getEMail(),
        contact.getPhoneNumber(),
        contact.getComment(),
        contact.getCreated(),
        contact.getLastChanged());
  }

  private boolean eMaliIsValid(String eMail) {
    return EmailValidator.getInstance().isValid(eMail);
  }

  private boolean phoneIsValid(String phoneNumber) throws Exception {
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    PhoneNumber number =
        phoneNumberUtil.parse(phoneNumber, PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
    return phoneNumberUtil.isValidNumber(number);
  }

  private Contact inputToEntity(ContactInput contactDTO) {
    Contact contact = new Contact();
    contact.setLastName(contactDTO.getLastName());
    contact.setFirstName(contactDTO.getFirstName());
    contact.setCompany(companyRepository.findByName(contactDTO.getCompanyName()));
    contact.setEMail(contactDTO.getEMail());
    contact.setPhoneNumber(contactDTO.getPhoneNumber());
    contact.setComment(contactDTO.getComment());
    return contact;
  }
}
