package pl.kmiecik.holistech.fixture.web;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kmiecik.holistech.fixture.application.port.FixtureService;
import pl.kmiecik.holistech.fixture.domain.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static pl.kmiecik.holistech.fixture.application.port.FixtureService.FixtureResponse;

@RestController
@RequestMapping("/api/fixtures")
class FixtureRestController {

    private final FixtureService service;

    @Value("${spring.profiles.active")
    private String activeProfile;

    @Autowired
    public FixtureRestController(FixtureService service) {
        this.service = service;
    }

    @GetMapping
    public List<Fixture> getAllFixtures() {
        return service.findAllFixtures();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fixture> getFixtureById(@PathVariable Long id) {
        return service.findFixtureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * BODY
     * {
     * "name": "fixt3",
     * "fisProcess": "ICT"
     * }
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addFixture(@Valid @RequestBody RestFixtureCommand command) {
        Fixture fixture = command.toFixture();
        service.setMyDefaultStrainStatus(fixture);
        service.setMyExpiredStrainDate(fixture);
        FixtureHistory fixtureHistory = service.createFixtureHistory(fixture, "INIT", ModificationReason.CREATE);
        service.saveFixture(fixture, fixtureHistory);
        URI fixtureUri = createFixtureUri(fixture);
        return ResponseEntity.created(fixtureUri).build();
    }

    private URI createFixtureUri(Fixture fixture) {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/" + fixture.getId().toString())
                .build()
                .toUri();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFixture(@PathVariable Long id) {
        service.deleteFixture(id);
    }


    /**
     * BODY
     * {
     * "name": "fixt3",
     * "fisProcess": "ICT"
     * }
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateFixture(@Valid @RequestBody RestFixtureCommand command, @PathVariable Long id) {
        Fixture fixture = command.toFixture();
        Optional<Fixture> fixtureById = service.findFixtureById(id);
        if (fixtureById.isPresent()) {

            Fixture fixtureToUpdate = fixtureById.get();
            mapUpdateFixture(fixture, fixtureToUpdate);

            FixtureResponse fixtureResponse = service.updateFixture(fixtureToUpdate);
            if (!fixtureResponse.isSuccess()) {
                String message = String.join(", ", fixtureResponse.getErrors());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
            } else {
                FixtureHistory fixtureHistory = service.createFixtureHistory(fixtureById.get(), String.format("%s , %s", fixtureResponse.getMessages().get(0), fixtureResponse.getMessages().get(1)), ModificationReason.EDIT);
                service.saveFixture(fixtureById.get(), fixtureHistory);
            }
        }

    }

    private void mapUpdateFixture(Fixture newFixtureData, Fixture fixtureToUpdate) {

        if (newFixtureData.getName() != null) {
            fixtureToUpdate.setName(newFixtureData.getName());
        }
        if (newFixtureData.getFisProcess() != null) {
            fixtureToUpdate.setFisProcess(newFixtureData.getFisProcess());
        }

    }

    /**
     * BODY
     * {
     * "descriptionOfChange": "my changeDescription"
     * }
     */

    @PutMapping("/{id}/ok")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setOKFixture(@Valid @RequestBody FixtureDto fixtureDto, @PathVariable String id) {
        Fixture fixture = service.setStrainStatus(id, Status.OK);
        FixtureHistory fixtureHistory = service.createFixtureHistory(fixture, fixtureDto.getDescriptionOfChange(), ModificationReason.SET_OK);
        service.saveFixture(fixture, fixtureHistory);
        if (activeProfile.equals("prod")) service.sendEmail(fixture);
    }


    @PutMapping("/{id}/nok")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setNOKFixture(@Valid @RequestBody FixtureDto fixtureDto, @PathVariable String id) {
        Fixture fixture = service.setStrainStatus(id, Status.NOK);
        FixtureHistory fixtureHistory = service.createFixtureHistory(fixture, fixtureDto.getDescriptionOfChange(), ModificationReason.SET_NOK);
        service.saveFixture(fixture, fixtureHistory);
        if (activeProfile.equals("prod")) service.sendEmail(fixture);
    }

    @Data
    @Builder
    private static class RestFixtureCommand {

        private Long id;
        @NotBlank
        private String name;
        @NotNull
        private FisProcess fisProcess;
        private Status statusStrain;
        private LocalDate expiredDateStrain;
        private List<FixtureHistory> fixtureHistories;

        public Fixture toFixture() {
            return new Fixture(this.getId(), this.getName(), this.getFisProcess(), this.getStatusStrain(), this.getExpiredDateStrain(), this.getFixtureHistories());
        }

    }


}
