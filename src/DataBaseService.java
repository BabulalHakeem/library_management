import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class DataBaseService {
    private Connection con;
    public DataBaseService(Connection con){
        this.con=con;
    }
    public boolean insertData(String sql,Object... obj){
        try(PreparedStatement ps=con.prepareStatement(sql)){
            for(int i=0;i<obj.length;i++){
                ps.setObject(i+1,obj[i]);
            }
            return ps.executeUpdate()>0;
        }catch(SQLException e){
            return false;
        }
    }

}
