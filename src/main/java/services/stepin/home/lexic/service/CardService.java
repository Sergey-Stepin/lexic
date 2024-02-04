package services.stepin.home.lexic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;

import java.util.List;

@Service
public interface CardService {

    Slice<Card> findAllByLanguageCode(LanguageCode languageCode, Pageable pageable);
    List<Card> find(LanguageCode languageCode);

    List<Card> find(LanguageCode languageCode, RepetitionFrequency repetitionFrequency);

    List<Card> find(LanguageCode languageCode, String contains);

    List<Card> find(LanguageCode languageCode, RepetitionFrequency repetitionFrequency, String contains);

    Card save(Card card);

    void delete(Card card);

}
