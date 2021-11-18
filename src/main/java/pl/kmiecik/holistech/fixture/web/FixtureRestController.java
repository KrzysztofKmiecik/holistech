package pl.kmiecik.holistech.fixture.web;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kmiecik.holistech.fixture.application.port.FixtureService;
import pl.kmiecik.holistech.fixture.domain.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

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
    }

}
