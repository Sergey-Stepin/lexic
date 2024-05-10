package services.stepin.home.lexic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;
import services.stepin.home.lexic.repository.CardRepository;
import services.stepin.home.lexic.ui.dataprovider.CardFilter;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;

    @Override
    public Slice<Card> find(LanguageCode languageCode, Pageable pageable) {
        return cardRepository.findAllByLanguageCodeOrderByCardId(languageCode, pageable);
    }

    @Override
    public Slice<Card> find(CardFilter filter, Pageable pageable) {

        String localWordContains = filter.getLocalWordContains();
        String foreignWordContains = filter.getForeignWordContains();
        RepetitionFrequency repetitionFrequency = filter.getRepetitionFrequency();

        if(repetitionFrequency != null && hasText(localWordContains) && hasText(foreignWordContains))
            return cardRepository.findByRepetitionFrequencyAndLocalWordContainsIgnoreCaseAndForeignWordContainsIgnoreCaseOrderByCardId(
                    repetitionFrequency,
                    localWordContains,
                    foreignWordContains,
                    pageable);
        else if (repetitionFrequency != null && hasText(localWordContains))
            return cardRepository.findByRepetitionFrequencyAndLocalWordContainsIgnoreCaseOrderByCardId(
                    repetitionFrequency,
                    localWordContains,
                    pageable);
        else if (repetitionFrequency != null && hasText(foreignWordContains))
            return cardRepository.findByRepetitionFrequencyAndForeignWordContainsIgnoreCaseOrderByCardId(
                    repetitionFrequency,
                    foreignWordContains,
                    pageable);
        else if(hasText(localWordContains) && hasText(foreignWordContains))
            return cardRepository.findByLocalWordContainsIgnoreCaseAndForeignWordContainsIgnoreCaseOrderByCardId(
                    localWordContains,
                    foreignWordContains,
                    pageable);
        else if(repetitionFrequency != null)
            return cardRepository.findByRepetitionFrequencyOrderByCardId(repetitionFrequency, pageable);
        else if(hasText(localWordContains))
            return cardRepository.findByLocalWordContainsIgnoreCaseOrderByCardId(localWordContains, pageable);
        else if(hasText(foreignWordContains))
            return cardRepository.findByForeignWordContainsIgnoreCaseOrderByCardId(foreignWordContains, pageable);
        else
            return cardRepository.findAll(pageable);
    }

    @Override
    public Slice<Card> findAll(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public void delete(Card card) {
        cardRepository.delete(card);
    }

    @Override
    public long count() {
        return cardRepository.count();
    }

    @Override
    public long count(CardFilter filter) {

        String localWordContains = filter.getLocalWordContains();
        String foreignWordContains = filter.getForeignWordContains();
        RepetitionFrequency repetitionFrequency = filter.getRepetitionFrequency();

        if(repetitionFrequency != null && hasText(localWordContains) && hasText(foreignWordContains))
            return cardRepository.countByRepetitionFrequencyAndLocalWordContainsIgnoreCaseAndForeignWordContainsIgnoreCase(
                    repetitionFrequency,
                    localWordContains,
                    foreignWordContains);
        else if (repetitionFrequency != null && hasText(localWordContains))
            return cardRepository.countByRepetitionFrequencyAndLocalWordContainsIgnoreCase(
                    repetitionFrequency,
                    localWordContains);
        else if (repetitionFrequency != null && hasText(foreignWordContains))
            return cardRepository.countByRepetitionFrequencyAndForeignWordContainsIgnoreCase(
                    repetitionFrequency,
                    foreignWordContains);
        else if(hasText(localWordContains) && hasText(foreignWordContains))
            return cardRepository.countByLocalWordContainsIgnoreCaseAndForeignWordContainsIgnoreCase(
                    localWordContains,
                    foreignWordContains);
        else if(repetitionFrequency != null)
            return cardRepository.countByRepetitionFrequency(repetitionFrequency);
        else if(hasText(localWordContains))
            return cardRepository.countByLocalWordContainsIgnoreCase(localWordContains);
        else if(hasText(foreignWordContains))
            return cardRepository.countByForeignWordContainsIgnoreCase(foreignWordContains);
        else
            return cardRepository.count();
    }
}
