package pl.kmiecik.holistech.fixture.web;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kmiecik.holistech.fixture.application.port.FixtureService;
import pl.kmiecik.holistech.fixture.domain.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fixtures")
class FixtureRestController {

    private final FixtureService service;

    @Value("${spring.profiles.active}")
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
        service.addFixture(fixture);
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
        Fixture fixtureDataToUpdate = command.toFixture();
        service.updateFixture(id, fixtureDataToUpdate);
    }


    /**
     * BODY
     * {
     * "descriptionOfChange": "my changeDescription"
     * }
     */

    @PutMapping("/{id}/ok")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setOKFixture(@Valid @RequestBody DescriptionCommand command, @PathVariable String id) {
        Fixture fixture = service.setStrainStatus(id, Status.OK);
        FixtureHistory fixtureHistory = service.createFixtureHistory(fixture, command.getDescriptionOfChange(), ModificationReason.SET_OK);
        service.addFixtureHistory(fixture, fixtureHistory);
        if (activeProfile.equals("prod")) service.sendEmail(fixture);
    }


    @PutMapping("/{id}/nok")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setNOKFixture(@Valid @RequestBody DescriptionCommand command, @PathVariable String id) {
        Fixture fixture = service.setStrainStatus(id, Status.NOK);
        FixtureHistory fixtureHistory = service.createFixtureHistory(fixture, command.getDescriptionOfChange(), ModificationReason.SET_NOK);
        service.addFixtureHistory(fixture, fixtureHistory);
        if (activeProfile.equals("prod")) service.sendEmail(fixture);
    }

    @Data
    @Builder
    private static class RestFixtureCommand {

        @NotEmpty
        @Pattern(regexp = "[a-zA-Z0-9]*",message = "Only letters and numbers are allowed")
        private String name;
        @NotNull
        private FisProcess fisProcess;

        public Fixture toFixture() {
            Fixture fixture = new Fixture();
            fixture.setName(name);
            fixture.setFisProcess(fisProcess);
            return fixture;
        }

    }

    @Data
    private static class DescriptionCommand {
        @NotEmpty
        @Pattern(regexp = "[a-zA-Z0-9]+[:][a-zA-Z0-9]+", message = "Only letters and numbers are allowed in 'owner:description' pattern")
        private String descriptionOfChange;

    }
}
