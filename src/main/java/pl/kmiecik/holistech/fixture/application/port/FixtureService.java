package pl.kmiecik.holistech.fixture.application.port;

import lombok.Value;
import pl.kmiecik.holistech.fixture.domain.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface FixtureService {

    List<Fixture> findAllFixtures();

    void saveFixture(Fixture fixture, FixtureHistory fixtureHistory);

    Fixture setStrainStatus(String id, Status ok);

    void sendEmail(Fixture myFixture);

    FixtureHistory getFixtureHistory(Fixture fixture, String descriptionOfChange, ModificationReason modificationReason);

    void deleteFixture(Long id);

    void setMyDefaultStrainStatus(Fixture fixture);

    void setMyExpiredStrainDate(Fixture fixture);

    Optional<Fixture> findFixtureById(Long valueOf);

    @Value
    class FixtureCommand {

        Fixture fixure;

    }

    @Value
    class FixtureResponse {

        boolean success;
        List<String> errors;

        public static FixtureResponse success() {
            return new FixtureResponse(true, Collections.emptyList());
        }

        public static FixtureResponse failure(String... errors) {
            return new FixtureResponse(false, Arrays.asList(errors));
        }


    }


}
