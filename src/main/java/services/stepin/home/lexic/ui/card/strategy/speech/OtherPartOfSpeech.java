package services.stepin.home.lexic.ui.card.strategy.speech;

import services.stepin.home.lexic.ui.card.CardForm;

public class OtherPartOfSpeech implements PartOfSpeech {

    private final CardForm cardForm;

    public OtherPartOfSpeech(CardForm cardForm) {
        this.cardForm = cardForm;
    }

    @Override
    public void on() {

    }

    @Override
    public void off() {

    }
}
