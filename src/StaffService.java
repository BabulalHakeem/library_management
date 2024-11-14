import java.sql.Connection;
import java.util.Scanner;
public class StaffService   {
    private Scanner sc=new Scanner(System.in);
    private ValidationService vs=null;
    private BookService bs=null;
    private UserService us=null;
    private int id=0;
    public StaffService(Connection con){
        vs=new ValidationService(con,"staffdata");
        bs=new BookService(con,"staffdata");
        us=new UserService(con);
    }
    public void staffPage(){
        System.out.println("1)Add Staffs\n2)Add book\n3)Remove book\n4)Show users\n5)Remove user\n6)Book details\n7)staff details\n8)Register data\n9)Reset password \n10)exit");
        System.out.println("Enter your choice :");
        int num=sc.nextInt();
        switch (num){
            case 1:
                createAccount();
                break;
            case 2:
                bs.addBook();
                break;
            case 3:
                bs.removeBook();
                break;
            case 4:
                 us.printUser();
                break;
            case 5:
                removeUser();
                break;
            case 6:
                bs.printBook(vs.getQuery());
                break;
            case 7:
                 bs.printUser();
                 break;
            case 8:
                 bs.register();
                 break;
            case 9:
                vs.resetp(this.id);
                break;
            case 10:
                System.exit(1);
            default:
                System.out.println("Enter proper number!");
                break;
        }
    }
    public void staffLogin(){
        System.out.println("Enter the ID number :");
        int id=sc.nextInt();
        System.out.println("Enter the Pin number :");
        int pin=sc.nextInt();
        if(vs.pinCheck(id,pin)){
            this.id=id;
            while(true) {
                staffPage();
            }
        }else{
            System.out.println("Enter proper details");
            staffLogin();
        }
    }
    private void createAccount(){
        System.out.println("Enter new Staff id: ");
        int ac=sc.nextInt();
        if(vs.checkAcc(ac)){
            System.out.println("Inserted successfully");
        }
        else{
            System.out.println("this id number already exist please enter new number");
            createAccount();
        }

    }
    public void removeUser(){
        if(us.removeUser()){
            System.out.println("Deleted succesfully");
        }
        else{
            System.out.println("Try again we cant able to delete it");
        }
    }
}
