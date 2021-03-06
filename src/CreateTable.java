import java.sql.*;
import java.lang.*;

/**
 * Created by Tharald on 14/03/15.
 */
public class CreateTable {

    public CreateTable() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/Grid";
        Connection m_Connection = DriverManager.getConnection(url, "tharald", "putin");
        PreparedStatement total = m_Connection.prepareStatement("SELECT TABLE_NAME from information_schema.TABLES WHERE TABLE_SCHEMA LIKE ('Grid');");
        ResultSet result = total.executeQuery();

        while (result.next()){
            String state = result.getString(1);
            System.out.println(state);
            PreparedStatement get = m_Connection.prepareStatement("SELECT ID FROM Grid." + state +";");
            ResultSet ids = get.executeQuery();
            while (ids.next()){
                double id = ids.getDouble(1);
                PreparedStatement insert = m_Connection.prepareStatement("REPLACE INTO Global.Info VALUES(?,?)");
                insert.setDouble(1, id);
                insert.setString(2, state);
                insert.executeUpdate();
            }
        }
        m_Connection.close();
    }

    public static void main(String[] args){
        try {
            CreateTable go = new CreateTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}