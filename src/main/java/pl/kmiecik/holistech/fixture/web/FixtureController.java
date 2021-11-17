package pl.kmiecik.holistech.fixture.web;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.kmiecik.holistech.fixture.application.port.FixtureService;
import pl.kmiecik.holistech.fixture.domain.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SameReturnValue")
@Controller
class FixtureController {

    private final FixtureService service;

    @Autowired
    public FixtureController(FixtureService service) {
        this.service = service;
    }

    @GetMapping("/fixtures")
    public String getAllFixtures(Model model) {
        List<Fixture> allFixturesList = service.findAllFixtures();
        model.addAttribute("allFixtures", allFixturesList);
        model.addAttribute("newFixture", new FixtureDto());
        return "mainFixturesView";
    }

    @GetMapping("/fixture/{id}")
    public String getFixtureById(@PathVariable Long id, Model model) {
        List<FixtureHistory> fixtureHistoryList;
        Fixture fixture;
        Optional<Fixture> fixtureById = service.findFixtureById(id);
        if (fixtureById.isPresent()) {
            fixture = fixtureById.get();
            fixtureHistoryList = fixture.getFixtureHistories();
        } else {
            fixtureHistoryList = Collections.emptyList();
            fixture = new Fixture();
        }
        model.addAttribute("fixtureName", fixture.getName());
        model.addAttribute("allFixtures", fixtureHistoryList);

        return "historyFixturesView";
    }

    //************
    @GetMapping("/addFixture")
    public String addFixtureGET(Model model) {
        model.addAttribute("newFixture", new FixtureDto());
        return "addFixtureView";
    }

    @PostMapping("/add-fixtureButton")
    public String addFixtureButton() {
        //noinspection SpringMVCViewInspection
        return "redirect:/addFixture";
    }


    @PostMapping("/addFixture")
    public String addFixturePOST(@Valid @ModelAttribute FixtureCommand command) {
        Fixture fixture= command.toFixture();
        service.setMyDefaultStrainStatus(fixture);
        service.setMyExpiredStrainDate(fixture);
        FixtureHistory fixtureHistory = service.getFixtureHistory(fixture, "INIT", ModificationReason.CREATE);
        service.saveFixture(fixture, fixtureHistory);
        return "redirect:/fixtures";
    }

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

    @Data
    private static class FixtureCommand {

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
