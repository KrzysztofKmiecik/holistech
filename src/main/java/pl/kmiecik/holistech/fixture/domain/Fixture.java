package pl.kmiecik.holistech.fixture.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
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
    private List<FixtureHistory> fixtureHistories;

    public void addHistory(FixtureHistory fixtureHistory){
        fixtureHistories.add(fixtureHistory);
        if(fixtureHistory.getFixture()!=this){
            fixtureHistory.setFixture(this);
        }
    }

}
