package services.stepin.home.lexic.ui.card.strategy.mode;

import services.stepin.home.lexic.ui.card.CardForm;

public class CheckForeignMode implements Mode {

    private final CardForm cardForm;

    public CheckForeignMode(CardForm cardForm) {
        this.cardForm = cardForm;
    }

    @Override
    public void on() {
        cardForm.getSaveButton().setEnabled(false);
        cardForm.getDeleteButton().setEnabled(false);
    }

    @Override
    public void off() {

    }
}
