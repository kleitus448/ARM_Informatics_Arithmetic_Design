import javafx.css.Style;
import javafx.css.StyleClass;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

public class Elements {

    public static TextField getNumberField() {
        int width = 25;
        int height = 25;
        TextField textField = new TextField();
        textField.setPrefSize(width, height);
        textField.setMaxSize(width, height);
        textField.setStyle("-fx-background-color: rgba(0,0,0,0); " +
                "-fx-border-color: rgba(101,101,101,0.8)");
        textField.setOnKeyTyped(event -> limitChar(textField));
        return textField;
    }

    public static TextField getPerenosField() {
        TextField textField = getNumberField();
        textField.setStyle("-fx-background-color: rgba(0,0,0,0); " +
                "-fx-border-color: rgba(128,128,128,0.2)");
        textField.setOnKeyTyped(event -> limitChar(textField));
        return textField;
    }

    public static Line getLine() {
        Line line = new Line();
        line.setStartX(0); line.setEndX(25);
        return line;
    }

    private static void limitChar(TextField textField) {
        String text = textField.getText();
        if (text.length() > 1) {
            textField.setText(Character.toString(text.charAt(0)));
            textField.positionCaret(text.length());
        }
        else {
            GridPane gridPane = ((GridPane) textField.getParent());
            int rowCount = gridPane.getRowCount();
            int columnCount = gridPane.getColumnCount();
            int row = GridPane.getRowIndex(textField);
            int column = GridPane.getColumnIndex(textField);
            System.out.println(("\nrowCount = " + rowCount));
            System.out.println(("columnCount = " + columnCount));
            System.out.println(("row = " + row));
            System.out.println(("column = " + column + "\n"));
            gridPane.getChildren().filtered(child -> {
                Integer tRow = GridPane.getRowIndex(child);
                Integer tColumn = GridPane.getColumnIndex(child);
                return tRow != null && tColumn != null
                        && tRow == ((column == columnCount - 1 && row < rowCount-1) ?
                        (row + (row == rowCount - 3 ? 2 : 1)) : row)
                        && tColumn == (row == rowCount - 3 && column == columnCount - 1 || column == 1 ?
                        column : ((column == columnCount - 1 && row < rowCount - 3) ?
                        2 : (row == rowCount-1 ? column - 1 : column + 1)));
            }).get(0).requestFocus();
        }
    }
}

