package pl.kmiecik.holistech.fixture.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fixtures")
public class Fixture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name",unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private FisProcess fisProcess;
    @Column(name = "status_strain")
    @Enumerated(EnumType.STRING)
    private Status statusStrain;
    @Column(name = "expired_date_strain")
    private LocalDate expiredDateStrain;

    @OneToMany(mappedBy = "fixture",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FixtureHistory> fixtureHistories;

}
