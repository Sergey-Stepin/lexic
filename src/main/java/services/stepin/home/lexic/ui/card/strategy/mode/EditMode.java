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

        cardForm.getForeignWord().setVisible(true);
        cardForm.getCheckWord().setVisible(false);

        cardForm.getCheckFirstExample().setVisible(false);
        cardForm.getCheckSecondExample().setVisible(false);
        cardForm.getCheckThirdExample().setVisible(false);

        cardForm.getForeignFirstExample().setVisible(true);
        cardForm.getForeignSecondExample().setVisible(true);
        cardForm.getForeignThirdExample().setVisible(true);

    }

    @Override
    public void off() {

    }
}
