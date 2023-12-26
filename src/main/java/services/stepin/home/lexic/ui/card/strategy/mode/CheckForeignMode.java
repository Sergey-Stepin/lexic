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

        cardForm.getForeignWord().setVisible(false);
        cardForm.getCheckWord().setVisible(true);

        cardForm.getCheckFirstExample().setVisible(true);
        cardForm.getCheckSecondExample().setVisible(true);
        cardForm.getCheckThirdExample().setVisible(true);

        cardForm.getForeignFirstExample().setVisible(false);
        cardForm.getForeignSecondExample().setVisible(false);
        cardForm.getForeignThirdExample().setVisible(false);

        cardForm.getCheckFirstExample().setTooltipText(cardForm.getForeignFirstExample().getValue());
        cardForm.getCheckSecondExample().setTooltipText(cardForm.getForeignSecondExample().getValue());
        cardForm.getCheckThirdExample().setTooltipText(cardForm.getForeignThirdExample().getValue());
    }

    @Override
    public void off() {

    }
}
