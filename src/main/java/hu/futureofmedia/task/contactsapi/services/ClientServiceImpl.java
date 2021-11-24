package hu.futureofmedia.task.contactsapi.services;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import hu.futureofmedia.task.contactsapi.dto.ClientDTO;
import hu.futureofmedia.task.contactsapi.dto.DetailedClientDTO;
import hu.futureofmedia.task.contactsapi.entities.Client;
import hu.futureofmedia.task.contactsapi.exceptions.BadResourceException;
import hu.futureofmedia.task.contactsapi.exceptions.ResourceNotFoundException;
import hu.futureofmedia.task.contactsapi.repositories.ClientRepository;
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
public class ClientServiceImpl implements ClientService {

  @Autowired
  private ClientRepository clientRepository;

  public Page<ClientDTO> getPageOfClients(int pageNumber) {
    int offset = (pageNumber - 1) * 10 - 1;
    Pageable pageable = PageRequest.of(offset, 10);
    Page<Client> clientPage =
        clientRepository.findAllByStatusActiveOrderByLastNameDescFirstNameDesc(pageable);
    return clientPage.map(this::convertToDTO);
  }

  public DetailedClientDTO findById(Integer id) {
    Objects.requireNonNull(id);
    return convertToDetailedDTO(clientRepository.getById(id));
  }

  @Transactional
  public DetailedClientDTO createClient(Client client) throws Exception {
    Objects.requireNonNull(client);
    if (!eMaliIsValid(client.getEMail())) {
      throw new BadResourceException("Invalid e-mail");
    }
    if (!client.getPhoneNumber().isEmpty() && !phoneIsValid(client.getPhoneNumber())) {
      throw new BadResourceException("Invalid phone number");
    } else {
      Client savedClient = clientRepository.save(client);
      return convertToDetailedDTO(savedClient);
    }
  }

  @Transactional
  public DetailedClientDTO updateClient(Client client) throws Exception {
    Objects.requireNonNull(client);
    if (clientRepository.existsById(client.getId())) {
      if (!eMaliIsValid(client.getEMail())) {
        throw new BadResourceException("Invalid e-mail");
      }
      if (!client.getPhoneNumber().isEmpty() && !phoneIsValid(client.getPhoneNumber())) {
        throw new BadResourceException("Invalid phone number");
      } else {
        client.setLastChanged(LocalDateTime.now());
        Client savedClient = clientRepository.save(client);
        return convertToDetailedDTO(savedClient);
      }
    } else {
      throw new ResourceNotFoundException("No client with given ID");
    }
  }

  @Transactional
  public DetailedClientDTO deleteClient(Integer id) {
    Objects.requireNonNull(id);
    if (clientRepository.existsById(id)) {
      Client client = clientRepository.getById(id);
      client.setStatus(Client.statuses.deleted);
      Client savedClient = clientRepository.save(client);
      return convertToDetailedDTO(savedClient);
    } else {
      throw new ResourceNotFoundException("No client with given id");
    }
  }

  private ClientDTO convertToDTO(Client client) {
    return new ClientDTO(client.getId(),
        client.getLastName() + " " + client.getLastName(),
        client.getCompany().getName(),
        client.getEMail(),
        client.getPhoneNumber());
  }

  private DetailedClientDTO convertToDetailedDTO(Client client) {
    return new DetailedClientDTO(client.getLastName(),
        client.getFirstName(),
        client.getCompany().getName(),
        client.getEMail(),
        client.getPhoneNumber(),
        client.getComment(),
        client.getCreated(),
        client.getLastChanged());
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
}
