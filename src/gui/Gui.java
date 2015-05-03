/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;




/**
 *
 * @author Evgeny Orlov, IM-21 FBMI
 */
public class Gui extends JFrame{        

    private JTabbedPane tabbedPane;
    private JLabel label1 = new JLabel("Ooops, nothing found");
    
    private JLabel leftLabel = new JLabel("Left");
    private JLabel rightLabel = new JLabel("First");
    
    private JPanel Add;
    private JPanel Search;
    private JPanel Delete;
    private JPanel Show;
    private JPanel Update;
    
    private JTable table;
           
    public Gui() {
        setTitle( "Tabbed Pane Application" );
        setSize( 600, 400 );
	setBackground( Color.gray );
        
        JPanel topPanel = new JPanel();
	topPanel.setLayout( new BorderLayout() );
	getContentPane().add( topPanel );
        
        createAdd();
        createSearch();
        createDelete();
        createShow();
        createUpdate();
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Search", Search);
        tabbedPane.addTab("Add", Add);
        tabbedPane.addTab("Delete", Delete);
        tabbedPane.addTab("Show", Show);
        tabbedPane.addTab("Update", Update);
        topPanel.add(tabbedPane, BorderLayout.CENTER);        
    }

    
/*Done*/public void createUpdate() {
        Update = new JPanel();
        Update.setLayout(new BoxLayout(Update, BoxLayout.Y_AXIS));
        
        JLabel label = new JLabel("Update colums: ");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        Update.add(label);
        
        List<List<Object>> rowData;
        rowData = new ArrayList<List<Object>>();            
        
        Object[] headers = { "ID", "Name", "Group", "Birthdate", "Address", "Document" };                          
        String[] db = {"id", "naame", "grooup", "birthdate", "address", "number"};
        
        rowData = Show();        
        Object[][] Data = new Object[rowData.size()][6]; //Fine
        
        for(int i=0;i<rowData.size();i++) {            
            for(int j=0;j<6;j++) {
                Data[i][j]=rowData.get(i).get(j);
            }
        }        
        JTable table = new JTable(Data, headers);
        
        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int column = e.getColumn();
                int row = e.getFirstRow();
                String data = (table.getModel().getValueAt(row, column)).toString();
                String id = (table.getModel().getValueAt(row, 0)).toString();                                
                Update(data, id, db[column]);
                repaint();
            }
        }); 
        
        table.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        Update.add(table);
        
    }
    
/*Done*/public void Update(String input, String id, String column) {
        
        Jdbc myJdbc = new Jdbc();
        myJdbc.connection();
        myJdbc.update(input, id, column);
        try {
            myJdbc.conn.close();
            myJdbc.stmt.close();
        } catch(Exception e) {}
        
    }
    
/*Done*/public void createAdd() {               
        
        Add = new JPanel();
        Add.setLayout(new BoxLayout(Add, BoxLayout.Y_AXIS));
        
        JLabel Lname = new JLabel("Enter name: ");
        JTextField Tname = new JTextField();
        Add.add(Lname);
        Add.add(Tname);        
        
        JLabel Lgroup = new JLabel("Enter student's group: ");
        JTextField Tgroup = new JTextField();
        Add.add(Lgroup);
        Add.add(Tgroup);
        
        JLabel Lbirth = new JLabel("Enter student's birthdate: ");
        JTextField Tbirth = new JTextField();
        Add.add(Lbirth);
        Add.add(Tbirth);
        
        JLabel Laddress = new JLabel("Enter student's address: ");
    
        JTextField Taddress = new JTextField();
        Add.add(Laddress);
        Add.add(Taddress);
        
        JLabel Ldoc = new JLabel("Enter student's document: ");
        JTextField Tdoc = new JTextField();
        Add.add(Ldoc);
        Add.add(Tdoc);
        
        JButton button = new JButton("Submit");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String name = Tname.getText();
                String group = Tgroup.getText();
                String birth = Tbirth.getText();
                String address = Taddress.getText();
                String doc = Tdoc.getText();                
                Add(name, group, birth, address, doc);
                Tname.setText("");
                Tgroup.setText("");
                Tbirth.setText("");
                Taddress.setText("");
                Tdoc.setText("");
            }
        });
        Add.add(button);
    }
    
