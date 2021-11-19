package pl.kmiecik.holistech.fixture.web;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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

import static pl.kmiecik.holistech.fixture.application.port.FixtureService.*;

@RestController
@RequestMapping("/api/fixtures")
class FixtureRestController {

    private final FixtureService service;

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
                .map(fixture -> ResponseEntity.ok(fixture))
                .orElse(ResponseEntity.notFound().build());
    }


    /**
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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateFixture(@PathVariable Long id, @RequestBody RestFixtureCommand command) {
        Optional<Fixture> fixtureById = service.findFixtureById(id);
        if (fixtureById.isPresent()) {
               FixtureResponse fixtureResponse = service.updateFixture(command.toUpdateFixtureCommand());
            if (!fixtureResponse.isSuccess()) {
                String message = String.join(", ", fixtureResponse.getErrors());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
            } else {
                FixtureHistory fixtureHistory = service.createFixtureHistory(fixtureById.get(), String.format("%s , %s", fixtureResponse.getMessages().get(0), fixtureResponse.getMessages().get(1)), ModificationReason.EDIT);
                service.saveFixture(fixtureById.get(), fixtureHistory);
            }
        }

    }

    /*
        @PostMapping
        public String addFixture(@Valid @RequestBody RestFixtureCommand command) {
            Fixture fixture = command.toFixture();
            service.setMyDefaultStrainStatus(fixture);
            service.setMyExpiredStrainDate(fixture);
            FixtureHistory fixtureHistory = service.getFixtureHistory(fixture, "INIT", ModificationReason.CREATE);
            service.saveFixture(fixture, fixtureHistory);
            retu "redirect:/fixtures";
        }

    /*
        //***********
        @PostMapping("/edit-fixtureButton")
        public String editFixtureButton(@RequestParam String id) {
            return "redirect:/editFixture/" + id;
        }

        @GetMapping("/editFixture/{id}")
        public String editFixtureGET(Model model, @PathVariable String id) {
            Fixture fixtureToEdit = service.findFixtureById(Long.valueOf(id)).orElse(new Fixture());
            model.addAttribute("fixtureToEdit", fixtureToEdit);
            return "editFixtureView";
        }

        @PostMapping("/editFixture")
        public String editFixturePOST(@Valid @ModelAttribute FixtureCommand command) {

            FisProcess fisProcess;
            Fixture fixture = command.toFixture();
            Optional<Fixture> fixtureById = service.findFixtureById(fixture.getId());
            String messageName = "", messageFis = "";
            if (fixtureById.isPresent()) {
                Fixture fixtureOld = fixtureById.get();
                String name = fixtureOld.getName();
                fisProcess = fixtureOld.getFisProcess();
                if (!name.equals(fixture.getName())) {
                    messageName = String.format("name was change from  %s to %s", name, fixture.getName());
                }
                if (!fisProcess.name().equals(fixture.getFisProcess().name())) {
                    messageFis = String.format("Fis_Process was change from  %s to %s", fisProcess.name(), fixture.getFisProcess().name());
                }
            }

            FixtureHistory fixtureHistory = service.getFixtureHistory(fixture, String.format("%s , %s", messageName, messageFis), ModificationReason.EDIT);
            service.saveFixture(fixture, fixtureHistory);
            return "redirect:/fixtures";
        }
        //***********

        @PostMapping("/setOK-fixtureButton")
        public String setOKPost(@RequestParam String id, @ModelAttribute FixtureDto fixtureDto) {
            Fixture fixture = service.setStrainStatus(id, Status.OK);
            FixtureHistory fixtureHistory = service.getFixtureHistory(fixture, fixtureDto.getDescriptionOfChange(), ModificationReason.SET_OK);
            service.saveFixture(fixture, fixtureHistory);
            service.sendEmail(fixture);
            return "redirect:/fixtures";
        }


        //***********
        @PostMapping("/setNOK-fixtureButton")
        public String setNOKPost(@RequestParam String id, @ModelAttribute FixtureDto fixtureDto) {
            Fixture fixture = service.setStrainStatus(id, Status.NOK);
            FixtureHistory fixtureHistory = service.getFixtureHistory(fixture, fixtureDto.getDescriptionOfChange(), ModificationReason.SET_NOK);
            service.saveFixture(fixture, fixtureHistory);
            service.sendEmail(fixture);
            return "redirect:/fixtures";
        }

        //***********
        @PostMapping("/delete-fixtureButton")
        public String deleteFixture(@RequestParam String id) {
            service.deleteFixture(Long.valueOf(id));
            return "redirect:/fixtures";
        }
    */
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

        CreateFixtureCommand toCreateFixtureCommand() {
            return new CreateFixtureCommand(id, name, fisProcess, statusStrain, expiredDateStrain, fixtureHistories);
        }
        UpdateFixtureCommand toUpdateFixtureCommand() {
            return new UpdateFixtureCommand(id, name, fisProcess, statusStrain, expiredDateStrain, fixtureHistories);
        }
    }

}
