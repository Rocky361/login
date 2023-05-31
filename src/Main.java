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

    // Declare variables for username, password, database URL, and credentials
    public static String username = "abc";
    public static String password = "abc";
    String url = "jdbc:mysql://localhost:3306/chatMember";
    String user = "root";
    String pass = "";

    @Override
    public void start(Stage primaryStage) {

        // Create the login pane and scene
        GridPane loginPane = createLoginPane(primaryStage);
        Scene loginScene = new Scene(loginPane);

        // Create the registration pane and scene
        GridPane registerPane = createRegisterGridPane(primaryStage, loginScene);
        Scene registerScene = new Scene(registerPane);



        // Set the stage to display the registration scene

        primaryStage.setScene(registerScene);
        primaryStage.show();
    }

    // Create the registration pane and return it
    public GridPane createRegisterGridPane(Stage primaryStage, Scene loginScene) {
        primaryStage.setTitle("Registrieren");
        // Create the registration form elements
        Button buttonRegister = new Button("Registrieren");
        Button buttonRegistriert = new Button("zum Login");
        Label regUsername = new Label("Name");
        Label regPassword = new Label("Password");
        TextField regUserTextfield = new TextField();
        PasswordField regPassTextfield = new PasswordField();

        // Create the registration pane and set its properties
        GridPane gridPaneRegister = new GridPane();
        gridPaneRegister.setMinSize(400, 200);
        gridPaneRegister.setHgap(5);
        gridPaneRegister.setVgap(5);
        gridPaneRegister.setAlignment(Pos.CENTER);

        // Add the registration form elements to the pane
        gridPaneRegister.add(regUsername, 0, 0);
        gridPaneRegister.add(regUserTextfield, 1, 0);
        gridPaneRegister.add(regPassword, 0, 1);
        gridPaneRegister.add(regPassTextfield, 1, 1);
        gridPaneRegister.add(buttonRegister, 0, 2);
        gridPaneRegister.add(buttonRegistriert, 1, 2);

        // Set action for the registration button click
        buttonRegister.setOnMouseClicked(e -> {
            // Get the username and password entered by the user
            username = regUserTextfield.getText();
            if (username.length() < 3){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Benutzername zu kurz! Mehr als 3 Zeichen!");
                alert.showAndWait();
                return;
            }
            password = regPassTextfield.getText();

            try {
                // Connect to the database
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement stm = con.createStatement();

                // Check if the username already exists in the database
                ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM tbl_member WHERE username = '" + username + "'");
                rs.next();
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("Benutzername bereits vorhanden!");
                    // Display an alert to inform the user that the username already exists
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Benutzername bereits vorhanden!");
                    alert.showAndWait();
                    return;
                } else {
                    // Insert the new user into the database
                    String abfrage = "INSERT INTO tbl_member(username, password) VALUES('" + username + "', '" + password + "')";
                    stm.executeUpdate(abfrage);
                    System.out.println("Erfolgreich registriert!");
                    // Display an alert to inform the user that registration was successful
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Erfolgreich registriert!");
                    alert.showAndWait();
                }

            } catch (SQLException f) {
                System.out.println(f.getMessage());
            }
            primaryStage.setTitle("Login");
            primaryStage.setScene(loginScene);
        });

        // Button click event handler for buttonRegistriert
        buttonRegistriert.setOnMouseClicked(e -> {
            primaryStage.setTitle("Login");
            primaryStage.setScene(loginScene);
        });

        return gridPaneRegister;
    }


    public GridPane createLoginPane(Stage primaryStage) {

        // Create UI elements
        Button submitButton = new Button("Submit");
        Button clearButton = new Button("Clear");
        Button zurueckButton = new Button("zurück");
        Label usernameLabel = new Label("Username");
        Label passwordLabel = new Label("Password");
        TextField usernameTF = new TextField();
        PasswordField passwordTF = new PasswordField();

        // Create grid panel
        GridPane gridPaneLogin = new GridPane();

        // Set size of grid panel
        gridPaneLogin.setMinSize(400, 200);

        // Set gap between grid elements
        gridPaneLogin.setHgap(5);
        gridPaneLogin.setVgap(5);

        // Set grid alignment
        gridPaneLogin.setAlignment(Pos.CENTER);

        // Add UI elements to grid panel
        gridPaneLogin.add(usernameLabel, 0, 0);
        gridPaneLogin.add(usernameTF, 1, 0);
        gridPaneLogin.add(passwordLabel, 0, 1);
        gridPaneLogin.add(passwordTF, 1, 1);
        gridPaneLogin.add(submitButton, 0, 2);
        gridPaneLogin.add(clearButton, 1, 2);
        gridPaneLogin.add(zurueckButton, 2, 2);

        // Event handler for submit button
        submitButton.setOnMouseClicked(f -> {
            // Get username from text field
            System.out.println(usernameTF.getText());
            try {
                // Connect to database
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement stm = con.createStatement();

                // Check if username and password are valid
                ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM tbl_member WHERE username = '" + usernameTF.getText() + "' AND password = '" + passwordTF.getText() + "'");
                rs.next();
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("Login successful!");
                    // Display an alert to inform the user that login was successful
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Login successful!");
                    alert.showAndWait();
                } else {
                    System.out.println("Invalid username or password!");
                    // Display an alert to inform the user that the username or password is invalid
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid username or password!");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });

        // Event handler for clear button
        clearButton.setOnMouseClicked(f -> {
            // Clear the text fields
            usernameTF.clear();
            passwordTF.clear();
        });

        // Event handler for "zurück" button
        zurueckButton.setOnMouseClicked(f -> start(primaryStage));

        return gridPaneLogin;
    }

}