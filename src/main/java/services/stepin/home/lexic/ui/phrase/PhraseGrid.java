package services.stepin.home.lexic.ui.phrase;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.grid.editor.EditorCancelEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import services.stepin.home.lexic.model.Phrase;
import services.stepin.home.lexic.ui.person.ValidationMessage;

import java.util.Collection;

import static com.vaadin.flow.component.AbstractField.*;
import static com.vaadin.flow.component.grid.Grid.SelectionMode.SINGLE;


public class PhraseGrid extends VerticalLayout {

    private final Grid<Phrase> phraseGrid = new Grid<>(Phrase.class, false);
    private final TextField examField = new TextField();
    private final Button examButton = new Button("Check");
    private final Button removeButton = new Button("Remove");
    private final Editor<Phrase> editor = phraseGrid.getEditor();
    private final Binder<Phrase> binder = new Binder<>(Phrase.class);
    private final ValidationMessage localPhraseValidationMessage = new ValidationMessage();
    private final ValidationMessage foreignPhraseValidationMessage = new ValidationMessage();
    private final ValidationMessage examValidationMessage = new ValidationMessage();

    private Grid.Column<Phrase> localPhraseColumn;
    private Grid.Column<Phrase> foreignPhraseColumn;
    private Grid.Column<Phrase> examColumn;
    private Grid.Column<Phrase> actionsColumn;

    public PhraseGrid() {
        prepareGrid();
    }

    public void addPhrase(Phrase phrase) {

        phraseGrid.getEditor().save();

        var listDataProvider = (ListDataProvider<Phrase>) phraseGrid.getDataProvider();
        listDataProvider.getItems().add(phrase);
        listDataProvider.refreshAll();

        phraseGrid.select(phrase);
    }

    public void removePhrase(Phrase phrase) {

        var listDataProvider = (ListDataProvider<Phrase>) phraseGrid.getDataProvider();
        listDataProvider.getItems().remove(phrase);
        listDataProvider.refreshAll();

        phraseGrid.asSingleSelect().clear();

    }

    public void setPhrases(Collection<Phrase> phrases) {
        ListDataProvider<Phrase> dataProvider = new ListDataProvider<>(phrases);
        phraseGrid.setDataProvider(dataProvider);
        dataProvider.refreshAll();
    }

    public Collection<Phrase> getPhrases() {
        editor.save();
        var listDataProvider = (ListDataProvider<Phrase>) phraseGrid.getDataProvider();
        return listDataProvider.getItems();
    }

    private Component prepareGrid() {

        phraseGrid.asSingleSelect().addValueChangeListener(this::listenValueChange);

        phraseGrid.setSelectionMode(SINGLE);
        phraseGrid.asSingleSelect().setRequiredIndicatorVisible(true);

        prepareColumns();
        prepareEditor();
        
        getThemeList().clear();
        getThemeList().add("spacing-s");
        add(phraseGrid, localPhraseValidationMessage, foreignPhraseValidationMessage, examValidationMessage);

        return phraseGrid;
    }

    private void prepareColumns() {
        prepareLocalPhraseColumn();
        prepareForeignPhraseColumn();
        prepareExamColumn();
        prepareActionsColumn();
    }

    private void prepareLocalPhraseColumn() {
        localPhraseColumn = phraseGrid
                .addColumn(Phrase::getLocalPhrase)
                .setWidth("300px")
                .setFlexGrow(0);

        TextField localPhraseField = new TextField();
        localPhraseField.setWidthFull();

        binder.forField(localPhraseField)
                .asRequired("Local Phrase must not be empty")
                .withStatusLabel(localPhraseValidationMessage)
                .bind(Phrase::getLocalPhrase, Phrase::setLocalPhrase);
        localPhraseColumn.setEditorComponent(localPhraseField);
    }

    private void prepareForeignPhraseColumn() {
        foreignPhraseColumn = phraseGrid
                .addColumn(Phrase::getForeignPhrase)
                .setWidth("300px");

        TextField foreignPhraseField = new TextField();
        foreignPhraseField.setWidthFull();

        binder.forField(foreignPhraseField).asRequired("Foreig Phrase must not be empty")
                .withStatusLabel(foreignPhraseValidationMessage)
                .bind(Phrase::getForeignPhrase, Phrase::setForeignPhrase);
        foreignPhraseColumn.setEditorComponent(foreignPhraseField);
    }

    private void prepareExamColumn() {
         examColumn = phraseGrid
                .addColumn(Phrase::getPhraseExam)
                .setWidth("300px");

        examField.setWidthFull();

        binder.forField(examField)
                .withStatusLabel(examValidationMessage)
                .bind(Phrase::getPhraseExam, Phrase::setPhraseExam);
        examColumn.setEditorComponent(examField);
    }

    private void prepareActionsColumn() {
         actionsColumn = phraseGrid
                .addColumn(unused -> "")
                .setWidth("200px")
                .setFlexGrow(0);

        examButton.addClickListener(this::listenExamButton);
        removeButton.addClickListener(this::listenRemoveButton);

        HorizontalLayout editorActions = new HorizontalLayout(examButton, removeButton);
        editorActions.setPadding(false);
        actionsColumn.setEditorComponent(editorActions);
    }

    private void listenExamButton(ClickEvent<Button> event) {

        TextField foreign = (TextField) foreignPhraseColumn.getEditorComponent();
        TextField exam = (TextField) examColumn.getEditorComponent();

        String currentForeignBackgroundColour = foreign.getStyle().get("background-color");

        if (foreign.getValue().trim().equals(exam.getValue().trim()))
            exam.getStyle().set("background-color", currentForeignBackgroundColour);
        else
            exam.getStyle().set("background-color", "#FFC0CB");
    }

    private void listenRemoveButton(ClickEvent<Button> event) {
        Phrase selectedPhrase = phraseGrid.asSingleSelect().getValue();
        removePhrase(selectedPhrase);
    }

    private void listenValueChange(ComponentValueChangeEvent<Grid<Phrase>, Phrase> event) {

        Phrase phrase = event.getValue();
        if (phrase != null) {
            editor.editItem(phrase);
        }

        examField.setValue("");

        phraseGrid.getListDataView().refreshAll();
    }

    private void listenEditorCancel(EditorCancelEvent<Phrase> event){
        localPhraseValidationMessage.setText("");
        foreignPhraseValidationMessage.setText("");
        examValidationMessage.setText("");
    }

    private void prepareEditor(){
        editor.setBinder(binder);
        editor.setBuffered(true);
        editor.addCancelListener(this::listenEditorCancel);
    }

}
