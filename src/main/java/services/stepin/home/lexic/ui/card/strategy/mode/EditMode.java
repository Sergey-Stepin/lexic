package services.stepin.home.lexic.ui.card.strategy.mode;

import services.stepin.home.lexic.ui.card.CardForm;

public class EditMode implements Mode {

    private final CardForm cardForm;

    public EditMode(CardForm cardForm) {
        this.cardForm = cardForm;
    }

    @Override
    public void on() {
        cardForm.getSaveButton().setEnabled(true);
        cardForm.getDeleteButton().setEnabled(true);
    }

    @Override
    public void off() {

    }
}
