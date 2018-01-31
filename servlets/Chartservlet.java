package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.Timestamp;
//import org.json.JSONObject;
import java.sql.SQLException;

import com.google.gson.Gson;

import entities.*;
import connectors.*;
/**
 * Clase que devuelve a traves de una comunicacion con la base de datos,
 * todos los registros de las temperaturas, como objeto JSON. Implementando una nueva manera de traer los datos, y efectuar 
 * un Rsponse.
 * <p>
 * Utilizada por Graficos google Charts y JQuery. Para mas informacion https://developers.google.com/chart/
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
@WebServlet("/ChartServlet")
public class Chartservlet extends HttpServlet {
    private ConnectorMysql cm = new ConnectorMysql();
    private static final long serialVersionUID = 1L;
    
     /**
      * @see HttpServlet#HttpServlet()
      */
     public Chartservlet() {
     super();
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
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
             ResultSet rs = cm.getRS(cm.getConn(), "SELECT celsius_temp , fah_temp, fecha_temp FROM MEDICIONES.TEMPERATURAS");
             System.out.println("llamaste a servlet ChartServlet");
             List<Temperature> listTemp=new ArrayList<Temperature>();
             while(rs.next()) {
                 listTemp.add(new Temperature(rs.getFloat(1), rs.getFloat(2), rs.getTimestamp(3)));
             }
             Gson gson=new Gson();
             String jsonString=gson.toJson(listTemp);
             response.setContentType("application/json");
             response.getWriter().write(jsonString);
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
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
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     doGet(request, response);
     }

}