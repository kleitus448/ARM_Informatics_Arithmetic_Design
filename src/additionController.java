import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;

public class additionController {

    @FXML private AnchorPane anchorPane;
    @FXML private TextField numberTextEdit;
    @FXML private Button perenosButton;
    @FXML private GridPane addGrid;

    @FXML
    private void onNumberTextEdit(KeyEvent event)  {
        if (event.getCode().equals(KeyCode.ENTER)) {
            int number = Integer.parseInt(numberTextEdit.getText());
            if (number <= 20 && number > 0) {
                addGrid.setVisible(true);
                int lastColumn = addGrid.getColumnCount();
                int columns = Integer.parseInt(numberTextEdit.getText()) - lastColumn + 2;
                System.out.println("columns = " + columns);
                System.out.println("lastColumn = " + lastColumn);
                if (columns > 0) {
                    for (int i = 0; i < columns; i++) {
                        addGrid.getColumnConstraints().add(lastColumn + i, new ColumnConstraints());
                        for (int j = 2; j < addGrid.getRowCount(); j++) {
                            if (j != addGrid.getRowCount() - 2)
                                addGrid.add(Elements.getNumberField(), lastColumn + i, j);
                            else
                                addGrid.add(Elements.getLine(), lastColumn + i, j);
                        }
                    }
                    Object[] array = addGrid.getChildren().filtered(child -> {
                        Integer row = GridPane.getRowIndex(child);
                        return row != null && row == 1;
                    }).toArray();
                    if (array.length != 0) {
                        boolean flagVis = ((Region) array[0]).isVisible();
                        addPerenos();
                        if (!flagVis) deletePerenos();
                    }
                } else if (columns < 0)
                    for (int i = -1; i >= columns; i--) {
                        int finalI = i;

                        //УДАЛЯЕМ НАХУЙ ДЕТЕЙ БЕСПЛАТНО И БЕЗ ОПЕРАЦИЙ
                        addGrid.getChildren().removeIf(child -> {
                            System.out.println(child.getTypeSelector());
                            Integer column = GridPane.getColumnIndex(child);
                            return column != null && column == lastColumn + finalI;
                        });

                        //УДАЛЯЕМ НАХУЙ ЯЙЦЕКЛЕТКИ ДЕТЕЙ БЕСПЛАТНО И БЕЗ ОПЕРАЦИЙ
                        addGrid.getColumnConstraints().remove(lastColumn + finalI - 1);
                    }
                System.out.println("columnCount = " + addGrid.getColumnCount());
                //resize();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Зураб НЕ ДОВОЛЕН");
                alert.setHeaderText("Ошибка");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void addPerenos() {
        for (int i = 2; i < addGrid.getColumnCount(); i++) {
            int finalI = i;
            Object[] array = addGrid.getChildren().filtered(child -> {
                Integer row = GridPane.getRowIndex(child);
                Integer column = GridPane.getColumnIndex(child);
                return row != null && row == 1 && column != null && column == finalI;
            }).toArray();
            if (array.length == 0) addGrid.add(Elements.getPerenosField(), i, 1);
            else
                try {
                    ((Region) array[0]).setVisible(true);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        perenosButton.setOnAction(event -> deletePerenos());
        //resize();
    }

    private void deletePerenos() {
        addGrid.getChildren().filtered(child -> {
            Integer row = GridPane.getRowIndex(child);
            return row != null && row == 1;
        }).forEach(child -> child.setVisible(false));
        perenosButton.setOnAction(event -> addPerenos());
    }

    //TODO: Васянство с колёсиком от жиги Антика (изменять ширину, высоту, нисх. присваивание через регион)
    @FXML private void onScroll(ScrollEvent event) {
//        System.out.println(event.getDeltaY());
//        anchorPane.getChildren().forEach(child -> {
//            //(child.getScaleX() + (event.getDeltaY() > 0 ? 2 : -2));
//            //child.setScaleY(child.getScaleY() + (event.getDeltaY() > 0 ? 2 : -2));
//            ((Region) child).setPrefWidth(((Region) child).getWidth()*(event.getDeltaY() > 0 ? 1.1 : 0.9));
//            ((Region) child).setPrefHeight(((Region) child).getHeight()*(event.getDeltaY() > 0 ? 1.1 : 0.9));
//            //child.setLayoutX(0); child.setLayoutY(0);
//        });
   }
    @FXML private void initialize() {
    }

    private void resize() {
        Window window = anchorPane.getScene().getWindow();
        window.sizeToScene();
        window.setWidth(window.getWidth()*1.01);
        window.setHeight(window.getHeight()*1.05);
    }
}
