package hu.futureofmedia.task.contactsapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact")
public class Contact {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  @Column(name = "last_name")
  private String lastName;

  @NotBlank
  @Column(name = "first_name")
  private String firstName;

  @NotBlank
  @Column(name = "e_mail")
  private String eMail;

  @Column(name = "phone_number")
  @NotNull
  private String phoneNumber;

  @NotNull
  @ManyToOne
  @JsonIgnoreProperties({"contacts"})
  @JoinColumn(name = "company_id")
  private Company company;

  private String comment;

  @Enumerated(EnumType.STRING)
  private statuses status = statuses.active;

  public enum statuses {
    active,
    deleted
  }

  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime created = LocalDateTime.now();

  @Column(name = "last_changed", columnDefinition = "TIMESTAMP")
  private LocalDateTime lastChanged = LocalDateTime.now();
}
