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
import pdf.*;
import java.io.File;
/**
 * Clase que sirve como servlet, para generar un update o insert, sobre la base de datos mysql.
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
@WebServlet(name = "JsonAddTemp", urlPatterns = {"/addTemp"})
public class JsonAddTemp extends HttpServlet {
    private ConnectorMysql cm = new ConnectorMysql();
    private static Gson gson;
    private ArrayList<Temperature> t = new ArrayList<Temperature>();
    private ResultSet rs;
    private ConvertPDFGeneric pdfg;    
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
        //Map<String,String> map = new HashMap<String,String>();       
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        System.out.println("llamaste al servlet JsonAddTemp");
        
        HttpSession session = request.getSession(false);//sino estaba logeado, lo redireccino a login. solo desde alli se generan permisos.      
        if(session != null) {
            try {
                String data = request.getParameter("data");
                System.out.println(data);
                String [] row = data.split("/");
                if (row[0].equals("insert")){ 
                    cm.getQuery(cm.getConn(), "insert into temperaturas (celsius_temp, fah_temp, fecha_temp) values ('"+row[1]+"', '"+row[2]+"', now() )");
                    System.out.println("insert into temperaturas (celsius_temp, fah_temp, fecha_temp) values ('"+row[1]+"', '"+row[2]+"', now() )");
                    out.write("{\"Result\":\"OK\"}");
                } else if (row[0].equals("update")){   
                    cm.getQuery(cm.getConn(), "update temperaturas set celsius_temp = '"+row[2]+"', fah_temp = '"+row[3]+"', fecha_temp = now() where id_temp = "+row[1]+" ");
                    System.out.println("update temperaturas set celsius_temp = '"+row[2]+"', fah_temp = '"+row[3]+"', fecha_temp = now() where id_temp = "+row[1]+" ");
                    out.write("{\"Result\":\"OK\"}");
                } else if (row[0].equals("pdf")) {
                    rs = cm.getRS(cm.getConn(), "SELECT *from mediciones.temperaturas");
                    pdfg = new ConvertPDFGeneric();
                    pdfg.convert(new File("Reporte_arduino.pdf"), rs);
                    rs.close();
                    out.write("{\"Result\":\"OK\"}");                    
                }
            } catch(Exception e) {
                System.err.println("error exception: "+e.getMessage());
                out.write("{\"Result\":\"FAIL\"}");              
            } finally {
                out.close();
            }
        }else {
            System.out.println("primero logeate gil!!!redireccinando a login");
            out.write("{\"Result\": \"FAIL\"}");            
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
