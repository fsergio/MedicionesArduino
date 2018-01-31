package connectors;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase especifica, para conexion con un motor de base de datos Mysql.
 * <p>
 * La clase ConnectorMysql utiliza la version de mysql Community server 5.7.20 Licencia GPL.
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 */
public class ConnectorMysql extends Connector {

    // construct
    /**
     *Constructor ConnectorMysql sin argumentos.
     * <ul>
     * <li>Debe tener cuidado, conectese con los datos de conexion previamente establecidos.
     * </ul>
     * @since     1.0
     */
    public ConnectorMysql() {

    }
    /**
     *Constructor ConnectorMysql con argumentos.
     * <ul>
     * <li>Aqui es aconsejable preestablecer los datos de conexion.
     * </ul>
     * @param driver    com.mysql.jdbc.Driver
     * @param protocol  jdbc:mysql
     * @param port  localhost:3306 (usted puede cambiar el puerto que desee, por defecto mysql escucha en 3306).
     * @param bd  nombre de su base de datos.
     * @param user  nombre de usuario con privilegios para su conexion con la base de datos.
     * @param pass  constraseña de usuario con privilegios para su conexion con la base de datos.
     * @since     1.0
     */    
    public ConnectorMysql(String driver, String protocol, String port, String bd, String user, String pass) {
        setDriver(driver);
        setProtocol(protocol);
        setPort(port);
        setBd(bd);
        setUser(user);
        setPass(pass);
    }
    /**
     *Metodo especifico para conexion con la base de datos mysql Community server 5.7.20.
     * <ul>
     * <li>Debe tener cuidado, conectese con los datos de conexion previamente establecidos.
     * </ul>
     * @return true si se ha podido establecer la conexion con normalidad.
     * @throws SQLEception  Si falla algun dato de conexion con la base de datos.
     * @throws ClassNotFoundException   Si falla la carga del driver odbc/jdbc(firebird), etc.
     * @since     1.0
     */ 
    @Override
    public boolean connect() {
        boolean r = false;
        try {
            Class.forName(getDriver());
            setConn(DriverManager.getConnection(getProtocol() + "://" + getPort() + "/" + getBd(), getUser(),getPass()));
            System.out.println("Connected to mysql!");      
            r = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            return r;
        }
    }

}