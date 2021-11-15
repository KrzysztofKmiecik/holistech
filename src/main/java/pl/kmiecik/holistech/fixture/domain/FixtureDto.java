package pl.kmiecik.holistech.fixture.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class FixtureDto {

    private Long id;
    private String name;
    private FisProcess fisProcess;
    private Status statusStrain;
    private LocalDate expiredDateStrain;
    private LocalDateTime modificationDateTime;
    private ModificationReason modificationReason;
    private String descriptionOfChange;

}
