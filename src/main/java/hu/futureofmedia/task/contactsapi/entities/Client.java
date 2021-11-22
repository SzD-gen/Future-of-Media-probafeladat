package hu.futureofmedia.task.contactsapi.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  private String lastName;

  @NotBlank
  private String firstName;

  @NotBlank
  private String eMail;

  private String phoneNumber;

  @NotBlank
  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  private String comment;

  @NotBlank
  private status status;

  private enum status {
    active,
    deleted
  }
}
