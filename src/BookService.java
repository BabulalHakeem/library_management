import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;
public class BookService {
    private final Scanner sc=new Scanner(System.in);
    private Connection con=null;
    private String db=null;
    private  PreparedStatement ps=null;
    private ResultSet rs=null;
    private DataBaseService ds=null;
    private ValidationService vs=null;
    private StaffService ss=null;
    public BookService(Connection con,String db){
        this.con=con;
        this.db=db;
        ds=new DataBaseService(con);
        vs=new ValidationService(con,"bookdata");
    }
    public void addBook(){
        System.out.println("Enter the Book no :");
        int bno=sc.nextInt();
        sc.nextLine();
        int bpiece=0;
        if(!vs.doesBookdExist(bno)){
            System.out.println("Enter the book name :");
            String bname=sc.nextLine();
            System.out.println("Enter the author name :");
            String author=sc.nextLine();
            System.out.println("enter the count of books :");
            int bcount=sc.nextInt();
            int aval=1;
            String sql="insert into bookdata(b_code,book_name,author,piece,available) values(?,?,?,?,?);";
            if(ds.insertData(sql,bno,bname,author,bcount,aval)){
                System.out.println("Inserted successfully!!");
            }else{
                System.out.println("Invalid access in insertion!");
                ss.staffPage();
            }
        }
        else{
            System.out.println("You can able to update and reduce the book piece ");
            System.out.println("You can give the positive numbers for add and negative for reduce");
            bpiece=sc.nextInt();
            if(vs.updateBook(bno,bpiece)){
                System.out.println("Changed successfully");
            }
            else{
                System.out.println("Cannot able to change");
                ss.staffPage();
            }
        }
        vs.updateAvailablity(bno);
    }
    public void printBook(String sql){
        try{
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            int space[]={5,36,20,10,10};
            System.out.println("ID   |BOOK NAME                           |AUTHOR              |PIECE     |AVAILABLE |");
            while(rs.next()){
                int id=rs.getInt("b_code");
                String bname=rs.getString("book_name");
                String author=rs.getString("author");
                int piece=rs.getInt("piece");
                int aval=rs.getInt("available");
                printt(space,id,bname,author,piece,aval);
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n\n\n");

    }
    public void printUser(){
        String sql="select * from "+this.db;
        int space[]={5,27,18};
        try{
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            System.out.println("ID   |NAME                       |PHONE NUMBER      |");
            while(rs.next()){
                int id=rs.getInt("id_no");
                String sname=rs.getString("sname");
                long phone=rs.getLong("ph");
                printt(space,id,sname,phone);
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n\n\n");
    }
    private void printt(int[] space,Object... obj){
        System.out.println();
        for(int i=0;i<obj.length;i++){
            char[] ch=null;
            if(obj[i]==null){
                ch="  -  ".toCharArray();
            }
            else if(obj[i] instanceof String){
                ch=((String) obj[i]).toCharArray();
            }
            else if(obj[i] instanceof Integer||obj[i] instanceof Long){
                ch=String.valueOf(obj[i]).toCharArray();
            }
            char c=32;
            for(int j=0;j<space[i];j++){
                if(j<ch.length){
                    System.out.print(ch[j]);
                }else{
                    System.out.print(c);
                    if(j==space[i]-1){
                        System.out.print('|');
                    }
                }
            }
        }
    }
    public void removeBook(){
        System.out.println("Enter the Book no you going to remove : ");
        int id=sc.nextInt();
        if(vs.doesBookdExist(id)){
            System.out.println("Are you sure to delete ? (y/n)");
            char c=sc.next().charAt(0);
            if(c=='y'||c=='Y'){
                System.out.println("Deleting.......");
                vs.RemoveOperation(id);
                System.out.println("Deleted successfully");
            }
            else{
                System.out.println("Enter the proper choice");
            }}
        else{
            System.out.println("Id is not exist");
        }

    }
    public void searchBook(){
        System.out.println("1)search via book name\n2)search via author name\n3)back");
        int a=sc.nextInt();
        sc.nextLine();
        if(a==3){
           return;
        }
        else if(a>0&&a<3){
            String s1=a==1?"book_name":"author";
            System.out.println("search the name :");
            String se=sc.nextLine();
            String sql="select * from bookdata where "+s1+" like \'%"+se+"%\';";
            printBook(sql);

        }else{
            System.out.println("Give proper number");
            searchBook();
        }
    }
    public void orderBook(int uid){
        System.out.println("Enter the book code :");
        int bid=sc.nextInt();
        sc.nextLine();
        String sql="insert into register(id_no,sname,b_code,book_name,dateout,datein) values(?,?,?,?,?,?);";

        if(vs.doesBookdExist(bid)){
            if(vs.getPieceCount(bid)>0) {
                String bookname=vs.getName(bid);
                String username=new ValidationService(con,"userdata").getName(uid);
                System.out.println("Processing.....");
                if(bookname!=null && username!=null) {
                    System.out.println("Enter the Book Receiving Date (dd-mm-yyyy)");
                    String dateout = sc.nextLine();
                    if (ds.insertData(sql, uid, username, bid, bookname, dateout, null)) {
                        System.out.println("Thankyou for receiving book");
                        vs.updateBook(bid, -1);
                        vs.updateAvailablity(bid);
                    } else {
                        System.out.println("Error occured in db transation");
                    }
                }
            }else{
                System.out.println("oop the book is not in a stock");
            }
        }else{
            System.out.println("Enter the proper book code");
        }
    }
    public void yourbooks(int uid){
        String sql="select b_code,book_name,dateout from register where id_no="+uid+" and returned=0;";
        int[] space={6,36,12};
        try{
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            System.out.println("bcode |book name                           |dateout    |");
            while(rs.next()){
                int bid=rs.getInt("b_code");
                String bname=rs.getString("book_name");
                String dot=rs.getString("dateout");
                printt(space,bid,bname,dot);
            }
        }catch(SQLException e){
            System.out.println("Error occurd in aserver");
        }
        System.out.println();
    }
    public void submitbook(int uid){
        sc.nextLine();
        System.out.println("Please enter today date(dd-mm-yyyy)");
        String date=sc.nextLine();
        System.out.println("Enter the book number for submission: ");
        int bid=sc.nextInt();
        if(new ValidationService(con,"register").doesBookdExist(bid)&& !vs.isReturned(bid)){
            String sql="update register set datein=?,returned=1 where id_no=? and b_code=?;";
            try{
                ps=con.prepareStatement(sql);
                ps.setString(1,date);
                ps.setInt(2,uid);
                ps.setInt(3,bid);
                if(ps.executeUpdate() > 0){
                    vs.updateBook(bid, 1);
                    vs.updateAvailablity(bid);
                    System.out.println("Returned successfully!!");
                }
                else{
                    System.out.println("Problem in server");
                }
            }catch (SQLException e){
                System.out.println("Error in a submit service");
            }
        }else{
            System.out.println("Sorry you entered a wrong book number please check the yourbook for more detail !!");
        }
    }
    public void register(){
        String sql="select * from register;";
        try{
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            int[] space={5,27,6,38,12,12,11};
            System.out.println("ID   |    USER NAME              |BCODE |         BOOK NAME                   |  DATE OUT  |  DATE IN   | RETURNED |");
            while (rs.next()){
                int id=rs.getInt("id_no");
                String name=rs.getString("sname");
                int bid=rs.getInt("b_code");
                String bname=rs.getString("book_name");
                String dout=rs.getString("dateout");
                String din=rs.getString("datein");
                int re=rs.getInt("returned");
                printt(space,id,name,bid,bname,dout,din,re);
            }
        }catch (SQLException e){
            System.out.println("Error in register service");
        }
        System.out.println();
    }
}




