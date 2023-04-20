import java.sql.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
        PasswordField regPassTextfield = new PasswordField();


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
        Button submitButton = new Button("Submit");
        Button clearButton = new Button("Clear");
        Label usernameLabel = new Label("Username");
        Label passwordLabel = new Label("Password");
        TextField usernameTF = new TextField();
        PasswordField passwordTF = new PasswordField();

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
        gridPaneLogin.add(usernameLabel, 0, 0);
        gridPaneLogin.add(usernameTF, 1, 0);
        gridPaneLogin.add(passwordLabel, 0, 1);
        gridPaneLogin.add(passwordTF, 1, 1);
        gridPaneLogin.add(submitButton, 0, 2);
        gridPaneLogin.add(clearButton, 1, 2);

        submitButton.setOnMouseClicked(f -> {
            System.out.println(usernameTF.getText());
            try {
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement stm = con.createStatement();
                String abfrage = "SELECT * FROM tbl_member WHERE username='" + usernameTF.getText() + "'";
                ResultSet rs = stm.executeQuery(abfrage);
                if (rs.next()) {
                    if (rs.getString("password").equals(passwordTF.getText())) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Erfolgreich angemeldet!");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Benutzername oder Passwort falsch!");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Unbekannter Benutzername");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });

        clearButton.setOnMouseClicked(g -> {
            usernameTF.setText("");
            passwordTF.setText("");
        });

        return gridPaneLogin;
    }






}
