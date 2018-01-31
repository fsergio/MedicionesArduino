/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectors;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase especifica, para conexion con un motor de base de datos Firebird.
 * <p>
 * La clase ConnectorFirebird utiliza la version de Firebird 3.0 Licencia GPL.
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 */
public class ConnectorFirebird extends Connector{
    
    //construct
    /**
     *Constructor ConnectorFirebird sin argumentos.
     * <ul>
     * <li>Debe tener cuidado, conectese con los datos de conexion previamente establecidos.
     * </ul>
     * @since     1.0
     */    
    public ConnectorFirebird() { 
        
    }
    /**
     *Constructor ConnectorFirebird con argumentos.
     * <ul>
     * <li>Aqui es aconsejable preestablecer los datos de conexion.
     * </ul>
     * @param driver    org.firebirdsql.jdbc.FBDriver
     * @param protocol  jdbc:firebirdsql
     * @param port  localhost/3050 (usted puede cambiar el puerto que desee, por defecto firebird escucha en 3050).
     * @param bd  nombre de su base de datos.
     * @param user  nombre de usuario con privilegios para su conexion con la base de datos.
     * @param encoding  en firebird 3.0, debe abrir archivo de configuracion firebird.conf(como administrador)
     * en ruta de instalacion. modificar encoding=NONE- Aqui puede luego utilizar ?encoding=NONE(como argumento).
     * @param pass  constraseña de usuario con privilegios para su conexion con la base de datos.
     * @since     1.0
     */      
    public ConnectorFirebird(String driver, String protocol, String port, String bd, String encoding, String user, String pass) {
        setDriver(driver);
        setProtocol(protocol);
        setPort(port);
        setBd(bd);
        setEncoding(encoding);
        setUser(user);  
        setPass(pass);        
    }
    /**
     *Metodo especifico para conexion con la base de datos Firebird 3.0.
     * <ul>
     * <li>Debe tener cuidado, conectese con los datos de conexion previamente establecidos.
     * </ul>
     * @return true si se ha podido establecer la conexion con normalidad.
     * @throws SQLEception  Si falla algun dato de conexion con la base de datos.
     * @throws ClassNotFoundException   Si falla la carga del driver odbc/jdbc(firebird), etc.
     * @since     1.0
     */     
    @Override
    public  boolean connect() {
        boolean r = false;
        try {
            Class.forName(getDriver());  
            setConn(DriverManager.getConnection(getProtocol()+":"+getPort()+":"+getBd()+getEncoding(), getUser(), getPass()));
            r = true;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }finally {
            return r;
        }
    }
    
}