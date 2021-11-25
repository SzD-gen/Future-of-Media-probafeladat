package hu.futureofmedia.task.contactsapi.apiTests;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.futureofmedia.task.contactsapi.dto.ContactDTO;
import hu.futureofmedia.task.contactsapi.dto.ContactInput;
import hu.futureofmedia.task.contactsapi.dto.DetailedContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactsApiTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ContactService contactService;

  @Test
  @DisplayName("/contacts returns list")
  public void getContactsReturns() throws Exception {
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
    List<ContactDTO> list = new ArrayList<>();
    list.add(contactDTO1);
    list.add(contactDTO2);
    Page<ContactDTO> page = new PageImpl<>(list);
    Mockito.when(contactService.getPageOfClients(1)).thenReturn(page);

    mockMvc.perform(get("/api/contacts").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].fullName", is("John Doe")))
        .andExpect(jsonPath("$[0].companyName", is("Johns company")))
        .andExpect(jsonPath("$[0].email", is("aenyaino@gmailco.ml")))
        .andExpect(jsonPath("$[0].phoneNumber", is("+3672987356")))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].fullName", is("Jane Doe")))
        .andExpect(jsonPath("$[1].companyName", is("Janes company")))
        .andExpect(jsonPath("$[1].email", is("aenyaino@gmailco.ml")))
        .andExpect(jsonPath("$[1].phoneNumber", is("+3653987356")));

  }

  @Test
  @DisplayName("get contact/id returns Details")
  public void getsDetails() throws Exception {
    DetailedContactDTO detailedContactDTO = new DetailedContactDTO("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        "comment",
        LocalDateTime.now(),
        LocalDateTime.now());
    Mockito.when(contactService.findById(1)).thenReturn(detailedContactDTO);

    mockMvc.perform(get("/api/contacts/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName", is("Doe")))
        .andExpect(jsonPath("$.firstName", is("John")))
        .andExpect(jsonPath("$.companyName", is("Johns company")))
        .andExpect(jsonPath("$.email", is("aenyaino@gmailco.ml")))
        .andExpect(jsonPath("$.phoneNumber", is("+3672987356")))
        .andExpect(jsonPath("$.comment", is("comment")));
  }

  @Test
  @DisplayName("post contact returns new contact")
  public void postReturnsDetails() throws Exception {
    DetailedContactDTO detailedContactDTO = new DetailedContactDTO("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        "comment",
        LocalDateTime.now(),
        LocalDateTime.now());
    Contact contact = new Contact();
    contact.setId(1);
    Mockito.when(contactService.convertToDetailedDTO(Mockito.any(Contact.class)))
        .thenReturn(detailedContactDTO);
    Mockito.when(contactService.createContact(Mockito.any(ContactInput.class))).thenReturn(contact);
    mockMvc.perform(post("/api/contacts").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(contact)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName", is("Doe")))
        .andExpect(jsonPath("$.firstName", is("John")))
        .andExpect(jsonPath("$.companyName", is("Johns company")))
        .andExpect(jsonPath("$.email", is("aenyaino@gmailco.ml")))
        .andExpect(jsonPath("$.phoneNumber", is("+3672987356")))
        .andExpect(jsonPath("$.comment", is("comment")));
  }

  @Test
  @DisplayName("put returns details")
  public void putReturnsDetails() throws Exception{
    DetailedContactDTO detailedContactDTO = new DetailedContactDTO("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        "comment",
        LocalDateTime.now(),
        LocalDateTime.now());
    Contact contact = new Contact();
    contact.setId(1);
    Mockito.when(contactService.convertToDetailedDTO(Mockito.any(Contact.class)))
        .thenReturn(detailedContactDTO);
    Mockito.when(contactService.updateContact(Mockito.any(Integer.class), Mockito.any(ContactInput.class))).thenReturn(contact);
    mockMvc.perform(put("/api/contacts/1").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(contact)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName", is("Doe")))
        .andExpect(jsonPath("$.firstName", is("John")))
        .andExpect(jsonPath("$.companyName", is("Johns company")))
        .andExpect(jsonPath("$.email", is("aenyaino@gmailco.ml")))
        .andExpect(jsonPath("$.phoneNumber", is("+3672987356")))
        .andExpect(jsonPath("$.comment", is("comment")));
  }

  @Test
  @DisplayName("delete contact returns with no content")
  public void deleteNoContent() throws Exception{
    DetailedContactDTO detailedContactDTO = new DetailedContactDTO("Doe",
        "John",
        "Johns company",
        "aenyaino@gmailco.ml",
        "+3672987356",
        "comment",
        LocalDateTime.now(),
        LocalDateTime.now());
    Mockito.when(contactService.deleteContact(1)).thenReturn(detailedContactDTO);
    mockMvc.perform(delete("/api/contacts/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName", is("Doe")))
        .andExpect(jsonPath("$.firstName", is("John")))
        .andExpect(jsonPath("$.companyName", is("Johns company")))
        .andExpect(jsonPath("$.email", is("aenyaino@gmailco.ml")))
        .andExpect(jsonPath("$.phoneNumber", is("+3672987356")))
        .andExpect(jsonPath("$.comment", is("comment")));
  }

  public static String asJsonString(final Object obj) {
    ObjectMapper objectMapper = new ObjectMapper();
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    objectMapper.registerModule(javaTimeModule);
    try {
      return  objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
