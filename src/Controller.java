import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.web.HTMLEditor;
import org.fxmisc.richtext.InlineCssTextArea;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Controller {
    @FXML private TabPane tabPane;
    @FXML private ScrollPane algPane;
    @FXML private Tab reqTab;
    @FXML private Tab testTab;
    @FXML private Tab algTab;

    //SOLUTION TAB

    //TEST TAB
    @FXML private Button backTestButton;
    @FXML private Button nextTestButton;
    @FXML private Group answerGroup;
    @FXML private Label questionLabel;

    //ALGORITHM TAB
    @FXML private Button addStep;
    @FXML private FlowPane stepPane;
    @FXML private MenuItem hyperLink;

    @FXML private InlineCssTextArea algTextArea;

    private int tabCounter = 0;
    private int rowCounter = 0;

    private void onAddStepButtonClick() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setId("comboBox_"+tabCounter);
        comboBox.setItems(FXCollections.observableArrayList(
                "1. Каждый бит важен",
                "2. C++ ",
                "3. Попитка не питка",
                "4. Играй по правилам"
                ));
        comboBox.setOnAction(event -> {
            String number = comboBox.getId().split("_")[1];
            FilteredList<Tab> filteredList = tabPane.getTabs().filtered(
                    tab -> tab.getId() != null && tab.getId().equals("tab_" + number)
            );
            if (filteredList.size() != 0) {
                filteredList.get(0).setText(comboBox.getValue());
            }
            else {
                Tab newTab = new Tab();
                newTab.setId("tab_" + number);
                System.out.println(newTab.getId());
                newTab.setText(comboBox.getValue());
                GridPane gridPane = new GridPane();
                gridPane.setGridLinesVisible(true);
                Button addInstrumentButton = new Button("+");
                addInstrumentButton.setOnAction(event1 -> {
                    ComboBox<String> instrumentComboBox = new ComboBox<>();
                    GridPane.setRowIndex(instrumentComboBox, gridPane.getRowCount() == 1 ? 1 : gridPane.getRowCount()+1);
                    instrumentComboBox.setId("instrumentComboBox" + tabCounter);
                    instrumentComboBox.setItems(FXCollections.observableArrayList(
                            "1. Сложение",
                            "4. Деление"));
                    instrumentComboBox.setOnAction(event2 -> {
                        try {
                            int rowComboBox = GridPane.getRowIndex(instrumentComboBox);
                            FilteredList<Node> instrumentList = gridPane.getChildren().filtered(child -> {
                                Integer rowInstrument = GridPane.getRowIndex(child);
                                return rowInstrument != null && rowInstrument == rowComboBox+1;
                            });
                            if (instrumentList.size() != 0) gridPane.getChildren().removeAll(instrumentList);
                            /*tabPane.getTabs().filtered(
                                tab -> tab.getId() != null && tab.getId().equals("tab_" + number)
                            );*/
                            String action = instrumentComboBox.getValue();
                            Pane pane = FXMLLoader.load(getClass().getResource(
                                action.equals("1. Сложение")    ?   "additionController.fxml"     :
                                action.equals("2. Вычитание")   ?   "subtractionController.fxml"  :
                                action.equals("3. Умножение")   ?   "multiplyController.fxml"     :
                                action.equals("4. Деление")     ?   "divisionController.fxml"     :
                                /*Контроллер текстового поля*/      "textFieldController.fxml"
                            ));
                            GridPane.setRowIndex(pane, rowComboBox+1);
                            gridPane.getChildren().add(pane);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    gridPane.getChildren().addAll(instrumentComboBox);
                });
                gridPane.getChildren().add(addInstrumentButton);
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(gridPane);
                newTab.setContent(scrollPane);
                tabPane.getTabs().add(newTab);
            }
        });
        Button delButton = new Button("X");
        delButton.setOnAction(event -> onDelButton(comboBox, delButton));
        stepPane.getChildren().remove(addStep);
        stepPane.getChildren().addAll(comboBox, delButton);
        stepPane.getChildren().add(addStep);
        rowCounter++; tabCounter++;
    }

    private void onDelButton(ComboBox<String> comboBox, Button delButton) {
        stepPane.getChildren().remove(comboBox);
        stepPane.getChildren().remove(delButton);
        String number = comboBox.getId().split("_")[1];
        System.out.println(number);
        FilteredList<Tab> filteredList = tabPane.getTabs().filtered(
                tab -> tab.getId() != null && tab.getId().equals("tab_" + number)
        );
        if (filteredList.size() != 0) {
            System.out.println("here");
            tabPane.getTabs().removeAll(filteredList.get(0));
        }
    }

    private void onNextTestButton(ListIterator<String> iterator) {
        backTestButton.setDisable(false);
        nextTestButton.setDisable(false);
        questionLabel.setText(iterator.next());
        GridPane gridPane = (GridPane) answerGroup.getChildren().get(0);
        for (int i = 0; i < 4; i++) {
            RadioButton radioButton = ((RadioButton) gridPane.getChildren().get(i));
            radioButton.setText(iterator.next());
        }
        iterator.next();
        if (!iterator.hasNext()) {
            nextTestButton.setDisable(true);
        };
        /*for (Node node: answerGroup.getChildren()) {
            RadioButton radioButton = (RadioButton) node;
            int length = radioButton.getText().length();
            int number = Character.getNumericValue(radioButton.getText().charAt(length-1)) + 1;
            radioButton.setText(radioButton.getText().substring(0, length-1) + number);
        }*/
    }
    private void onBackTestButton(ListIterator<String> iterator) {
        backTestButton.setDisable(false);
        nextTestButton.setDisable(false);
        System.out.println(iterator.previousIndex());
        for (int i = 0; i < 7; i++) iterator.previous();
        GridPane gridPane = (GridPane) answerGroup.getChildren().get(0);
        for (int i = 3; i >= 0; i--) {
            RadioButton radioButton = ((RadioButton) gridPane.getChildren().get(i));
            radioButton.setText(iterator.previous());
        }
        questionLabel.setText(iterator.previous());
        for (int i = 0; i < 6; i++) iterator.next();
        if (iterator.previousIndex() == 5) backTestButton.setDisable(true);

        /*for (Node node: answerGroup.getChildren()) {
            RadioButton radioButton = (RadioButton) node;
            int length = radioButton.getText().length();
            int number = Character.getNumericValue(radioButton.getText().charAt(length-1)) - 1;
            radioButton.setText(radioButton.getText().substring(0, length-1) + number);
        }*/
    }

    @FXML void initialize() {
        addStep.setOnAction(event -> onAddStepButtonClick());
        algTab.setDisable(true);
        try {
            testMethod();
        } catch (IOException e) {
            e.printStackTrace();
        }
        algTextArea = new InlineCssTextArea();
        algTextArea.setOnMouseReleased(event -> {
            algTextArea.getStyleRangeAtPosition(algTextArea.getCaretPosition());
        });
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Make Hyperlink");
        menuItem.setOnAction(event -> onHyperLink());
        contextMenu.getItems().add(menuItem);
        algTextArea.setContextMenu(contextMenu);
        stepPane.getChildren().add(algTextArea);
        //stepPane.getChildren().remove(addStep);
        //algPane.getChildren().add(addStep);
    }

    private void testMethod() throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(new FileReader("gavnovopros.txt"));
        ArrayList<String> arrayList = new ArrayList<>();
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            arrayList.add(s+"\n");
        }
        arrayList.forEach(System.out::print);
        ListIterator<String> iterator = arrayList.listIterator();
        questionLabel.setText(iterator.next());
        GridPane gridPane = (GridPane) answerGroup.getChildren().get(0);
        ToggleGroup toggleGroup = new ToggleGroup();
        for (int i = 0; i < 4; i++) {
            RadioButton answerRB = new RadioButton(iterator.next());
            answerRB.setToggleGroup(toggleGroup);
            answerRB.setPrefWidth(Region.USE_COMPUTED_SIZE);
            answerRB.setMaxWidth(Region.USE_PREF_SIZE);
            GridPane.setRowIndex(answerRB, i);
            gridPane.getChildren().add(answerRB);
        }
        iterator.next();
        backTestButton.setDisable(true);
        backTestButton.setOnAction(event -> onBackTestButton(iterator));
        nextTestButton.setOnAction(event -> onNextTestButton(iterator));
    }

    @FXML
    private void onEndTestButton() {
        testTab.setDisable(true);
        algTab.setDisable(false);
        tabPane.getSelectionModel().select(algTab);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Тестирование пройдено");
        alert.showAndWait();
    }

    @FXML
    private void onHyperLink() {
        System.out.println(algTextArea.getSelectedText());
        algTextArea.setStyle(   algTextArea.getSelection().getStart(),
                                algTextArea.getSelection().getEnd(),
                                "-fx-fill: blue; -fx-underline: true"   );
        algTextArea.setStyle(   algTextArea.getSelection().getEnd(),
                                algTextArea.getLength(),
                                ""                                      );
        /*System.out.println(textArea.getSelectedText());
        System.out.println(textArea.getSelectionBounds());*/
    }
}
