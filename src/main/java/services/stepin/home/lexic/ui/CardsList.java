package services.stepin.home.lexic.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;
import services.stepin.home.lexic.service.CardService;
import services.stepin.home.lexic.ui.card.CardFormEvent;
import services.stepin.home.lexic.ui.card.CardForm;

import static com.vaadin.flow.data.value.ValueChangeMode.LAZY;
import static services.stepin.home.lexic.model.Card.LANGUAGE_CODES;
import static services.stepin.home.lexic.model.Card.REPETITION_FREQUENCIES;
import static services.stepin.home.lexic.model.LanguageCode.DE;
import static services.stepin.home.lexic.model.LanguageCode.RU;

@Route(value = "lexic")
@PageTitle("Lexic | cards")
public class CardsList extends VerticalLayout {

    private final CardService cardService;

    public static final LanguageCode FOREIGN_LANGUAGE = DE;
    public static final LanguageCode LOCAL_LANGUAGE = RU;
    private final ComboBox<RepetitionFrequency> repetitionFrequencyFilter = new ComboBox<>();
    private final TextField localWordFilter = new TextField();;

    private Grid<Card> cardsGrid;
    private CardForm cardForm;

    public CardsList(CardService cardService) {

        this.cardService = cardService;

        addClassName("cards-class");
        setSizeFull();

        add(createToolbar());
        add(createContent());

        updateList();
        closeEditor();
    }

    private Component createToolbar() {

        localWordFilter.setPlaceholder("find by word ...");
        localWordFilter.setClearButtonVisible(true);
        localWordFilter.setValueChangeMode(LAZY);
        localWordFilter.addValueChangeListener(event -> updateList());

        Button addCardButton = new Button("Add card");
        addCardButton.addClickListener(event -> addCard());

        HorizontalLayout toolBar = new HorizontalLayout(localWordFilter, addCardButton);
        toolBar.addClassName("toolbar-class");

        return toolBar;
    }

    private Component createContent() {

        this.cardsGrid = createCardsGrid();
        this.cardForm = createCardForm();

        HorizontalLayout contentLayout = new HorizontalLayout(cardsGrid, cardForm);
        contentLayout.addClassName("content-class");

        cardsGrid.setMinWidth("300px");
        contentLayout.setFlexGrow(1, cardsGrid);

        cardForm.setSizeFull();
        contentLayout.setFlexGrow(2, cardForm);

        contentLayout.setSizeFull();

        return contentLayout;

    }

    private Grid<Card> createCardsGrid() {

        Grid<Card> cardsGrid = new Grid<>(Card.class);

        cardsGrid.addClassName("card-grid-class");
        //cardsGrid.setSizeFull();

        cardsGrid.setColumns("localWord", "languageCode", "repetitionFrequency");

        //cardsGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        cardsGrid.asSingleSelect().addValueChangeListener(event -> editCard(event.getValue()));

        return cardsGrid;
    }

    private CardForm createCardForm() {

        CardForm cardForm = new CardForm(LANGUAGE_CODES, REPETITION_FREQUENCIES);

        cardForm.addSaveListener(this::saveCard);
        cardForm.addDeleteListener(this::deleteCard);
        cardForm.addCloseListener(event -> closeEditor());


        return cardForm;
    }

    private void addCard() {
        cardsGrid.asSingleSelect().clear();
        editCard(new Card());
    }

    private void saveCard(CardFormEvent.CardFormSaveEvent event){

        System.out.println("CardList (SAVE LISTENER): ");

        cardService.save(event.getCard());
        updateList();
        closeEditor();
    }

    private void deleteCard(CardFormEvent.CardFormDeleteEvent event) {
        cardService.delete(event.getCard());
        updateList();
        closeEditor();
    }

    private void updateList() {
        cardsGrid.setItems(cardService.find(FOREIGN_LANGUAGE, localWordFilter.getValue()));}

    private void editCard(Card card) {
        if(card == null){
            closeEditor();
        }

        cardForm.setCard(card);
        cardForm.setVisible(true);

        addClassName("editing-class");
    }

    private void closeEditor() {
        cardForm.setCard(null);
        cardForm.setVisible(false);
        removeClassName("editing-class");
    }
}
