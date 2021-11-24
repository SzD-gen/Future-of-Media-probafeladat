package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dto.ClientDTO;
import hu.futureofmedia.task.contactsapi.dto.DetailedClientDTO;
import hu.futureofmedia.task.contactsapi.entities.Client;
import org.springframework.data.domain.Page;

public interface ClientService {
  Page<ClientDTO> getPageOfClients(int pageNumber);
  DetailedClientDTO findById(Integer id);
  DetailedClientDTO createClient(Client client) throws Exception;
  DetailedClientDTO updateClient(Client client) throws Exception;
  DetailedClientDTO deleteClient(Integer id);
}
