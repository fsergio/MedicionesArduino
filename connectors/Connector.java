package connectors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * las interfaces sirven para saber que se debe hacer en una subclase, pero no como...no hay metodos concretos en las mismas. solo abstractos.
 * los campos de instancia son publicos, estaticos y finales.
 * se diferencian de las clases abstractas ya que en ellas, si se podrian llegar a tener metodos concretos, pero al menos un metodo abstracto. 
 */
/**
 * Clase abstracta, de la cual pueden derivar diferentes clases, con el
 * fin de poder conectarse a un motor de base de datos.
 * <p>
 * La clase Connector posee solo un metodo abstracto, el cual 
 * es obligatorio implementarlo.
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 */
public abstract class Connector {
    private static Connection conn = null;
    private String driver;
    private String protocol;
    private String port;
    private String bd;
    private String encoding;
    private String user;
    private String pass;
    
    /** 
     * Clase abstracta para comunicacion con motores de base de datos.
     * <p>
     *Es obligatoria su implementacion.
     * @see             ConnectorFirebird
     * @see             ConnectorMysql
     * @since           1.0
     */    
    public abstract boolean connect();
    /**
     *Obtiene el driver de conexion con motor de base de datos.
     * @since     1.0
     * @return          <code>true</code> si se ha podido establecer la conexion.
     */    
    public String getDriver() {
        return driver;
    }
    /**
     *Obtiene el protocolo de conexion con motor de base de datos.
     * @since     1.0
     * @return          <code>protocol</code>
     */    
    public String getProtocol() {
        return protocol;
    }
    /**
     *Obtiene el puerto de conexion con motor de base de datos.
     * @since     1.0
     * @return          <code>port</code>
     */    
    public String getPort() {
        return port;
    }
    /**
     *Obtiene la direccion, del fichero de la base de datos.
     *para su posterior conexion.
     *Ejemplo Firebird 3.0 (Windows) C:/poo/base_datos/BASE_25417V3.0.GDB
     * @return          <code>bd</code>
     * @since     1.0
     */    
    public String getBd() {
        return bd;
    }
    /**
     *Obtiene el Encoding de conexion con motor de base de datos.
     *Ejemplo Firebird 3.0 (Windows) encoding=NONE
     * @return          <code>encoding</code>     *
     * @since     1.0
     */ 
    public String getEncoding() {
        return encoding;
    }
    /**
     *Obtiene el Usuario  de conexion con motor de base de datos.
     * @return          <code>user</code>
     * @since     1.0
     */
    public String getUser() {
        return user;
    }
    /**
     *Obtiene la contraseña de conexion con motor de base de datos.
     * @return          <code>pass</code>
     * @since     1.0
     */
    public String getPass() {
        return pass;
    }
    /**
     *Obtiene la conexion con motor de base de datos.
     *Previamente se debera establecer la misma.
     * @return          <code>conn</code>
     * @since     1.0
     */
    public Connection getConn() {
        return conn;
    }
    //setters
    /**
     *Obtiene la conexion con motor de base de datos.
     *Previamente se debera establecer la misma.
     * @return          <code>conn</code>
     * @since     1.0
     */
    public void setPort(String port) {
        this.port = port;
    }
    /**
     *Establece la ubicacion del archivo, para posterior conexion con motor de base de datos.
     * @param bd    La ubicacion del archivo de la base de datos.
     * @since     1.0
     */
    public void setBd(String bd) {
        this.bd = bd;
    }
    /**
     *Establece el usuario con privilegios, para posterior conexion con motor de base de datos.
     * @param user    El nombre del usuario previamente establecido,
     * en la configuracion de la base de datos.
     * @since     1.0
     */
    public void setUser(String user) {
        this.user = user;
    }
    /**
     *Establece la contraseña para el usuario, para posterior conexion con motor de base de datos.
     * @param pass   La contraseña del usuario para conexion con motor de base de datos.
     * @since     1.0
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
    /**
     *Establece el driver de comunicacion, para posterior conexion con motor de base de datos.
     * @param driver    El driver de comunicacion jdbc/odbc para la conexion con la base de datos.
     * @since     1.0
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }
    /**
     *Establece el encoding, para posterior conexion con motor de base de datos.
     * @param encoding    El encoding, puede ser encoding=NONE en firebird, a partir de la version 3.0.
     * @since     1.0
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    /**
     *Establece el protocolo, para posterior conexion con motor de base de datos.
     * @param protocol    El protocolo que se deba utilizar, para su posterior comunicacion con la base de datos.
     * @since     1.0
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    /**
     *Establece la conexion(previamente establecida), para posterior conexion con motor de base de datos.
     * @param bd    El encoding, puede ser encoding=NONE en firebird, a partir de la version 3.0
     * @param Observar que el campo de instancia conn, es una variable estatica. visible para sus subsclases.
     * @since     1.0
     */
    public static void setConn(Connection conn) {
        Connector.conn = conn;
    }
    /**
     *Devuelve un objeto de tipo Resultset, el cual contiene registros extraidos desde una base de datos.
     * @param conn    La conexion previamente establecida con la base de datos.
     * @param query   La consulta para ser ejecutada por getRS.
     * @return ResultSet    Devuelve la consulta ejecutada, como un objeto ResultSet.
     * @return null         Devuelve null cuando no se han encontrado registros. O bien, cuando se lanza
     * una excepcion SQLException.
     * @since     1.0
     */
    public static ResultSet getRS(Connection conn, String query) {
        ResultSet rs = null;
        try {
            Statement stm = conn.createStatement();
            rs = stm.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            return rs;
        }
    }
    /**
     *Para ejecutar consultas simples a un motor de base de datos.
     * @param conn    La conexion previamente establecida con la base de datos.
     * @param query   La consulta para ser ejecutada por getQuery.
     * @return true   true si se ha ejecutado con normalidad.
     * @return false  false si se lanza una excepcion de tipo SQLException.
     * @since     1.0
     */
    public static boolean getQuery(Connection conn, String query) {
        boolean r = false;
        try {
            Statement stm = conn.createStatement();
            stm.execute(query);
            stm.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            return r;
        }
    }
    /**
     *Para la desconexion con motor de base de datos.
     * @param conn    La conexion previamente establecida con la base de datos.
     * @param query   La consulta para ser ejecutada por getQuery.
     * @return true   true si se ha ejecutado con normalidad.
     * @return false  false si se lanza una excepcion de tipo SQLException.
     * @since     1.0
     */
    public static boolean disconnect(Connection conn) {
        boolean r = false;
        try {
            conn.close();
            r = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            return r;
        }
    }
}
