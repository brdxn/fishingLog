package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FishApp extends JFrame {
    
    FishApp() throws ClassNotFoundException {
  //Create the frame
  JFrame frame = new JFrame("Fishing Log Application");
  //Create label
  JLabel tab1Label = new JLabel("Log a fish you caught!", SwingConstants.CENTER);
  JLabel tab2Label = new JLabel("This is a directory of all the recorded catches.", SwingConstants.CENTER);
  JLabel tab3label = new JLabel("<html>I developed this application in order for fisherman like you and me<br/> to be able to log their catches and note down additional info of their trip. <br/>This application was created by Brendan Dill in 2022. <br/>Any questions or concerns contact <a href='brdxn.github.io'>here</a></html>", SwingConstants.CENTER);
  JLabel speciesLabel = new JLabel("Species: ");
  JLabel lengthLabel = new JLabel("Length (inches): ");
  JLabel weightLabel = new JLabel("Weight (pounds): ");
  JLabel dateLabel = new JLabel("Date caught: ");
  JLabel locationLabel = new JLabel("Location caught: ");
  JLabel additionalLabel = new JLabel("Additional Info: ");

  //Create panel 1
  JPanel p1 = new JPanel();

  //Create fields for panel 1
  JTextField speciesField = new JTextField("common name format",20);
  JTextField lengthField = new JTextField(5);
  JTextField weightField = new JTextField(6);
  JTextField dateField = new JTextField("mm-dd-yyyy format", 10);
  JTextField locationField = new JTextField("place, city, state format", 30);
  JTextArea additionalField = new JTextArea();

  //Get text from field
  Random id = new Random();  
  int fishID = id.nextInt(1000);

  //Create log button
  JButton logBtn = new JButton("Log catch!");

  //Create load button
  JButton loadBtn = new JButton("Load catches...");
  loadBtn.setBounds(50,50,50,30);

  //Set the layout for panel 1
  p1.setLayout(new GridLayout(0,1));
  //Add label in panel 1
  p1.add(tab1Label);
  p1.add(speciesLabel);
  p1.add(speciesField);
  p1.add(lengthLabel);
  p1.add(lengthField);
  p1.add(weightLabel);
  p1.add(weightField);
  p1.add(dateLabel);
  p1.add(dateField);
  p1.add(locationLabel);
  p1.add(locationField);
  p1.add(additionalLabel);
  p1.add(additionalField);
  p1.add(logBtn);

  //Create panel 2
  JPanel p2 = new JPanel();
  p2.setLayout(new GridLayout(0,1));
  p2.add(tab2Label);
  //p2.add(sp);
  p2.add(loadBtn);
  //Create panel 3
  JPanel p3 = new JPanel();
  p3.add(tab3label);

  
  //Create the tab container
  JTabbedPane tabs = new JTabbedPane();
  //Set tab container position
  tabs.setBounds(20,20,750,750);
  //Associate each panel with the corresponding tab
  tabs.add("Log Fish", p1);
  tabs.add("Directory", p2);
  tabs.add("About", p3);
  
  //Add tabs to the frame
  frame.add(tabs);
  
  frame.setSize(800,800);
  frame.setLayout(null);
  frame.setVisible(true);

  //Log catch into database 
    logBtn.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            String species = speciesField.getText();
            String length = lengthField.getText();
            String weight = weightField.getText();
            String date = dateField.getText();
            String location = locationField.getText();
            String additional = additionalField.getText();


           try {Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sys","root","fishing");
            String query = "INSERT INTO `FishEntry` (FishId, Species, Length, Weight, Date, Location, AdditionalInfo) VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
             preparedStmt.setInt (1, fishID);
             preparedStmt.setString(2, species);
             preparedStmt.setString(3, length);
             preparedStmt.setString(4, weight);
             preparedStmt.setString(5, date);
             preparedStmt.setString(6, location);
             preparedStmt.setString(7, additional);
             preparedStmt.executeUpdate();
        }
            catch (Exception d) {
            System.out.println("Error found "+ d);
        }

        
    }
    });

    //Load Catches from MySQL database
    loadBtn.addActionListener(new ActionListener(){



        @Override
        public void actionPerformed(ActionEvent e) {
            try {Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/sys","root","fishing");
            String query = "SELECT * FROM FishEntry;";
            Object rowData[][] = {{"Row1-Column1", "Row1-Column2", "Row1-Column3","Row1-Column4", "Row1-Column5", "Row1-Column6", "Row1-Column7"}};
            // array to hold column names
            Object columnNames[] = {"FishId", "Species", "Length", "Weight", "Date", "Location", "Additional Info"};
 
            // create a table model and table based on it
            DefaultTableModel mTableModel = new DefaultTableModel(rowData, columnNames);
            JTable table = new JTable(mTableModel);
 
 
            // make a statement with the server
            Statement stmt = conn.createStatement();
            // execute the query and return the result
            ResultSet rs = stmt.executeQuery(query);
 
            // create the gui
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane, BorderLayout.CENTER);
            //frame.setSize(300, 150);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
 
            // remove the temp row
            mTableModel.removeRow(0);
 
            // create a temporary object array to hold the result for each row
            Object[] rows;
            // for each row returned
            while (rs.next()) {
            // add the values to the temporary row
            rows = new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)};
            // add the temp row to the table
            mTableModel.addRow(rows);
        }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } finally {

        }}
    });

 }

 public static void main(String[] args) throws ClassNotFoundException 
 {
  new FishApp();
 }
}