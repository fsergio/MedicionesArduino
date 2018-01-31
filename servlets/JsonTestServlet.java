package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.*;
import com.google.gson.*;
import java.io.InputStream;
import entities.*;
import java.text.*;
import connectors.*;

/**
 * Clase que devuelve a traves de una comunicacion con la base de datos,
 * todos los registros de las temperaturas, como objeto JSON.
 * <p>
 * <li>Para realizar la consulta de uno o mas registros. previamente debe haber
 * una conexion con la base de datos.
 * <li>La instancia de {@link JettyServer} se encarga de la conexion, en esta aplicacion.
 * <li>Si se genera la consulta/operacion correctamente,el metodo {@link processRequest} puede devolver
 * en response, un objeto de tipo JSON con los registros.
 * <li>Debe haber creado una sesion en el servidor, antes de poder hacer algun tipo de peticion por parte del cliente.
 * <li>Debe tener instalado libreria GSON 2.2.4
 * </u1>
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 */
@WebServlet(name = "JsonTestServlet", urlPatterns = {"/listMediciones"})
public class JsonTestServlet extends HttpServlet {
    private ConnectorMysql cm = new ConnectorMysql();
    // a custom gson.
    private final static GsonBuilder gsonBuilder = new GsonBuilder();
    private static Gson cgson = new Gson();
    private Map<String,String> map = new HashMap<String,String>();    
    
   static {
        //gsonBuilder.registerTypeAdapter(entities.Temperature.class, new STemperature());//registering a new serializer for Temperature class{aun sin uso, se utiliza ResultSetJson}
        gsonBuilder.setPrettyPrinting();//for view in the format json.
        cgson = gsonBuilder.create();
    }
    
    
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
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);//sino estaba logeado, lo redireccino a login. solo desde alli se generan permisos.      
        if(session != null) {
            if(session.getAttribute("usr") != null) {
                    ResultSet rs = cm.getRS(cm.getConn(), "SELECT id_temp, celsius_temp , fah_temp, fecha_temp FROM MEDICIONES.TEMPERATURAS");
                    String json = cgson.toJson(ResultSetJson.toJson(rs));//obtaining json object from resultset java(see ResultSetJson class)                
                    //System.out.println(json);//printing console with json object
                    out.write(json);
            }else {
               //out.write("{\"Result\": \"FAIL\"}"); 
                map.put("Result","FAIL");
                out.write(cgson.toJson(map));                
            }
        }else {//no estaba logeado, direccionar a login
            System.out.println("primero logeate...");
            map.put("Msj","Usuario no autentificado");
            map.put("Result","FAIL");
            out.write(cgson.toJson(map));            
            //out.write("{\"Result\": \"FAIL\"}");          
        }
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
