package hu.futureofmedia.task.contactsapi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Company {
  @Id
  private Long id;
  private String name;

  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Client> clients = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
