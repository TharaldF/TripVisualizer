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

        CreateGrid grid = new CreateGrid();

        PreparedStatement total = m_Connection.prepareStatement("INSERT INTO Pixels VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");

        for(int key : grid.getKeys()){
            Point2D centroid = grid.getPixelCentroid(key);
            Point2D bl = grid.getPixelBl(key);
            Point2D br = grid.getPixelBr(key);
            Point2D tr = grid.getPixelTr(key);
            Point2D tl = grid.getPixelTl(key);
            int x = grid.getX(key);
            int y = grid.getX(key);

            //ID
            total.setInt(1, key);

            //X/Y
            total.setInt(2, x);
            total.setInt(3, y);

            //Centroid Long/Lat
            total.setDouble(4,centroid.x());
            total.setDouble(5,centroid.y());

            //Bottom Left Long/Lat
            total.setDouble(6,bl.x());
            total.setDouble(7,bl.y());

            //Bottom Right Long/Lat
            total.setDouble(8,br.x());
            total.setDouble(9,br.y());

            //Top Left Long/Lat
            total.setDouble(10,tl.x());
            total.setDouble(11,tl.y());

            //Top Right Long/Lat
            total.setDouble(12,tr.x());
            total.setDouble(13,tr.y());

            total.executeUpdate();

        }


        m_Connection.close();
    }

}
