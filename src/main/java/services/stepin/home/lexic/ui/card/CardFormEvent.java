package services.stepin.home.lexic.ui.card;

import com.vaadin.flow.component.ComponentEvent;
import services.stepin.home.lexic.model.Card;

public abstract class CardFormEvent extends ComponentEvent<CardForm> {

    private Card card;

    public static class CardFormSaveEvent extends CardFormEvent {
        public CardFormSaveEvent(CardForm cardForm, Card card) {
            super(cardForm, card);
        }
    }

    public static class CardFormDeleteEvent extends CardFormEvent {
        public CardFormDeleteEvent(CardForm cardForm, Card card) {
            super(cardForm, card);
        }
    }

    public static class CardFormCloseEvent extends CardFormEvent {
        public CardFormCloseEvent(CardForm cardForm) {
            super(cardForm, null);
        }
    }

    protected CardFormEvent(CardForm cardForm, Card card) {
        super(cardForm, false);
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}


