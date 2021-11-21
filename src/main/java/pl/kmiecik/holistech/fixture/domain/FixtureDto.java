package pl.kmiecik.holistech.fixture.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9]+[:][a-zA-Z0-9]+", message = "Only letters and numbers are allowed in 'owner:description' pattern")
    private String descriptionOfChange;

}
