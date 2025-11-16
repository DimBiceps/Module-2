package module25.userservice.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, length=100) private String name;
  @Column(nullable=false, length=250) private String email;
  private Integer age;

  @Column(name="created_at", nullable=false)
  private OffsetDateTime createdAt;


      public User() {
    }

    public User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = OffsetDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null){
            this.createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdAt=" + createdAt + '}';
    }
}
