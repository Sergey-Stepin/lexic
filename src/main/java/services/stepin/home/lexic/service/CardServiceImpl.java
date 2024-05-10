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
            return cardRepository.findByRepetitionFrequencyAndLocalWordContainsAndForeignWordContainsOrderByCardId(
                    repetitionFrequency,
                    localWordContains,
                    foreignWordContains,
                    pageable);
        else if (repetitionFrequency != null && hasText(localWordContains))
            return cardRepository.findByRepetitionFrequencyAndLocalWordContainsOrderByCardId(
                    repetitionFrequency,
                    localWordContains,
                    pageable);
        else if (repetitionFrequency != null && hasText(foreignWordContains))
            return cardRepository.findByRepetitionFrequencyAndForeignWordContainsOrderByCardId(
                    repetitionFrequency,
                    foreignWordContains,
                    pageable);
        else if(hasText(localWordContains) && hasText(foreignWordContains))
            return cardRepository.findByLocalWordContainsAndForeignWordContainsOrderByCardId(
                    localWordContains,
                    foreignWordContains,
                    pageable);
        else if(repetitionFrequency != null)
            return cardRepository.findByRepetitionFrequencyOrderByCardId(repetitionFrequency, pageable);
        else if(hasText(localWordContains))
            return cardRepository.findByLocalWordContainsOrderByCardId(localWordContains, pageable);
        else if(hasText(foreignWordContains))
            return cardRepository.findByForeignWordContainsOrderByCardId(foreignWordContains, pageable);
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
            return cardRepository.countByRepetitionFrequencyAndLocalWordContainsAndForeignWordContains(
                    repetitionFrequency,
                    localWordContains,
                    foreignWordContains);
        else if (repetitionFrequency != null && hasText(localWordContains))
            return cardRepository.countByRepetitionFrequencyAndLocalWordContains(
                    repetitionFrequency,
                    localWordContains);
        else if (repetitionFrequency != null && hasText(foreignWordContains))
            return cardRepository.countByRepetitionFrequencyAndForeignWordContains(
                    repetitionFrequency,
                    foreignWordContains);
        else if(hasText(localWordContains) && hasText(foreignWordContains))
            return cardRepository.countByLocalWordContainsAndForeignWordContains(
                    localWordContains,
                    foreignWordContains);
        else if(repetitionFrequency != null)
            return cardRepository.countByRepetitionFrequency(repetitionFrequency);
        else if(hasText(localWordContains))
            return cardRepository.countByLocalWordContains(localWordContains);
        else if(hasText(foreignWordContains))
            return cardRepository.countByForeignWordContains(foreignWordContains);
        else
            return cardRepository.count();
    }
}
