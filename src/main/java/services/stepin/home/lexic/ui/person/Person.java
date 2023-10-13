package services.stepin.home.lexic.ui.person;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Nonnull
    private String firstName;

    @Nonnull
    private String lastName;

    @Nonnull
    private String email;

    @Nonnull
    private Date birthday;

    @Nonnull
    private boolean subscriber;

    @Nonnull
    private String membership;

    @Nonnull
    private String pictureUrl;

    @Nonnull
    private String profession;

//    @Nonnull
//    private Address address;

    private Integer managerId;

    @Nonnull
    private boolean manager;

    @Nonnull
    private String status;


    public Person() {
        super();
    }
    public Person(@Nonnull String firstName) {
        this.firstName = firstName;
    }


}