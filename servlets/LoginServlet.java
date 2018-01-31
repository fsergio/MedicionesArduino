package servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.*;
import connectors.*;
import entities.*;
import com.google.gson.*;

/**
 * Clase que sirve para el procesamiento de log-in de usuarios.
 * <p>
 * <u1>
 * <li>Autentifica al usuario.
 * <li>Crea una nueva instancia de objeto Session, para cada usuario a conectarse.
 * <li>Si el usuario se ha autentificado correctamente, el metodo {@link processRequest} puede devolver
 * <li>en response un objeto de tipo JSON con OK o FAIL(si fallo la autentificacion de usuario).
 * <li>Debe tener instalado libreria GSON 2.2.4
 * </u1>
 * La clase ConnectorFirebird utiliza la version de Firebird 3.0 Licencia GPL.
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 * @WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
 */
public class LoginServlet extends HttpServlet {
    private ConnectorMysql cm = new ConnectorMysql();
    private static Gson gson = new Gson();    
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
        String usr = "", pwd = "", data = "", name = "";//nombre de usuario y contraseña, separados por /     
        System.out.println("llamaste al servlet LoginServlet");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = null;        
        Map<String,String> map = new HashMap<String,String>();
        ResultSet rs= null;
        
        try {
            data = request.getParameter("user");//tomo los valores del string "valor1/valor2/...valorn"
            int i = 0;
            for(String d : data.split("/")) {//seteo variables para su posterior verificacion en la base de datos.
                if(i == 0) {
                    usr = d;
                    System.out.println("usuario: "+usr);
                } else {
                    pwd = d;
                    System.out.println("pass: "+pwd);
                }
                i++;
            }
            //busco al usuario en mysql.USAR ESTOS DOS CAMPOS DESDE UN QUERY CON "COLUMN", PORQUE SON VARCHAR.
            rs = cm.getRS(cm.getConn(), "select usr_name from usuarios where usr_name = \""+usr+"\" and pwd = \""+pwd+"\"");//null si no lo encuentra...
            System.out.println(cm.getConn());
            if ( rs.next()) {
                name = rs.getString("usr_name");
                if ( name != null ) {//usuario encontrado.
                    session = request.getSession(true); // crea sesion de usuario, si no esta la sesion, crea nueva.
                    session.setAttribute("usr",name);//guardo el usuario, en el servidor(en su sesion).
                    session.setAttribute("pwd",pwd);
                    System.out.println("id session: "+session.getId()+" usuario "+session.getAttribute("usr")+", se crea session y direcciono a index.html");
                    //out.write("{\"Result\": \"OK\"}");                        
                    map.put("Result","OK");
                    map.put("Msg","Bienvenido "+usr);
                    out.write(gson.toJson(map));
                }
            }else {//SI SALE POR ACA, DEBERIA CONTACTARSE CON EL ADMIN, O CREARSE UNA CUENTA/REGISTRARSE/ETC...ETC
               //out.write("{\"Result\": \"FAIL\"}");
                map.put("Result","FAIL");
                map.put("Msg","Usuario "+usr+" desconocido, contactese con el administrador.");
                out.write(gson.toJson(map));               
            }
            rs.close();
        } catch(SQLException e) {
            System.out.println(e.getMessage());    
            map.put("Result","FAIL");
            map.put("Exception: ", e.getMessage());
            out.write(gson.toJson(map));
        }finally {
            out.close();
        }
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
