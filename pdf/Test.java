package pdf;

import connectors.ConnectorMysql; 
import entities.*;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.sql.ResultSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.*;
/**
 * Write a description of class Test here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Test {
    ConnectorMysql cm = new ConnectorMysql("com.mysql.jdbc.Driver", "jdbc:mysql", "localhost:3306", "evaluaciones", "root", "masterkey");    
    private ArrayList<Temperature> t = new ArrayList<Temperature>();
    private ResultSet rs;
    private ConvertPDFGeneric pdfg;
    
    public Test() {
        try {
            cm.connect();        
            rs = cm.getRS(cm.getConn(), "SELECT *from mediciones.temperaturas");
            //rs = cm.getRS(cm.getConn(), "SELECT id_temp, celsius_temp  FROM MEDICIONES.TEMPERATURAS");
            pdfg = new ConvertPDFGeneric();   
            pdfg.convert(new File("asdl.pdf"), rs);
            rs.close();
        }catch(SQLException e){
            
        }
    }
    public static void main(String args[]) {
       //ConvertoPDF pdf = new ConvertoPDF();
        //pdf.convert(new File("asd.pdf"));
        new Test();
    }
}
