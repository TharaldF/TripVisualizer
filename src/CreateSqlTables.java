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
        String url = "jdbc:mysql://127.0.0.1:3306/Grid";
        Connection m_Connection = DriverManager.getConnection(url, "tharald", "putin");
        StdOut.println("Connection Successful");

        CreateGrid grid = new CreateGrid();

        StdOut.println("Grid Created");


        for(String state: grid.getStates()) {
            for (double key : grid.getKeys(state)) {
                Point2D centroid = grid.getPixelCentroid(key);
                Point2D bl = grid.getPixelBl(key);
                Point2D br = grid.getPixelBr(key);
                Point2D tr = grid.getPixelTr(key);
                Point2D tl = grid.getPixelTl(key);
                int x = grid.getX(key);
                int y = grid.getX(key);

                PreparedStatement total = m_Connection.prepareStatement("REPLACE INTO "+ state + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");

                //ID
                total.setDouble(1, key);

                //X/Y
                total.setInt(2, x);
                total.setInt(3, y);

                //Centroid Long/Lat
                total.setDouble(4, centroid.x());
                total.setDouble(5, centroid.y());

                //Bottom Left Long/Lat
                total.setDouble(6, bl.x());
                total.setDouble(7, bl.y());

                //Bottom Right Long/Lat
                total.setDouble(8, br.x());
                total.setDouble(9, br.y());

                //Top Left Long/Lat
                total.setDouble(10, tl.x());
                total.setDouble(11, tl.y());

                //Top Right Long/Lat
                total.setDouble(12, tr.x());
                total.setDouble(13, tr.y());

                total.executeUpdate();

            }
        }


        m_Connection.close();
    }

}
