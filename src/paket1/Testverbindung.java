package paket1;
import java.sql.*;

public class Testverbindung {

    public static void main(String[] args){

        String url = "jdbc:mysql://localhost:3306/chatMember";
        String user = "root";
        String pass = "";

        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            Statement stm = con.createStatement();
            String abfrage = "SELECT * FROM tbl_member";
            ResultSet rs = stm.executeQuery(abfrage);

            while(rs.next()){
                System.out.println(rs.getString(2) + " " +
                        rs.getString(3));
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}