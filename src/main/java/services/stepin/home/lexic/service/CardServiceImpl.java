package services.stepin.home.lexic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;
import services.stepin.home.lexic.repository.CardRepository;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;


    @Override
    public Slice<Card> findAllByLanguageCode(LanguageCode languageCode, Pageable pageable) {
        return cardRepository.findAllByLanguageCodeOrderByCardId(languageCode, pageable);
    }

    @Override
    public List<Card> find(LanguageCode languageCode) {
        return null;
    }

    @Override
    public List<Card> find(LanguageCode languageCode, RepetitionFrequency repetitionFrequency) {
        return null;
    }

    @Override
    public List<Card> find(LanguageCode languageCode, String startsWith) {
        return cardRepository.findAll();
    }

    @Override
    public List<Card> find(LanguageCode languageCode, RepetitionFrequency repetitionFrequency, String startsWith) {

        if(repetitionFrequency != null && hasText(startsWith))
            return cardRepository.findByRepetitionFrequencyAndLocalWordStartsWithOrderByCardId(repetitionFrequency, startsWith);
        else if(repetitionFrequency != null)
            return cardRepository.findByRepetitionFrequencyOrderByCardId(repetitionFrequency);
        else if(hasText(startsWith))
            return cardRepository.findByLocalWordStartsWithOrderByCardId(startsWith);
        else
            return cardRepository.findAll();
    }

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public void delete(Card card) {
        cardRepository.delete(card);
    }


}
