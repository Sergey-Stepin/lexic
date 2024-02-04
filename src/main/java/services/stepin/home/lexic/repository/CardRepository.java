package services.stepin.home.lexic.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByRepetitionFrequencyAndLocalWordContainsOrderByCardId(RepetitionFrequency repetitionFrequency, String contains);

    List<Card> findByRepetitionFrequencyOrderByCardId(RepetitionFrequency repetitionFrequency);

    List<Card> findByLocalWordContainsOrderByCardId(String contains);

    Slice<Card> findAllByLanguageCodeOrderByCardId(LanguageCode languageCode, Pageable pageable);

}
