package services.stepin.home.lexic.ui.buffered;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import services.stepin.home.lexic.model.Phrase;
import services.stepin.home.lexic.ui.person.DataService;
import services.stepin.home.lexic.ui.person.ValidationMessage;

import java.util.List;

@Route("phrase")
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
                .setHeader("Local")
                .setWidth("300px")
                .setFlexGrow(0);

        Grid.Column<Phrase> foreignPhraseColumn = grid
                .addColumn(Phrase::getForeignPhrase)
                .setHeader("Foreig")
                .setWidth("300px")
                //.setFlexGrow(0)
                ;

        Grid.Column<Phrase> examColumn = grid
                .addColumn(Phrase::getPhraseExam)
                .setHeader("Local")
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
            if (editor.isOpen()) {
                editor.save();
                grid.getListDataView().refreshAll();
            }

            grid.getEditor().editItem(event.getValue());
            examField.setValue("");
        });

//        Button saveButton = new Button(
//                "Save",
//                e -> {
//                    editor.save();
//                    grid.getListDataView().refreshAll();
//                    editor.closeEditor();
//                });
//
//        Button cancelButton = new Button(
//                VaadinIcon.CLOSE.create(),
//                e -> editor.cancel());
//        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
//                ButtonVariant.LUMO_ERROR);

//        HorizontalLayout editorActions = new HorizontalLayout(saveButton, cancelButton);
//        editorActions.setPadding(false);
//        actionsColumn.setEditorComponent(editorActions);

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

        HorizontalLayout editorActions = new HorizontalLayout(examButton);
        editorActions.setPadding(false);
        actionsColumn.setEditorComponent(editorActions);

        editor.addCancelListener(e -> {
            localPhraseValidationMessage.setText("");
            foreignPhraseValidationMessage.setText("");
            examValidationMessage.setText("");
        });

        List<Phrase> people = DataService.getPhrases();
        grid.setItems(people);

        getThemeList().clear();
        getThemeList().add("spacing-s");
        add(grid, localPhraseValidationMessage, foreignPhraseValidationMessage, examValidationMessage);
    }

}
