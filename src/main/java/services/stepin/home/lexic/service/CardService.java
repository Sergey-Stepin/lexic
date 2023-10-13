package services.stepin.home.lexic.service;

import org.springframework.stereotype.Service;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;

import java.util.List;

@Service
public interface CardService {


    List<Card> find(LanguageCode languageCode);

    List<Card> find(LanguageCode languageCode, RepetitionFrequency repetitionFrequency);

    List<Card> find(LanguageCode languageCode, String startsWith);

    List<Card> find(LanguageCode languageCode, RepetitionFrequency repetitionFrequency, String startsWith);

    Card save(Card card);

    void delete(Card card);

}
