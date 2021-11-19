package pl.kmiecik.holistech.fixture.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kmiecik.holistech.config.CustomProperties;
import pl.kmiecik.holistech.email.application.port.GmailService;
import pl.kmiecik.holistech.fis.application.port.FisService;
import pl.kmiecik.holistech.fixture.application.port.FixtureService;
import pl.kmiecik.holistech.fixture.domain.*;
import pl.kmiecik.holistech.fixture.infrastructure.FixtureHistoryRepository;
import pl.kmiecik.holistech.fixture.infrastructure.FixtureRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
@Setter
@Getter
@Transactional
class FixtureServiceUseCase implements FixtureService {

    private final FixtureRepository repository;
    private final FixtureHistoryRepository historyRepository;
    private final FisService fisService;
    private final GmailService gmailService;
    private final CustomProperties customProperties;
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
            return myFixture.get();
        } else {
            return new Fixture();
        }
    }

    @Override
    public void sendEmail(final Fixture myFixture) {

        if (myFixture != null) {
            int size = myFixture.getFixtureHistories().size();
            String lastChangeOwner = myFixture.getFixtureHistories().get(size - 1).getChangeOwner();
            String descriptionChange = myFixture.getFixtureHistories().get(size - 1).getDescriptionOfChange();
            String message = String.format("Fixture %s was change to %s  by  %s  with description %s ", myFixture.getName(), myFixture.getStatusStrain(), lastChangeOwner, descriptionChange);
            emailToArray = customProperties.getEmailreceiver();
            IntStream.range(0, emailToArray.length).forEach(i -> gmailService.sendSimpleMessage(emailToArray[i], "Status was change", message));
        }
    }

    @Override
    public FixtureHistory createFixtureHistory(final Fixture fixture, final String descriptionOfChange, final ModificationReason modificationReason) {
        FixtureHistory fixtureHistory = new FixtureHistory();
        fixtureHistory.setFixture(fixture);
        fixtureHistory.setModificationDateTime(LocalDateTime.now());
        fixtureHistory.setDescriptionOfChange(descriptionOfChange);
        fixtureHistory.setModificationReason(modificationReason);
        fixtureHistory.setChangeOwner(getSimpleGrantedAuthoritiesString());
        return fixtureHistory;
    }

    private String getSimpleGrantedAuthoritiesString() {
        String authority = "";
        Optional<? extends GrantedAuthority> grantedAuthority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst();
        if (grantedAuthority.isPresent()) {
            authority = grantedAuthority.get().getAuthority();
        }
        return authority;
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
    public FixtureResponse updateFixture(Long id,UpdateFixtureCommand command) {
        Fixture fixture= command.;
        FisProcess fisProcess;
        boolean status;
        List<String> messages = new ArrayList<>();
        List<String> errors;
        Optional<Fixture> fixtureById = this.findFixtureById(id);
        String messageName = "", messageFis = "";
        messages.add(messageName);
        messages.add(messageFis);

        if (fixtureById.isPresent()) {
            Fixture fixtureOld = fixtureById.get();
            String name = fixtureOld.getName();
            fisProcess = fixtureOld.getFisProcess();
            if (!name.equals(fixture.getName())) {
                messages.set(0, String.format("name was change from  %s to %s", name, fixture.getName()));
            }
            if (!fisProcess.name().equals(fixture.getFisProcess().name())) {
                messages.set(1, String.format("Fis_Process was change from  %s to %s", fisProcess.name(), fixture.getFisProcess().name()));
            }
            status = true;
            errors = Collections.emptyList();
        }else{
            status=false;
            errors=Collections.singletonList("updateFixture error");
        }

        return new FixtureResponse(status, messages, errors);

    }

    @Override
    public void setMyDefaultStrainStatus(final Fixture fixture) {
        fixture.setStatusStrain(Status.NOK);
    }

    @Override
    public void setMyExpiredStrainDate(final Fixture fixture) {
        LocalDate myDate = LocalDate.now();
        fixture.setExpiredDateStrain(myDate.plusMonths(6));
    }
}
