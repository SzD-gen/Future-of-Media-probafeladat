package hu.futureofmedia.task.contactsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ContactInput {
  private String lastName;
  private String firstName;
  private String companyName;
  private String eMail;
  private String phoneNumber;
  private String comment;
}
