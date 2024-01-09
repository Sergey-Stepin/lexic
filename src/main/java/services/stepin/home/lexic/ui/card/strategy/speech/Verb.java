package services.stepin.home.lexic.ui.card.strategy.speech;

import services.stepin.home.lexic.ui.card.CardForm;

public class Verb implements PartOfSpeech {

    private final CardForm cardForm;

    public Verb(CardForm cardForm) {
        this.cardForm = cardForm;
    }

    @Override
    public void on() {

    }

    @Override
    public void off() {

    }
}
