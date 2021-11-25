package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dto.ContactDTO;
import hu.futureofmedia.task.contactsapi.dto.ContactInput;
import hu.futureofmedia.task.contactsapi.dto.DetailedContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.springframework.data.domain.Page;

public interface ContactService {
  Page<ContactDTO> getPageOfClients(int pageNumber);
  DetailedContactDTO findById(Integer id);
  Contact createContact(ContactInput input) throws Exception;
  Contact updateContact(Integer id, ContactInput input) throws Exception;
  DetailedContactDTO deleteContact(Integer id);
  DetailedContactDTO convertToDetailedDTO(Contact contact);
}
