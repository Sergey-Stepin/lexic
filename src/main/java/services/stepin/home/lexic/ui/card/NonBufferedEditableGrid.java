package services.stepin.home.lexic.ui.card;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//
//public class NonBufferedEditableGrid extends VerticalLayout {
//
//    ValidationMessage firstNameValidationMessage = new ValidationMessage();
//    ValidationMessage lastNameValidationMessage = new ValidationMessage();
//    ValidationMessage emailValidationMessage = new ValidationMessage();
//
//    public NonBufferedEditableGrid() {
//
//
//        Grid<Person> grid = new Grid<>(Person.class, false);
//        Grid.Column<Person> firstNameColumn = grid
//                .addColumn(Person::getFirstName).setHeader("First name")
//                .setWidth("120px").setFlexGrow(0);
//        Grid.Column<Person> lastNameColumn = grid.addColumn(Person::getLastName)
//                .setHeader("Last name").setWidth("120px").setFlexGrow(0);
//        Grid.Column<Person> emailColumn = grid.addColumn(Person::getEmail)
//                .setHeader("Email");
//
//        Binder<Person> binder = new Binder<>(Person.class);
//        Editor<Person> editor = grid.getEditor();
//        editor.setBinder(binder);
//
//        TextField firstNameField = new TextField();
//        firstNameField.setWidthFull();
//        addCloseHandler(firstNameField, editor);
//        binder.forField(firstNameField)
//                .asRequired("First name must not be empty")
//                .withStatusLabel(firstNameValidationMessage)
//                .bind(Person::getFirstName, Person::setFirstName);
//        firstNameColumn.setEditorComponent(firstNameField);
//
//        TextField lastNameField = new TextField();
//        lastNameField.setWidthFull();
//        addCloseHandler(lastNameField, editor);
//        binder.forField(lastNameField).asRequired("Last name must not be empty")
//                .withStatusLabel(lastNameValidationMessage)
//                .bind(Person::getLastName, Person::setLastName);
//        lastNameColumn.setEditorComponent(lastNameField);
//
//        EmailField emailField = new EmailField();
//        emailField.setWidthFull();
//        addCloseHandler(emailField, editor);
//        binder.forField(emailField).asRequired("Email must not be empty")
//                .withValidator(
//                        new EmailValidator("Enter a valid email address"))
//                .withStatusLabel(emailValidationMessage)
//                .bind(Person::getEmail, Person::setEmail);
//        emailColumn.setEditorComponent(emailField);
//
//        grid.addItemDoubleClickListener(e -> {
//            editor.editItem(e.getItem());
//            Component editorComponent = e.getColumn().getEditorComponent();
//            if (editorComponent instanceof Focusable) {
//                ((Focusable) editorComponent).focus();
//            }
//        });
//
//        editor.addCancelListener(e -> {
//            firstNameValidationMessage.setText("");
//            lastNameValidationMessage.setText("");
//            emailValidationMessage.setText("");
//        });
//
//        List<Person> people = DataService.getPeople();
//        grid.setItems(people);
//
//        getThemeList().clear();
//        getThemeList().add("spacing-s");
//        add(grid, firstNameValidationMessage, lastNameValidationMessage,
//                emailValidationMessage);
//    }
//
//    private static void addCloseHandler(Component textField,
//                                        Editor<Person> editor) {
//        textField.getElement().addEventListener("keydown", e -> editor.cancel())
//                .setFilter("event.code === 'Escape'");
//    }
//}
