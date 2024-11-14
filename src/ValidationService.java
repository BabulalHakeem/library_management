import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
public class ValidationService {
    private Connection con=null;
    private PreparedStatement ps=null;
    private ResultSet rs=null;
    private Scanner sc=new Scanner(System.in);
    private String db=null;
    private DataBaseService ds=null;
    public ValidationService(Connection con,String db){
        this.con=con;
        this.db=db;
        ds=new DataBaseService(con);
    }

    public boolean doesIdExist(int id){
        String sql="select id_no from "+db+" where id_no=?;";
        ResultSet rs=null;
        boolean b=true;
        try{
            ps=con.prepareStatement(sql);
            ps.setInt(1,id);
            rs=ps.executeQuery();
            b=rs.next();
            rs.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
      return b;
    }
    public boolean pinCheck(int id,int pin){
        if(doesIdExist(id)){
            int org=0;
            String sql="select pin from "+db+" where id_no=?;";
            try {
                ps = con.prepareStatement(sql);
                ps.setInt(1,id);
                rs=ps.executeQuery();
                while(rs.next()){
                   org=rs.getInt("pin");
                }
                if(pin==org)
                    return true;
                else
                    return false;

            }catch(SQLException e){
                return false;
            }
        }else{
            System.out.println("Invalid access");
            return false;
        }
    }
    private boolean pinReset(int id,int newpin){
        String sql="update "+db+" set pin=? where id_no=?;";

        try(PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,newpin);
            ps.setInt(2,id);
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public void resetp(int id){
        System.out.println("Please enter the new pin :");
        int newpin=sc.nextInt();
        if(pinReset(id,newpin)){
            System.out.println("PIN Reseted successfully");
        }
        else{
            System.out.println("cannot able to change pin");
        }
    }
    public String getQuery(){
        return "select * from bookdata";
    }
    public boolean checkAcc(int id){
        if(!doesIdExist(id)) {
            System.out.println("Enter your name :");
            String name = sc.next();
            System.out.println("Please set the PIN :");
            int pin = sc.nextInt();
            System.out.println("Enter your contact number :");
            long contact = sc.nextLong();
            String sql = "insert into "+db+"(id_no,sname,pin,ph) values(?,?,?,?);";
            if (ds.insertData(sql, id, name, pin, contact))
                return true;
            else
                return false;

        }
        else return false;

    }
    public boolean doesBookdExist(int id){
        String sql="select b_code from "+db+" where b_code=?;";
        ResultSet rs;
        boolean b=true;
        try{
            ps=con.prepareStatement(sql);
            ps.setInt(1,id);
            rs=ps.executeQuery();
            b=rs.next();
            rs.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return b;
    }
    public boolean updateBook(int id,int bp){
        String sql2="update bookdata set piece=piece+? where b_code=?;";
        int org=0;
            try{
               org=getPieceCount(id);
                if((org>0)&&((bp>0 && org>=bp) ||(bp<0 && org>=(bp*-1)))){
                    ps=con.prepareStatement(sql2);
                    ps.setInt(1,bp);
                    ps.setInt(2,id);
                    return ps.executeUpdate()>0;
                }
                else return false;
            }catch(SQLException e){
                System.out.println(e.getMessage());
                return false;
            }
            finally {
                updateAvailablity(id);
            }
    }
    public int getPieceCount(int b_code){
        String sql1="select piece from bookdata where b_code=?;";
        int org=0;
        try {
            ps = con.prepareStatement(sql1);
            ps.setInt(1, b_code);
            rs = ps.executeQuery();
            while (rs.next()) {
                org = rs.getInt("piece");
            }
            return org;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return org;
        }
    }
    public boolean updateAvailablity(int book_id){
        String sql="update bookdata set available=? where b_code=?;";
        if(doesBookdExist(book_id)) {
            int org = getPieceCount(book_id);
            try{
                ps=con.prepareStatement(sql);
                if(org>0) ps.setInt(1,1);
                else ps.setInt(1,0);
                ps.setInt(2,book_id);
                return ps.executeUpdate()>0;
            }catch(SQLException e){
                System.out.println(e.getMessage());
                return false;
            }

        }else {
            System.out.println("Book not exist");
            return false;
        }
    }
    public boolean RemoveOperation(int id){
        String s2=this.db=="bookdata"?"b_code":"id_no";
        String sql="delete from "+this.db+" where "+s2+"=?;";
        try{
            ps=con.prepareStatement(sql);
            ps.setInt(1,id);
            return ps.executeUpdate()>0;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public String getName(int id){
        String s1=this.db=="bookdata"?"book_name":"sname";
        String s2=this.db=="bookdata"?"b_code":"id_no";
        String s3=null;
        String sql="select "+s1+" from "+this.db+" where "+s2+"="+id+";";
        try{
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                s3=rs.getString(s1);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return s3;
    }
    public boolean isReturned(int bid){
        String sql="select returned from register where b_code=?;";
        try{
            ps=con.prepareStatement(sql);
            ps.setInt(1,bid);
            rs= ps.executeQuery();
            rs.next();
            if(rs.getInt("returned")==1){
                return true;
            }
        }catch(SQLException e){
            System.out.println("Error in returned service");

        }
        return false;
    }
}
