package entities;


/**
 * Clase pojo, que sirve para persistir, una entidad llamada usuario, en la base de datos.
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 */
public class Usuario {
    private String nombreUsuario;
    private int idUsuario;
    private String pwdUsuario;
    
    //constructs
    public Usuario(){}  
    public Usuario(String nombreUsuario) {
        setNombreUsuario(nombreUsuario);
    }    
    public Usuario(String nombreUsuario, int idUsuario) {
        setNombreUsuario(nombreUsuario);
        setIdUsuario(idUsuario);
    } 
     /**
     * Constructor de la clase Usuario.
     * @param nombreUsuario Nombre de usuario.
     * @param idUsuario Identificador de usuario.
     * @param pwdUsuario contraseña de usuario.
     * @author      Lazzaroni Gonzalo
     * @author      Fernandez Sergio
     * @version     %I%, %G%
     * @since       1.0
     */    
    public Usuario(String nombreUsuario, int idUsuario, String pwdUsuario) {
        setNombreUsuario(nombreUsuario);
        setIdUsuario(idUsuario);
        setPwdUsuario(pwdUsuario);
    }
    //setters
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public void setPwdUsuario(String pwdUsuario) {
        this.pwdUsuario = pwdUsuario;
    }
    //getters
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public int getIdUsuario () {
        return idUsuario;
    }
    public String getPwdUsuario () {
        return pwdUsuario;
    }    
}
