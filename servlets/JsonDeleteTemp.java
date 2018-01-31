package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.*;
import com.google.gson.*;
import java.io.InputStream;
import entities.*;
import java.text.*;
import connectors.*;
import java.util.Enumeration;
import javax.servlet.http.HttpSession;

/**
 * Clase que sirve para Eliminar uno o mas registros en una base de datos.
 * <p>
 * <u1>
 * <li>Para realizar la eliminacion de uno o mas registros. previamente debe haber
 * una conexion con la base de datos.
 * <li>La instancia de {@link JettyServer} se encarga de la conexion, en esta aplicacion.
 * <li>Si se genera la consulta/operacion correctamente,el metodo {@link processRequest} puede devolver
 * en response un objeto de tipo JSON con OK o FAIL.
 * <li>Debe haber creado una sesion en el servidor, antes de poder hacer algun tipo de peticion por parte del cliente.
 * <li>Debe tener instalado libreria GSON 2.2.4
 * </u1>
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 */
@WebServlet(name = "JsonDeleteTemp", urlPatterns = {"/deleteTemp"})
public class JsonDeleteTemp extends HttpServlet {
    private ConnectorMysql cm = new ConnectorMysql();
    private static Gson gson = new Gson();
    private Map<String,String> map = new HashMap<String,String>();    
    /**
     * Procesamiento de ambas peticiones HTTP
     * <code>GET</code> y
     * <code>POST</code> metodos.
     * @param request servlet request.
     * @param response servlet response.
     * @throws ServletException si un servlet en especifico falla.
     * @throws IOException si hubo algun tipo de error de I/O File.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("llamaste al servlet JsonDeleteTemp");               
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = null;       
        
        session = request.getSession(false);//sino estaba logeado, lo redireccino a login. solo desde alli se generan permisos.      
        if(session != null) {
                    // hacer query, cargar lista, pasar a json, agregar returns y records
                    String data = request.getParameter("rows");
                    String [] rows = data.split("/");
                    boolean flag = true;
                    for (int i=0; i<rows.length; i++){
                        if(rows[i] != "") {
                            cm.getQuery(cm.getConn(), "delete FROM temperaturas WHERE id_temp = "+rows[i]);
                        }else {//no se recibieron datos.
                         flag = false;   
                        }
                    }
                    if(flag) {
                        //out.write("{\"Result\":\"OK\"}");
                        map.put("Result","OK");
                        out.write(gson.toJson(map));                        
                    }else {
                        map.put("Result","FAIL");
                        out.write(gson.toJson(map));                        
                    }
         
                    //para test
                    /*Enumeration en =request.getParameterNames();
                    while(en.hasMoreElements()) {
                         Object objOri=en.nextElement();
                         String param=(String)objOri;
                         String value=request.getParameter(param);
                         System.out.println("Parameter Name is '"+param+"' and Parameter Value is '"+value+"'");
                    }*/
                    
        }else {
            System.out.println("desconocido, contactese con el administrador.");          
            map.put("Result","FAIL");
            System.out.println(gson.toJson(map));            
            out.write(gson.toJson(map));                         
        }
        out.close();
    }

    /*
     * No utilizar getParameter() antes de llamar a este metodo!
     * Si se obtiene toda la peticion enviada por el cliente, no puede 
     * usarse luego getParameter()
     */
    private String getStringFromRequest(HttpServletRequest request) {
        InputStream in;
        StringBuilder sb=new StringBuilder();
        int d;
        try {
            in=request.getInputStream();
            while((d=in.read()) != -1){
                sb.append((char)d);
            }
        }catch(IOException ioe) {
        }
        //TRACE
        System.out.println("JsonTestServlet getStringFromRequest="+sb.toString());
        return sb.toString();
    }

    /**
     * Handles de HTTP
     * <code>GET</code> metodo.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException si un servlet en especifico falla.
     * @throws IOException si hubo algun tipo de error de I/O File
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

     /**
     * Handles de HTTP
     * <code>POST</code> metodo.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException si un servlet en especifico falla.
     * @throws IOException si hubo algun tipo de error de I/O File
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    /**
     * Returns Una breve descripcion del servlet.
     *
     * @return un String que representa la informacion del servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
