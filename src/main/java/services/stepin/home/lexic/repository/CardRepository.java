package services.stepin.home.lexic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import services.stepin.home.lexic.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
