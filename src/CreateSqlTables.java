import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Tharald on 10/02/15.
 */
public class CreateSqlTables {

    public static void main(String[] args){
        CreateSqlTables obj = new CreateSqlTables();

        try {
            obj.updateTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTable() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/TestDB";
        Connection m_Connection = DriverManager.getConnection(url, "tharald", "putin");

        CreateGrid grid = new CreateGrid();
        for(Point2D p : grid.getKeys()){
            Point2D bl = grid.getPixelBr(p);
            StdOut.println(bl.toString());
            StdDraw.point(bl.x(),bl.y());
        }
        PreparedStatement total = m_Connection.prepareStatement("INSERT INTO table1 VALUES(?,?)");
        total.setInt(1, 3);
        total.setString(2, "JACOB");

        total.executeUpdate();

        m_Connection.close();
    }

}
