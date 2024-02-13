package gr.uoa.di.project.ebids.bids;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidsRepository extends JpaRepository<Bids, Long> {
}
