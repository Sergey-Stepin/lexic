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

	Slice<Card> findAllByLanguageCodeOrderByCardId(
			LanguageCode languageCode,
			Pageable pageable);

	Slice<Card> findByRepetitionFrequencyOrderByCardId(
			RepetitionFrequency repetitionFrequency,
			Pageable pageable);

	Slice<Card> findByRepetitionFrequencyAndLocalWordContainsAndForeignWordContainsOrderByCardId(
			RepetitionFrequency repetitionFrequency,
			String localWordContains,
			String foreignWordContains,
			Pageable pageable);

	Slice<Card> findByRepetitionFrequencyAndLocalWordContainsOrderByCardId(
			RepetitionFrequency repetitionFrequency,
			String localWordContains,
			Pageable pageable);

	Slice<Card> findByRepetitionFrequencyAndForeignWordContainsOrderByCardId(
			RepetitionFrequency repetitionFrequency,
			String foreignWordContains,
			Pageable pageable);

	Slice<Card> findByLocalWordContainsAndForeignWordContainsOrderByCardId(
			String localWordContains,
			String foreignWordContains,
			Pageable pageable);

	Slice<Card> findByForeignWordContainsOrderByCardId(
			String foreignWordContains,
			Pageable pageable);

	Slice<Card> findByLocalWordContainsOrderByCardId(
			String localWordContains,
			Pageable pageable);

	long countByRepetitionFrequencyAndLocalWordContainsAndForeignWordContains(
			RepetitionFrequency repetitionFrequency,
			String localWordContains,
			String foreignWordContains);

	long countByRepetitionFrequencyAndLocalWordContains(
			RepetitionFrequency repetitionFrequency,
			String localWordContains);

	long countByRepetitionFrequencyAndForeignWordContains(
			RepetitionFrequency repetitionFrequency,
			String foreignWordContains);

	long countByLocalWordContainsAndForeignWordContains(
			String localWordContains,
			String foreignWordContains);

	long countByForeignWordContains(String foreignWordContains);

	long countByLocalWordContains(String localWordContains);

	long countByRepetitionFrequency(RepetitionFrequency repetitionFrequency);
}
