package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.dto.ContactDTO;
import hu.futureofmedia.task.contactsapi.dto.ContactInput;
import hu.futureofmedia.task.contactsapi.dto.DetailedContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ContactController {

  @Autowired
  ContactService contactService;

  @GetMapping("/contacts")
  public ResponseEntity<List<ContactDTO>> activeClientPages(
      @RequestParam(name = "page", required = false) Integer page) {
    if (page == null) {
      page = 1;
    }
    return new ResponseEntity<>(contactService.getPageOfClients(page).getContent(), HttpStatus.OK);
  }

  @GetMapping("/contacts/{id}")
  public ResponseEntity<DetailedContactDTO> contactDetails(@PathVariable Integer id) {
    return new ResponseEntity<>(contactService.findById(id), HttpStatus.OK);
  }

  @PostMapping("/contacts")
  public ResponseEntity<DetailedContactDTO> createContact(@Valid @RequestBody ContactInput input)
      throws Exception {
    Contact savedContact = contactService.createContact(input);
    return ResponseEntity.created(new URI("/api/contacts/" + savedContact.getId()))
        .body(contactService.convertToDetailedDTO(savedContact));
  }

  @PutMapping("/contacts/{id}")
  public ResponseEntity<DetailedContactDTO> updateContact(@PathVariable Integer id,
                                                          @Valid @RequestBody ContactInput input)
      throws Exception {
    return new ResponseEntity<>(
        contactService.convertToDetailedDTO(contactService.updateContact(id, input)),
        HttpStatus.OK);
  }

  @DeleteMapping("contacts/{id}")
  public ResponseEntity<DetailedContactDTO> deleteContact(@PathVariable Integer id){
    return new ResponseEntity<>(contactService.deleteContact(id), HttpStatus.NO_CONTENT) ;
  }
}
