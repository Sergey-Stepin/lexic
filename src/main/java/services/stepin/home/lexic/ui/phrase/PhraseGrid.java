package services.stepin.home.lexic.ui.phrase;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import services.stepin.home.lexic.model.Phrase;
import services.stepin.home.lexic.ui.person.DataService;
import services.stepin.home.lexic.ui.person.ValidationMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.vaadin.flow.component.grid.Grid.SelectionMode.SINGLE;


public class PhraseGrid extends VerticalLayout {

    private final Grid<Phrase> grid = new Grid<>(Phrase.class, false);
    private final Editor<Phrase> editor = grid.getEditor();
    private final Binder<Phrase> binder = new Binder<>(Phrase.class);
    private final ValidationMessage localPhraseValidationMessage = new ValidationMessage();
    private final ValidationMessage foreignPhraseValidationMessage = new ValidationMessage();
    private final ValidationMessage examValidationMessage = new ValidationMessage();

    public PhraseGrid() {

        Grid.Column<Phrase> localPhraseColumn = grid
                .addColumn(Phrase::getLocalPhrase)
                //.setHeader("Local")
                .setWidth("300px")
                .setFlexGrow(0);

        Grid.Column<Phrase> foreignPhraseColumn = grid
                .addColumn(Phrase::getForeignPhrase)
                //.setHeader("Foreig")
                .setWidth("300px")
                //.setFlexGrow(0)
                ;

        Grid.Column<Phrase> examColumn = grid
                .addColumn(Phrase::getPhraseExam)
                //.setHeader("Local")
                .setWidth("300px")
                //.setFlexGrow(0)
                ;

        Grid.Column<Phrase> actionsColumn = grid
                .addColumn(unused -> "")
                .setWidth("200px")
                .setFlexGrow(0);

        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField localPhraseField = new TextField();
        localPhraseField.setWidthFull();
        binder.forField(localPhraseField)
                .asRequired("Local Phrase must not be empty")
                .withStatusLabel(localPhraseValidationMessage)
                .bind(Phrase::getLocalPhrase, Phrase::setLocalPhrase);
        localPhraseColumn.setEditorComponent(localPhraseField);

        TextField foreignPhraseField = new TextField();
        foreignPhraseField.setWidthFull();
        binder.forField(foreignPhraseField).asRequired("Foreig Phrase must not be empty")
                .withStatusLabel(foreignPhraseValidationMessage)
                .bind(Phrase::getForeignPhrase, Phrase::setForeignPhrase);
        foreignPhraseColumn.setEditorComponent(foreignPhraseField);

        TextField examField = new TextField();
        examField.setWidthFull();
        binder.forField(examField)
                .withStatusLabel(examValidationMessage)
                .bind(Phrase::getPhraseExam, Phrase::setPhraseExam);
        examColumn.setEditorComponent(examField);


        grid.asSingleSelect().addValueChangeListener(event -> {

            System.out.println("### editor.isOpen()=" + editor.isOpen());

            Phrase phrase = event.getValue();
            if (phrase != null) {
                editor.editItem(phrase);
            }

            examField.setValue("");

            grid.getListDataView().refreshAll();
        });

        grid.setSelectionMode(SINGLE);
        //grid.asMultiSelect().setEnabled(false);
        grid.asSingleSelect().setRequiredIndicatorVisible(true);

        Button examButton = new Button("Check");
        examButton.addClickListener(e -> {

            TextField foreign = (TextField) foreignPhraseColumn.getEditorComponent();
            System.out.println("### foreign=" + foreign.getValue());

            TextField exam = (TextField) examColumn.getEditorComponent();
            System.out.println("### exam=" + exam.getValue());

            String currentForeignBackgroundColour = foreign.getStyle().get("background-color");

            if (foreign.getValue().trim().equals(exam.getValue().trim())) {
                System.out.println("### exam OK!");
                exam.getStyle().set("background-color", currentForeignBackgroundColour);
            } else {
                exam.getStyle().set("background-color", "#FFC0CB");
            }
        });

        Button removeButton = new Button("Remove");
        removeButton.addClickListener(event -> {
            Phrase selectedPhrase = grid.asSingleSelect().getValue();
            removePhrase(selectedPhrase);
        });

        HorizontalLayout editorActions = new HorizontalLayout(examButton, removeButton);
        editorActions.setPadding(false);
        actionsColumn.setEditorComponent(editorActions);

        editor.addCancelListener(e -> {

            System.out.println("### CANCEL:" + e.getItem());

            localPhraseValidationMessage.setText("");
            foreignPhraseValidationMessage.setText("");
            examValidationMessage.setText("");
        });

//        List<Phrase> phrases = new ArrayList<>();
//        grid.setItems(phrases);

        getThemeList().clear();
        getThemeList().add("spacing-s");
        add(grid, localPhraseValidationMessage, foreignPhraseValidationMessage, examValidationMessage);
    }

    public void addPhrase(Phrase phrase) {
        grid.asSingleSelect().clear();

        var listDataProvider = (ListDataProvider<Phrase>) grid.getDataProvider();
        listDataProvider.getItems().add(phrase);
        listDataProvider.refreshAll();

        grid.select(phrase);
    }

    public void removePhrase(Phrase phrase) {

        var listDataProvider = (ListDataProvider<Phrase>) grid.getDataProvider();
        listDataProvider.getItems().remove(phrase);
        listDataProvider.refreshAll();

        grid.asSingleSelect().clear();

    }

    public void setPhrases(Collection<Phrase> phrases) {
        ListDataProvider<Phrase> dataProvider = new ListDataProvider<>(phrases);
        grid.setDataProvider(dataProvider);
        dataProvider.refreshAll();
    }

    public Collection<Phrase> getPhrases() {
        editor.save();
        var listDataProvider = (ListDataProvider<Phrase>) grid.getDataProvider();
        return listDataProvider.getItems();
    }

}
