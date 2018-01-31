package entities;

import java.io.*;
import com.google.gson.*;
import com.google.gson.stream.*;
import com.google.gson.reflect.*;
import java.util.*;
import java.lang.reflect.*;
import java.sql.*;
import java.math.*;
import java.sql.Timestamp;
/**
 * Serializacion de objeto ResultSet acorde con requerimientos de jtable.org
 * @author G. Cherencio 
 * <p>
 * <ul>
 * <li>Se ha modificado, para obtener valores de tipo timestamp, desde Mysql.
 * </ul>
 * @version 1.1
 */
public class ResultSetJson {

    /**
     * Retorna un objeto Json, previamente serializado desde un objeto java, con GSON.
     * @param rs Objeto ResultSet(con registros de alguna base de datos) a serializar.
     * @return JsonElement Retorna objeto java serializado.
     * @throws SQLException Lanza SQLException, si falla la consulta.
     */     
    public static JsonObject toJson(ResultSet rs) {
        JsonObject jo = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        long nr=0;
        try {
            ResultSetMetaData rsm = rs.getMetaData();
            int cols = rsm.getColumnCount();
            String[] ncols = new String[cols+1];
            for(int i=1;i<=cols;i++) ncols[i]=rsm.getColumnName(i).toLowerCase();
            while (rs.next()) {
                nr++;
                JsonObject obj = new JsonObject();
                for (int i = 1; i <= cols; i++) {
                    int colType = rsm.getColumnType(i);
                    switch(colType) {
                        case Types.BIGINT:
                            BigDecimal bd = rs.getBigDecimal(i);
                            if ( bd != null ) obj.addProperty(ncols[i],bd);
                            break;
                        case Types.REAL:
                        case Types.NUMERIC:
                        case Types.DOUBLE:
                        case Types.FLOAT:
                            Double dbl = rs.getDouble(i);
                            if ( dbl != null ) obj.addProperty(ncols[i],dbl);
                            break;
                        case Types.ROWID:
                        case Types.SMALLINT:
                        case Types.TINYINT:
                        case Types.BIT:
                        case Types.INTEGER:
                            Long lng = rs.getLong(i);
                            if ( lng != null ) obj.addProperty(ncols[i],lng);
                            break;
                        case Types.BOOLEAN:
                            Boolean bol = rs.getBoolean(i);
                            if ( bol != null ) obj.addProperty(ncols[i],bol);
                            break;
                        case Types.TIME:
                        case Types.TIME_WITH_TIMEZONE:
                        case Types.TIMESTAMP:
                        case Types.TIMESTAMP_WITH_TIMEZONE:
                        case Types.DATE:
                            Timestamp d = rs.getTimestamp(i);//convert datime(mysql) to timestamp
                            //System.out.println(d);
                            //if ( d != null ) obj.addProperty(ncols[i], "/Date("+d.getTime()+")/");
                            if ( d != null ) obj.addProperty(ncols[i], d.toString());
                            break;
                        case Types.LONGNVARCHAR:
                        case Types.LONGVARCHAR:
                        case Types.NCHAR:
                        case Types.NVARCHAR:
                        case Types.VARCHAR:
                        case Types.CHAR:
                            String str = rs.getString(i);
                            if ( str != null ) obj.addProperty(ncols[i],str);
                            break;
                        default:
                            String str2 = rs.getString(i);
                            if ( str2 != null ) obj.addProperty(ncols[i],str2);
                    }
                }
                jsonArray.add(obj);
            }
            //jo.addProperty("Result","OK");//con enviar "data" y el array de objetos json, basta para comunicacion con datatables.
            jo.add("data",jsonArray);
            
        } catch(SQLException sqle) {
            jo.addProperty("data","ERROR");
            jo.addProperty("Message",sqle.getMessage());
        }
        return jo;
    }
}
