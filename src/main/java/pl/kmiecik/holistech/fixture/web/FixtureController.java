package pl.kmiecik.holistech.fixture.web;

import lombok.Builder;
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
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        return "redirect:/addFixture";
    }


    @PostMapping("/addFixture")
    public String addFixturePOST(@Valid @ModelAttribute FixtureCommand command) {
        Fixture fixture = command.toFixture();
        service.addFixture(fixture);
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
        Fixture fixture = command.toFixture();
        service.updateFixture(fixture.getId(), fixture);
        return "redirect:/fixtures";
    }


    //***********

    @PostMapping("/setOK-fixtureButton")
    public String setOKPost(@RequestParam String id, @Valid @ModelAttribute FixtureDto fixtureDto) {
        Fixture fixture = service.setStrainStatus(id, Status.OK);
        FixtureHistory fixtureHistory = service.createFixtureHistory(fixture, fixtureDto.getDescriptionOfChange(), ModificationReason.SET_OK);
        service.addFixtureHistory(fixture, fixtureHistory);
        service.sendEmail(fixture);
        return "redirect:/fixtures";
    }


    //***********
    @PostMapping("/setNOK-fixtureButton")
    public String setNOKPost(@RequestParam String id, @Valid @ModelAttribute FixtureDto fixtureDto) {
        Fixture fixture = service.setStrainStatus(id, Status.NOK);
        FixtureHistory fixtureHistory = service.createFixtureHistory(fixture, fixtureDto.getDescriptionOfChange(), ModificationReason.SET_NOK);
        service.addFixtureHistory(fixture, fixtureHistory);
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
    @Builder
    private static class FixtureCommand {
        private Long id;
        @NotBlank
        @Pattern(regexp = "[a-zA-Z0-9]+",message = "Only letters and numbers are allowed")
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
