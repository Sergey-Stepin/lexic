package services.stepin.home.lexic.ui.card;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.shared.Registration;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;

import java.util.List;

import static services.stepin.home.lexic.model.LanguageCode.DE;
import static services.stepin.home.lexic.model.RepetitionFrequency.DAYLY;
import static services.stepin.home.lexic.ui.card.CardFormEvent.*;

public class CardForm extends FormLayout {

    private final Binder<Card> cardBinder = new BeanValidationBinder<>(Card.class);

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");

    private final ComboBox<LanguageCode> languageCode = new ComboBox<>("Language");
    private final ComboBox<RepetitionFrequency> repetitionFrequency = new ComboBox<>("Repeat");

    private TextField localWord = new TextField("Local");

    private Card card;

    private Grid<String> foreignWords = new Grid<>(String.class, false);
    private Binder binder = new BeanValidationBinder<>(String.class);

    public CardForm(List<LanguageCode> languageCodes, List<RepetitionFrequency> repetitionFrequencies) {

        addClassName("card-form-class");
        cardBinder.bindInstanceFields(this);

        add(localWord);

        languageCode.setItems(languageCodes);
        add(languageCode);

        repetitionFrequency.setItems(repetitionFrequencies);
        add(repetitionFrequency);

        addForeignWordsGrid();
        addButtonsLayout();

        languageCode.setValue(DE);
        repetitionFrequency.setValue(DAYLY);
    }

    private void addButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new CardFormEvent.CardFormDeleteEvent(this, card)));
        cancel.addClickListener(event -> fireEvent(new CardFormEvent.CardFormCloseEvent(this)));

        HorizontalLayout buttonLayout = new HorizontalLayout(save, delete, cancel);
        add(buttonLayout);
    }

    private void addForeignWordsGrid() {

        var editor = foreignWords.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(false);
        add(foreignWords);
    }

    public void setCard(Card card) {
        this.card = card;
        cardBinder.readBean(card);
    }

    private void validateAndSave() {
        try {
            cardBinder.writeBean(card);
            fireEvent(new CardFormEvent.CardFormSaveEvent(this, card));
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<CardFormDeleteEvent> listener) {
        return addListener(CardFormDeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<CardFormSaveEvent> listener) {
        return addListener(CardFormSaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CardFormCloseEvent> listener) {
        return addListener(CardFormCloseEvent.class, listener);
    }

}