/*Done*/public void Add(String name, String group, String birth, String address, String doc) {
        
        Jdbc myJdbc = new Jdbc();
        myJdbc.connection();
        myJdbc.insert(name, group, birth, address, doc);
        try {
            myJdbc.conn.close();
            myJdbc.stmt.close();
        } catch(Exception e) {}
        
    }
    
/*Done*/public void createSearch() {        
                
        Search = new JPanel();
        Search.setLayout(new FlowLayout(3));
        Search.setPreferredSize(new Dimension(400, 400));        
        
        JLabel label = new JLabel("Enter student's name or id:");
        label.setBounds( 10, 15, 200, 20 );
        Search.add(label);                                               
        
        JTextField input = new JTextField(40);
        input.setText("Enter name or id");
        Search.add(input);
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {                
                final String inputString = input.getText();                
                if(inputString != null) {
                    List<List<Object>> rowData;
                    rowData = new ArrayList<List<Object>>();
                    rowData = Search(inputString);
                    //get Table META
                    Object[] headers = { "ID", "Name", "Gr", "Birth", "Add", "Doc" };
                    Object[][] Data = new Object[rowData.size()][6];
                    for(int i=0;i<rowData.size();i++) {
                        for(int j=0;j<rowData.get(i).size();j++) {
                            Data[i][j]=rowData.get(i).get(j);
                        }
                    }                            
                    table = new JTable(Data, headers);
                    Search.add(table);                            
                    validate();
                    repaint();
                }                
            }
        });                                                       
    }
    
/*Done*/public List Search(String inputString) {
        //Done
        List<List<Object>> rowData;
        rowData = new ArrayList<List<Object>>();    
        
        Jdbc myJdbc = new Jdbc();
        myJdbc.connection();
        rowData = myJdbc.search(inputString);  
        try {
            myJdbc.conn.close();
            myJdbc.stmt.close();
        } catch(Exception e) {}
        
        
        return rowData;
    }
    
/*Done*/public void createDelete() {
        
        Delete = new JPanel();
        Delete.setLayout(new BoxLayout(Delete, BoxLayout.Y_AXIS));
        
        JLabel label = new JLabel("Enter student's id: ");
        Delete.add(label);
        
        JTextField input = new JTextField();
        Delete.add(input);
        
        JButton button = new JButton("Delete");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(input.getText() != "") {
                    String id = input.getText();
                    Delete(id);
                    input.setText("");
                    //Show.revalidate();
                    //Show.repaint();
                }
            }
        });
        Delete.add(button);        
                
    } 

/*Done*/public void Delete(String id) {
        Jdbc myJdbc = new Jdbc();
        myJdbc.connection();
        myJdbc.delete(id);
        try {
            myJdbc.conn.close();
            myJdbc.stmt.close();
        } catch(Exception e) {}
    }
    
/*Done*/public void createShow() {        
        //Done       
        Show = new JPanel();
        Show.setLayout(new BoxLayout(Show, BoxLayout.Y_AXIS));        
        
        JLabel label = new JLabel("Database results:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        Show.add(label);
        
        List<List<Object>> rowData;
        rowData = new ArrayList<List<Object>>();            
        Object[] headers = { "ID", "Name", "Group", "Birthdate", "Address", "Document" };                  
        rowData = Show();        
        Object[][] Data = new Object[rowData.size()][6]; //Fine
        for(int i=0;i<rowData.size();i++) {            
            for(int j=0;j<6;j++) {
                Data[i][j]=rowData.get(i).get(j);
            }
        }
                                
        table = new JTable(Data, headers);
        table.setAlignmentX(Component.LEFT_ALIGNMENT);
        Show.add(table);        
        
    }
    
/*Done*/public List Show() {
        //Done
        List<List<Object>> rowData;
        rowData = new ArrayList<List<Object>>();    
        
        Jdbc myJdbc = new Jdbc();
        myJdbc.connection();
        //Jdbc.insert(); //delete after checking
        rowData = myJdbc.show();        
        try {
            myJdbc.conn.close();
            myJdbc.stmt.close();
        } catch(Exception e) {}
        
        return rowData;
    }
    
    public static void main(String[] args) {
                
        Gui gui = new Gui();
        gui.setVisible(true);        
        
        gui.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);                
            }
        });
        
    }
    
}