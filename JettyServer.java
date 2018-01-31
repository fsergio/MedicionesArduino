import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.util.log.*;
import org.eclipse.jetty.servlet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import connectors.ConnectorMysql;
import java.net.URI;
import java.net.URL;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;

/**
 * Clase que instancia un objeto Jetty Server - version 9.4.8.v20171121
 * <p>
 * <li>Para mas informacion Visiste: https://www.eclipse.org/jetty/
 * <li>Debe ingresar al servidor desde la URL: localhost:8080/login.html (por defecto, debe estar registrado).
 * <li>Realiza automaticamente la conexion con una base de datos Mysql version 5.7.20
 * <li>Cuando usted cierra el servidor, automaticamente cierra todas las comunicaciones.
 * </u1>
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 */
public class JettyServer extends JFrame {
    private boolean isRunning = false;
    private JButton btn = null;
    private Server server;
    private ServerConnector http;
    private ServletContextHandler context;
    private ConnectorMysql cmysql = new ConnectorMysql("com.mysql.jdbc.Driver", "jdbc:mysql", "localhost:3306", "mediciones", "root", "masterkey");
    
    public JettyServer() {
        System.out.println("jetty load...");        
        server = new Server();
        http = new ServerConnector(server);
        http.setHost("localhost");        
        http.setPort(8080);
        //http.setIdleTimeout(30000);
        server.addConnector(http);//añadiendo nuevo conector http para comunicaciones con el cliente.
        
        // resource handle, para manejo de contenido html estatico
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[] { "index.html" });
        resource_handler.setResourceBase("./");
        
        // configuracion context servlets.
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/"); 
        context.setResourceBase("./servlet"); // asume que las clases de servlets estan en el directorio actual      
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        // asocio servlets - uri  application
        context.addServlet(servlets.JsonTestServlet.class, "/listMediciones");//desde ajax, se debe llamar a listMediciones(url del servlet asociado en jetty)
        context.addServlet(servlets.JsonDeleteTemp.class, "/deleteTemp");//desde ajax, se debe llamar a listMediciones(url del servlet asociado en jetty)        
        context.addServlet(servlets.LoginServlet.class, "/LoginServlet");       
        context.addServlet(servlets.JsonAddTemp.class, "/addTemp");         
        context.addServlet(servlets.Chartservlet.class, "/ChartServlet");
        // asocio lista de handlers al servidor (file server + servlets)
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context, resource_handler, new DefaultHandler() });
        server.setHandler(handlers);
        //arranca el servidor y conecta con la base de datos mysql por defecto al iniciar app, con conexion mysql estatica.
        doStart();
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( ((JButton) e.getSource()).getText().equalsIgnoreCase("Stop Server") ) {
                    doStop();
                } else {
                    doStart();
                }
            }
        });
        add(btn);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if ( isRunning ) doStop();
                // cerrar las conexiones a bases de datos!
                System.out.println("JettyServer close");
                cmysql.disconnect(cmysql.getConn());
                System.out.println("Mysql close");
                System.exit(0);
            }
        });
        setSize(300,300);
        setTitle("Jetty Server Control Panel");
        setVisible(true);
    }
    /**
     * Inicia el servidor y una conexion con Mysql.
     * @return True Si se ha iniciado el servidor, con exito.
     * @throws Exception Si falla el inicio del servidor.
     */
    private boolean doStart() {
        try {
            server.start();
            isRunning=true;
            updateLabel("Stop Server");
            System.out.println("Servidor Started! version "+server.getVersion() + " en puerto "+http.getPort());
            cmysql.connect();//conectando a mysql
            return true;
        } catch(Exception ex) {
            System.err.println("Error Starting Server ..."+ex.getMessage());
        }
        return false;
    }
    /**
     * Detiene el servidor y la conexion con Mysql.
     * @return false Si no se ha podido detener el servidor, con exito.
     * @throws Exception Si no se ha podido detener el servidor, con exito, o desconexion con Mysql.
     */    
    private boolean doStop() {
        try {
            server.stop();
            isRunning=false;
            updateLabel("Start Server");
            System.out.println("Servidor Stopped!");
            cmysql.disconnect(cmysql.getConn());//cierro conexion mysql
            return true;
        } catch(Exception ex) {
            System.err.println("Error Stopping Server ..."+ex.getMessage());
        }
        return false;
    }
    /**
     * Actualiza el boton, de interfaz grafica del servidor.
     * @param label String par aactualizar.
     */     
    private void updateLabel(final String label) {
        if ( btn == null ) btn = new JButton(label);
        else btn.setText(label);
    }
    /**
     * Inicia la aplicacion, creando una nueva instancia del servidor jetty y mysql.
     */         
    public static void main(String[] args) throws Exception {
        new JettyServer();
    }
}
