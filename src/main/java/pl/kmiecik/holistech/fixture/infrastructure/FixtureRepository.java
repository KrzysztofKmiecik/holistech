package pl.kmiecik.holistech.fixture.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kmiecik.holistech.fixture.domain.Fixture;

@Repository
public interface FixtureRepository extends JpaRepository<Fixture, Long> {
}
