package pl.kmiecik.holistech.fixture.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kmiecik.holistech.fixture.domain.FixtureHistory;

@Repository
public interface FixtureHistoryRepository extends JpaRepository<FixtureHistory, Long> {
}
