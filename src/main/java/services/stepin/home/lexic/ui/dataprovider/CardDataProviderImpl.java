package services.stepin.home.lexic.ui.dataprovider;

import com.vaadin.flow.data.provider.AbstractDataProvider;
import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.service.CardService;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CardDataProviderImpl extends AbstractDataProvider<Card, CardFilter> implements CardDataProvider {

    private final CardService cardService;

    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public int size(Query<Card, CardFilter> query) {
        Optional<CardFilter> filter = query.getFilter();

        return filter
                .map(this::countWithFilter)
                .orElseGet(this::countAll);
    }

    private Integer countAll() {
        return (int) cardService.count();
    }

    private int countWithFilter(CardFilter cardFilter) {
        return (int) cardService.count(cardFilter);
    }

    @Override
    public Stream<Card> fetch(Query<Card, CardFilter> query) {

        Pageable pageable = createPageable(query);
        Optional<CardFilter> filter = query.getFilter();

        Stream<Card> cardStream = filter
                .map(cardFilter -> fetchWithFilter(cardFilter, pageable))
                .orElseGet(() -> fetchAll(pageable));

        return markCards(cardStream, filter);
    }

    private Stream<Card> fetchWithFilter(CardFilter cardFilter, Pageable pageable) {
        return cardService.find(cardFilter, pageable)
                .stream();
    }

    private Stream<Card> fetchAll(Pageable pageable) {
        return cardService.findAll(pageable)
                .stream();
    }

    private Pageable createPageable(Query<Card, CardFilter> query) {

        int offset = query.getOffset();
        int limit = query.getLimit();
        int pageNumber = offset / limit;

        return PageRequest.of(pageNumber, limit);
    }

    private Stream<Card> markCards(Stream<Card> cardStream, Optional<CardFilter> filter) {
        return cardStream
                .filter(card -> applyAgain(card, filter))
                .peek(card -> markAgain(card, filter));
    }

    private void markAgain(Card card, Optional<CardFilter> filter) {

        filter.ifPresent(cardFilter ->
                markAgain(card, cardFilter));
    }

    private void markAgain(Card card, CardFilter filter) {
        if (filter.getCheckAgainCards().contains(card))
            card.setAgain(true);
    }

    private boolean applyAgain(Card card, Optional<CardFilter> filter) {

        return filter.filter(cardFilter -> applyAgain(card, cardFilter))
                .isPresent();
    }

    private boolean applyAgain(Card card, CardFilter filter) {

        if (!filter.isCheckAgain())
            return true;

        return filter.getCheckAgainCards().contains(card);
    }

}
