package hu.futureofmedia.task.contactsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ClientDTO {
  private Integer id;
  private String fullName;
  private String companyName;
  private String eMail;
  private String phoneNumber;
}
