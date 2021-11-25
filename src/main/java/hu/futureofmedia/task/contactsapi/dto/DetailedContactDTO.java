package hu.futureofmedia.task.contactsapi.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DetailedContactDTO {
  private String lastName;
  private String firstName;
  private String companyName;
  private String eMail;
  private String phoneNumber;
  private String comment;
  private LocalDateTime created;
  private LocalDateTime lastChanged;
}
