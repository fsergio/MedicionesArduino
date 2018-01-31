package entities;

import java.util.*;
import java.sql.*;
/**
 * Write a description of class JTableData here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class JTableData<E> {
    private String Result = "OK";
    private List<E> Records = null;
    private String Message = null;
    private E Record = null;

    /**
     * Constructor for objects of class JTableData
     */
    public JTableData() {
        this("OK");
    }
    public JTableData(String r) {
        setResult(r);
    }
    public JTableData(String r,String m) {
        setResult(r);
        setMessage(m);
    }
    public JTableData(String r,List<E> e) {
        setResult(r);
        setRecords(e);
    }
    public JTableData(String r,E e) {
        setResult(r);
        setRecord(e);
    }
    // SETTERS
    public void setResult(String r) { 
        Result=r; 
        if ( r.equals("OK") ) setMessage(null);
        else {
            setRecords(null);
            setRecord(null);
        }
    }
    public void setRecords(List<E> e) { 
        Records=e;
        if ( e != null ) setRecord(null); 
    }
    public void setRecord(E e) { 
        Record=e;
        if ( e != null ) setRecords(null); 
    }
    public void setMessage(String m) { 
        Message=m;
        if ( m != null ) {
            if ( !getResult().equals("ERROR") ) setResult("ERROR");
            if ( getRecord() != null ) setRecord(null);
            if ( getRecords() != null ) setRecords(null);
        } else {
            if ( !getResult().equals("OK") ) setResult("OK");
        }
    }
    // GETTERS
    public String getResult() { return Result; }
    public List<E> getRecords() { return Records; }
    public E getRecord() { return Record; }
    public String getMessage() { return Message; }

}
