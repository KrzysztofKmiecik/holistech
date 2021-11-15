package pl.kmiecik.holistech.fixture.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kmiecik.holistech.email.application.port.GmailService;
import pl.kmiecik.holistech.fis.application.port.FisService;
import pl.kmiecik.holistech.fixture.application.port.FixtureService;
import pl.kmiecik.holistech.fixture.domain.Fixture;
import pl.kmiecik.holistech.fixture.domain.FixtureHistory;
import pl.kmiecik.holistech.fixture.domain.ModificationReason;
import pl.kmiecik.holistech.fixture.domain.Status;
import pl.kmiecik.holistech.fixture.infrastructure.FixtureHistoryRepository;
import pl.kmiecik.holistech.fixture.infrastructure.FixtureRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Setter
@Getter
@Transactional
public class FixtureServiceUseCase implements FixtureService {

    private final FixtureRepository repository;
    private final FixtureHistoryRepository historyRepository;
    private final FisService fisService;
    private final GmailService gmailService;

    @Value("${fixture-service-usecase.emailTo}")
    private String[] emailToArray;

    @Override
    public List<Fixture> findAllFixtures() {
        return repository.findAll();
    }

    @Override
    public void saveFixture(final Fixture fixture, final FixtureHistory fixtureHistory) {
        repository.save(fixture);
        historyRepository.save(fixtureHistory);
    }

    @Override
    public Fixture setStrainStatus(final String id, final Status status) {
        Optional<Fixture> myFixture = repository.findById(Long.valueOf(id));
        if (myFixture.isPresent()) {
            myFixture.get().setStatusStrain(status);
            fisService.sendFixtureStatusToFis(myFixture.get());
            //   sendEmail(myFixture);
            return myFixture.get();
        } else {
            return new Fixture();
        }
    }

    @Override
    public void sendEmail(Optional<Fixture> myFixture) {
        int size = myFixture.get().getFixtureHistories().size();
        String lastChangeOwner = myFixture.get().getFixtureHistories().get(size - 1).getChangeOwner();
        String descriptionChange = myFixture.get().getFixtureHistories().get(size - 1).getDescriptionOfChange();
        String message = String.format("Fixture %s was change to %s  by  %s  with description %s ", myFixture.get().getName(), myFixture.get().getStatusStrain(), lastChangeOwner, descriptionChange);

        for (int i = 0; i < emailToArray.length; i++) {
            gmailService.sendSimpleMessage(emailToArray[i], "Status was change", message);
        }
    }

    @Override
    public FixtureHistory getFixtureHistory(final Fixture fixture, final String descriptionOfChange, final ModificationReason modificationReason) {
        FixtureHistory fixtureHistory = new FixtureHistory();
        fixtureHistory.setFixture(fixture);
        fixtureHistory.setModificationDateTime(LocalDateTime.now());
        fixtureHistory.setDescriptionOfChange(descriptionOfChange);
        fixtureHistory.setModificationReason(modificationReason);
        fixtureHistory.setChangeOwner(getSimpleGrantedAuthoritiesString());
        return fixtureHistory;
    }

    private String getSimpleGrantedAuthoritiesString() {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.toString();
    }

    @Override
    public void deleteFixture(final Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Fixture> findFixtureById(final Long id) {
        return repository.findById(id);
    }

    @Override
    public void setMyDefaultStrainStatus(final Fixture fixture) {
        fixture.setStatusStrain(Status.NOK);
        //   fixture.setFixtureHistories(Collections.emptyList());
    }

    @Override
    public void setMyExpiredStrainDate(final Fixture fixture) {
        LocalDate myDate = LocalDate.now();
        fixture.setExpiredDateStrain(myDate.plusMonths(6));
    }


}
