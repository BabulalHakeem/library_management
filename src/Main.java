import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
public class  Main {
    private ValidationService vs=null;
    private StaffService ss=null;
    private UserService us=null;
    public Main(Connection con){
        vs=new ValidationService(con,"userdata");
        ss=new StaffService(con);
        us=new UserService(con);
    }
    private Scanner sc=new Scanner(System.in);
    public void firstpage(){
        System.out.println(" 1)Create account \n 2)User login \n 3)Staff login \n 4)exit\n");
        System.out.println("enter your choice :  ");
        int num=sc.nextInt();
        switch (num){
            case 1:
                    createAccount();
                    break;
            case 2:
                    us.userLogin();
                    break;
            case 3:
                    ss.staffLogin();
                    break;
            case 4:
                System.exit(1);
            default:
                System.out.println("Please enter correct number ");
                firstpage();
        }
    }
    private void createAccount(){
        System.out.println("Enter your new account no: ");
        int id=sc.nextInt();
        if(vs.checkAcc(id)){
            System.out.println("Account created successfully!!!");
        }
        else{
            System.out.println("this id number already exist please enter new number");
            createAccount();
        }

    }
    public static void main(String[] args) {
        System.out.println("Welcome to library");
        Connection con=null;
        String username="root";
        String password="BabuDB@66";
        String url="jdbc:mysql://localhost:3306/library";
        try{
            con=DriverManager.getConnection(url,username,password);
            Main m=new Main(con);
            while(true) {
                m.firstpage();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try{
                if(con!=null) con.close();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
}