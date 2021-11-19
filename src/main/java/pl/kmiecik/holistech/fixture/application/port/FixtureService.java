package pl.kmiecik.holistech.fixture.application.port;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import pl.kmiecik.holistech.fixture.domain.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface FixtureService {

    List<Fixture> findAllFixtures();

    void saveFixture(Fixture fixture, FixtureHistory fixtureHistory);

    Fixture setStrainStatus(String id, Status ok);

    void sendEmail(Fixture myFixture);

    FixtureHistory createFixtureHistory(Fixture fixture, String descriptionOfChange, ModificationReason modificationReason);

    void deleteFixture(Long id);

    void setMyDefaultStrainStatus(Fixture fixture);

    void setMyExpiredStrainDate(Fixture fixture);

    Optional<Fixture> findFixtureById(Long valueOf);

    FixtureResponse updateFixture(UpdateFixtureCommand command);

    @Value
    class CreateFixtureCommand {
        Long id;
        String name;
        FisProcess fisProcess;
        Status statusStrain;
        LocalDate expiredDateStrain;
        List<FixtureHistory> fixtureHistories;

        public Fixture toFixture() {
            return new Fixture(id, name, fisProcess, statusStrain, expiredDateStrain, fixtureHistories);
        }

    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateFixtureCommand {
        Long id;
        String name;
        FisProcess fisProcess;
        Status statusStrain;
        LocalDate expiredDateStrain;
        List<FixtureHistory> fixtureHistories;

        public Fixture updateFields(Fixture fixture) {
            if (name != null) {
                fixture.setName(name);
            }
            if (fisProcess != null) {
                fixture.setFisProcess(fisProcess);
            }
            if (statusStrain != null) {
                fixture.setStatusStrain(statusStrain);
            }
            if (expiredDateStrain != null) {
                fixture.setExpiredDateStrain(expiredDateStrain);
            }
            if (fixtureHistories != null) {
                fixture.setFixtureHistories(fixtureHistories);
            }
            return fixture;
        }

    }

    @Value
    class FixtureResponse {

        boolean success;
        List<String> messages;
        List<String> errors;

        public static final FixtureResponse EMPTY = new FixtureResponse(false, Collections.emptyList(), Collections.emptyList());

    }


}
