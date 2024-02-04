package services.stepin.home.lexic.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Slice<Card> findByRepetitionFrequencyAndLocalWordContainsOrderByCardId(
            RepetitionFrequency repetitionFrequency,
            String contains,
            Pageable pageable);

    Slice<Card> findByRepetitionFrequencyOrderByCardId(RepetitionFrequency repetitionFrequency, Pageable pageable);

    Slice<Card> findByLocalWordContainsOrderByCardId(String contains, Pageable pageable);

    Slice<Card> findAllByLanguageCodeOrderByCardId(LanguageCode languageCode, Pageable pageable);

    long countByRepetitionFrequencyAndLocalWordContainsOrderByCardId(
            RepetitionFrequency repetitionFrequency,
            String contains);

    long countByRepetitionFrequencyOrderByCardId(RepetitionFrequency repetitionFrequency);

    long countByLocalWordContainsOrderByCardId(String localWordContains);
}
