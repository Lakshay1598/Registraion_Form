import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DashBoard extends JFrame{

    private JPanel DashBoardPanel;
    private JLabel lbAdmin;
    private JButton ButtRegister;

    public DashBoard(){

        setTitle("DashBoard");
        setContentPane(DashBoardPanel);
        setMinimumSize(new Dimension(450,480));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegisteredUsers = connectToDatabase();
        if(hasRegisteredUsers){
            LoginForm loginForm = new LoginForm(this);
            User user  = loginForm.user;

            if(user != null){
                lbAdmin.setText("User : "+  user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else{
                dispose();
            }
        }
        else{
             RegistrationForm myForm = new RegistrationForm(this);
             User user = myForm.user;

            if(user != null){
                lbAdmin.setText("User : "+  user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else{
                dispose();
            }
        }
        ButtRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashBoard.this);
                User user = registrationForm.user;

                if(user != null){
                    JOptionPane.showMessageDialog(DashBoard.this,
                            "New User :" + user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;

        final String SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhost:3306/mystore";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS");

            st.close();
            conn.close();

            conn = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
            st = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users ("+
                    "id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,"+
                    "name VARCHAR(200) NOT NULL,"+
                    "email VARCHAR(200) NOT NULL UNIQUE,"+
                    "phone VARCHAR(200) ,"+
                    "address VARCHAR(200) ,"+
                    "password VARCHAR(200) NOT NULL" +")";
            st.executeUpdate(sql);

            //CHECK IF WE HAVE USERS IN THE TABLE USERS
            st = conn.createStatement();
            ResultSet resultSet = st.executeQuery("SELECT COUNT(*) FROM users");

            if(resultSet.next()){
                int numUsers = resultSet.getInt(1);
                if(numUsers > 0){
                    hasRegisteredUsers = true;
                }
            }
            st.close();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return hasRegisteredUsers;
    }

    public static void main(String[] args) {
        DashBoard dashBoard = new DashBoard();

    }
}
