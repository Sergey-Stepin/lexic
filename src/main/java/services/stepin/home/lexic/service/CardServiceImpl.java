package services.stepin.home.lexic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;
import services.stepin.home.lexic.repository.CardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;


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
        return null;
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
