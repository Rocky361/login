import java.sql.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;




public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static String username = "abc";
    public static String password = "abc";
    String url = "jdbc:mysql://localhost:3306/chatMember";
    String user = "root";
    String pass = "";

    @Override
    public void start(Stage primaryStage) {

        GridPane loginPane = createLoginPane();
        Scene loginScene = new Scene(loginPane);

        GridPane registerPane = createRegisterGridPane(primaryStage, loginScene);
        Scene registerScene = new Scene(registerPane);

        primaryStage.setTitle("Registrieren");
        primaryStage.setScene(registerScene);
        primaryStage.show();
    }

    public GridPane createRegisterGridPane(Stage primaryStage, Scene loginScene) {
        Button buttonRegister = new Button("Registrieren");
        Button buttonRegistriert = new Button("Bin Registriert");
        Label regUsername = new Label("Name");
        Label regPassword = new Label("Password");
        TextField regUserTextfield = new TextField();
        TextField regPassTextfield = new TextField();

        GridPane gridPaneRegister = new GridPane();

        gridPaneRegister.setMinSize(400, 200);
        gridPaneRegister.setHgap(5);
        gridPaneRegister.setVgap(5);
        gridPaneRegister.setAlignment(Pos.CENTER);
        gridPaneRegister.add(regUsername, 0, 0);
        gridPaneRegister.add(regUserTextfield, 1, 0);
        gridPaneRegister.add(regPassword, 0, 1);
        gridPaneRegister.add(regPassTextfield, 1, 1);
        gridPaneRegister.add(buttonRegister, 0, 2);
        gridPaneRegister.add(buttonRegistriert, 1, 2);

        buttonRegister.setOnMouseClicked(e -> {
            username = regUserTextfield.getText();
            password = regPassTextfield.getText();
            try {
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement stm = con.createStatement();

                // Überprüfen, ob der Benutzername bereits vorhanden ist
                ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM tbl_member WHERE username = '" + username + "'");
                rs.next();
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("Benutzername bereits vorhanden!");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Benutzername bereits vorhanden!");
                    alert.showAndWait();
                    return;
                } else {
                    String abfrage = "INSERT INTO tbl_member(username, password) VALUES('" + username + "', '" + password + "')";
                    stm.executeUpdate(abfrage);
                    System.out.println("Erfolgreich registriert!");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Erfolgreich registriert!");
                    alert.showAndWait();
                }

            } catch (SQLException f) {
                System.out.println(f.getMessage());
            }

            primaryStage.setScene(loginScene);
        });

        buttonRegistriert.setOnMouseClicked(e -> {
            primaryStage.setScene(loginScene);
        });

        return gridPaneRegister;
    }


    public GridPane createLoginPane(){
        // LOGIN
        //objekte erstellen
        Button button1 = new Button("Submit");
        Button button2 = new Button("Clear");
        Label label1 = new Label("Username");
        Label label2 = new Label("Password");
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();

        //grid panel erstellen
        GridPane gridPaneLogin = new GridPane();

        //größe
        gridPaneLogin.setMinSize(400, 200);

        //horizontale und verticale gap zwischen den grid elementen
        gridPaneLogin.setHgap(5);
        gridPaneLogin.setVgap(5);

        //grid alignment
        gridPaneLogin.setAlignment(Pos.CENTER);

        // objekte ins grid einfügen
        gridPaneLogin.add(label1, 0, 0);
        gridPaneLogin.add(textField1, 1, 0);
        gridPaneLogin.add(label2, 0, 1);
        gridPaneLogin.add(textField2, 1, 1);
        gridPaneLogin.add(button1, 0, 2);
        gridPaneLogin.add(button2, 1, 2);

        button1.setOnMouseClicked(f -> {
            System.out.println(textField1.getText());
            try {
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement stm = con.createStatement();
                String abfrage = "SELECT * FROM tbl_member WHERE username='" + textField1.getText() + "'";
                ResultSet rs = stm.executeQuery(abfrage);
                if (rs.next()) {
                    if (rs.getString("password").equals(textField2.getText())) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Logged in!");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong Password");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Unknown User");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });

        button2.setOnMouseClicked(g -> {
            textField1.setText("");
            textField2.setText("");
        });

        return gridPaneLogin;
    }






}
