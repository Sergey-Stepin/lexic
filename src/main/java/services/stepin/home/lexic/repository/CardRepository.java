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

	Slice<Card> findByRepetitionFrequencyAndLocalWordContainsIgnoreCaseAndForeignWordContainsIgnoreCaseOrderByCardId(
			RepetitionFrequency repetitionFrequency,
			String localWordContains,
			String foreignWordContains,
			Pageable pageable);

	Slice<Card> findByRepetitionFrequencyAndLocalWordContainsIgnoreCaseOrderByCardId(
			RepetitionFrequency repetitionFrequency,
			String localWordContains,
			Pageable pageable);

	Slice<Card> findByRepetitionFrequencyAndForeignWordContainsIgnoreCaseOrderByCardId(
			RepetitionFrequency repetitionFrequency,
			String foreignWordContains,
			Pageable pageable);

	Slice<Card> findByLocalWordContainsIgnoreCaseAndForeignWordContainsIgnoreCaseOrderByCardId(
			String localWordContains,
			String foreignWordContains,
			Pageable pageable);

	Slice<Card> findByForeignWordContainsIgnoreCaseOrderByCardId(
			String foreignWordContains,
			Pageable pageable);

	Slice<Card> findByLocalWordContainsIgnoreCaseOrderByCardId(
			String localWordContains,
			Pageable pageable);

	long countByRepetitionFrequencyAndLocalWordContainsIgnoreCaseAndForeignWordContainsIgnoreCase(
			RepetitionFrequency repetitionFrequency,
			String localWordContains,
			String foreignWordContains);

	long countByRepetitionFrequencyAndLocalWordContainsIgnoreCase(
			RepetitionFrequency repetitionFrequency,
			String localWordContains);

	long countByRepetitionFrequencyAndForeignWordContainsIgnoreCase(
			RepetitionFrequency repetitionFrequency,
			String foreignWordContains);

	long countByLocalWordContainsIgnoreCaseAndForeignWordContainsIgnoreCase(
			String localWordContains,
			String foreignWordContains);

	long countByForeignWordContainsIgnoreCase(String foreignWordContains);

	long countByLocalWordContainsIgnoreCase(String localWordContains);

	long countByRepetitionFrequency(RepetitionFrequency repetitionFrequency);

}
