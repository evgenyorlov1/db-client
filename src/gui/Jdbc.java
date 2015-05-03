/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author pc
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Jdbc {

    private static List<List<String>> rowData = new ArrayList<List<String>>();    
    
    
    public static Connection conn = null;
    public static Statement stmt = null;
    
    
    private static final String CreateDb = "CREATE DATABASE IF NOT EXISTS ORLOVEAPROJECT";
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost/";
    private static final String urlf = "jdbc:mysql://localhost/orloveaproject";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String CreateTb = "CREATE TABLE IF NOT EXISTS STUDENTS" + 
            "( id INTEGER NOT NULL AUTO_INCREMENT," + 
            "naame VARCHAR(70) NOT NULL," + 
            "grooup VARCHAR(10) NOT NULL," + 
            "birthdate DATE NOT NULL," +
            "address VARCHAR(50) NOT NULL,"+
            "number SMALLINT NOT NULL UNIQUE,"+ 
            "PRIMARY KEY(id))";
//    private static final String Insertion = "INSERT  INTO STUDENTS\n" +
//            "VALUES\n" +
//            "(1,'Evgeny Orlov','IM-21','1993-01-22','Kiev',1213),\n" +
//            "(2,'Orlov Evgeny','IM-22','1992-03-23','Kiev',1222),\n" +
//            "(3,'Orlov Orlov','IM-23','1992-04-21','Kiev',1224)";            
    
    
    private static String Insertion = "insert into orloveaproject.students (naame, grooup, birthdate, address, number) values (";
    private static String SearchDocument = "SELECT * FROM orloveaproject.STUDENTS WHERE id =";
    private static String SearchName = "SELECT * FROM orloveaproject.STUDENTS WHERE naame =";
    private static String Show = "SELECT * FROM orloveaproject.STUDENTS";
    private static String Delete = "DELETE FROM orloveaproject.STUDENTS WHERE number =";
    private static String Update = "UPDATE `orloveaproject`.`students` SET ";
    
    public static void update(String input, String id, String column) {
        
        try {                                               
            String str = Update + column + " = " + "'"+ input +"'" + " WHERE `id`= " + id;            
            stmt.executeUpdate(str);
        } catch (Exception e) {
            System.out.println("Error updating row: " + e);
            try {
                if(conn != null)
                    conn.close();
            } catch (Exception ed) {}
            try {
                if(stmt != null)
                    stmt.close();
            } catch(Exception ed) {}                    
        }
        
    }
    
    //done
    public static void delete(String id) {
        try {                        
            String str = Delete+id;
            stmt.executeUpdate(str);
        } catch (Exception e) {
            System.out.println("Error deleting row: " + e);
            try {
                if(conn != null)
                    conn.close();
            } catch (Exception ed) {}
            try {
                if(stmt != null)
                    stmt.close();
            } catch(Exception ed) {}                    
        }
    }
    
    //done
    public static void connection() {                        
        
        //Load driver
        try {
            Class.forName(driver);
        } catch(ClassNotFoundException e) {            
            System.out.println("Error loading driver: " + e);
            System.exit(0);
        }
        
        //Connect to Mysql
        try {            
            conn = DriverManager.getConnection(url, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Error connecting to database");
            System.exit(0);
        }                
        
        //Create Database and close connection
        try {
            stmt = conn.createStatement();          
            stmt.executeUpdate(CreateDb);            
        } catch(Exception e) {
            System.out.println("Error creating database: " + e);
        } finally {
            try {
                if (conn != null) 
                    conn.close();                 
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e);
            }
            try {
                if (stmt != null)				
                    stmt.close();
            } catch (SQLException e) {
                System.out.println("Error closing statement: " + e);
            }            
        }        
        
        //Creating new connection to my Database        
        try {
            conn = DriverManager.getConnection(urlf, USER, PASSWORD);
        } catch(Exception e) {
            System.out.println("Error connecting to databasef: " + e);
            System.exit(0);
        }         
        
        //Creating my table in my Database        
        try {
            stmt = conn.createStatement();                                  
            stmt.executeUpdate(CreateTb);            
        } catch(Exception e) {
            System.out.println("Error creating databasef: " + e);            
            try {
                if (conn != null)
                    conn.close();
            } catch(Exception ed) {}
            System.exit(0);            
        }         
    }
    
    //done
    public static void insert(String name, String group, String birth, String address, String doc) {
        try {            
            String str = Insertion+ "\""+ name+"\""+ ","+"\"" +group+"\""+","+"\""+birth+"\""+","+"\""+address+"\""+","+"\""+doc+"\""+");";
            stmt.executeUpdate(str);
            //stmt.executeUpdate("insert into orloveaproject.students (naame, grooup, birthdate, address, number) values('denis', 'IM-21', '1992-02-20', 'kiev', 1214);" );
        } catch (Exception e) {
            System.out.println("Error inserting to the table: " + e);
            try {
                if(conn != null)
                    conn.close();
            } catch (Exception ed) {}
            try {
                if(stmt != null)
                    stmt.close();
            } catch(Exception ed) {}            
        }
    }
    
    //done
    public static List search(String inputString) {        
        
        rowData.clear(); 
        ResultSet result;        
                        
        try {
            
            if(isNumeric(inputString)) {        
                result = stmt.executeQuery(SearchDocument+inputString);                                                                  
            }
            else {
                result = stmt.executeQuery(SearchName+inputString);                                                   
            }
            
            int i = 0;                                    
            while(result.next()) {                                                
                List<String> row = new ArrayList<String>();
                
                row.add(result.getString("id"));                                
                row.add(result.getString("naame"));
                row.add(result.getString("grooup"));
                row.add(result.getString("birthdate"));
                row.add(result.getString("address"));
                row.add(result.getString("number"));
                rowData.add(row);                
                i++;
            }
                        
        } catch (Exception e) {
            System.out.println("Error during executing search: " + e);
        }       
        return rowData;
    }   
    
    //done
    public static List show() {
        
        rowData.clear();        
        ResultSet result;
        try {
            result = stmt.executeQuery(Show);
            
            int i = 0;
            while(result.next()) {
                List<String> row = new ArrayList<String>();                
                row.add(result.getString("id"));                                
                row.add(result.getString("naame"));
                row.add(result.getString("grooup"));
                row.add(result.getString("birthdate"));
                row.add(result.getString("address"));
                row.add(result.getString("number"));
                rowData.add(row);                
                i++;
            }
            
        } catch(Exception e) {}
                
        return rowData;
    }
    
    //done
    public static boolean isNumeric(String str) {  
        try {  
            double d = Double.parseDouble(str);  
        }catch(NumberFormatException nfe) {  
            return false;  
        }  
        return true;  
    }
    
}   