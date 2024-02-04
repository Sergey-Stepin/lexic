package services.stepin.home.lexic.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;
import services.stepin.home.lexic.ui.dataprovider.CardFilter;

import java.util.List;

@Service
public interface CardService {

    Slice<Card> find(LanguageCode languageCode, Pageable pageable);

    Slice<Card> find(CardFilter filter, Pageable pageable);
    Slice<Card> findAll(Pageable pageable);

    Card save(Card card);

    void delete(Card card);

    long count();

    long count(CardFilter cardFilter);
}
