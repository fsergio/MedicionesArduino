package entities;
//import java.sql.Date;
import java.sql.Timestamp;
/**
 * Clase que sirve para persistir, una entidad llamada temperatura, en la base de datos.
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 */
public class Temperature {
    private int idTemp;
    private float celsiusTemp;
    private float fahTemp;
    private Timestamp fechaTemp;//para utilizacion de las fechas y hora en mysql. utilizando datatime
    
    //constructs
    public Temperature() {}   
    public Temperature(int idTemp) {
        setIdTemp(idTemp);
    }
    public Temperature(int idTemp, float celsiusTemp) {
        setIdTemp(idTemp);
        setCelsiusTemp(celsiusTemp);
    }
    public Temperature(int idTemp, float celsiusTemp, float fahTemp) {
        setIdTemp(idTemp);
        setCelsiusTemp(celsiusTemp);
        setFahTemp(fahTemp);
    }
    //para google charts
    public Temperature(float celsiusTemp, float fahTemp, Timestamp fechaTemp) {
        setCelsiusTemp(celsiusTemp);
        setFahTemp(fahTemp);
        setFechaTemp(fechaTemp);
    }      
    /**
     * Constructor de la clase Temperature
     * @param idTemp Identificador de temperatura.
     * @param celsiusTemp Valor de la temperatura en celcius.
     * @param fahTemp Valor de la temperatura en fahrenhit.
     * @param fechaTemp Fecha.
     * @author      Lazzaroni Gonzalo
     * @author      Fernandez Sergio
     * @version     %I%, %G%
     * @since       1.0
     */
    public Temperature(int idTemp, float celsiusTemp, float fahTemp, Timestamp fechaTemp) {
        setIdTemp(idTemp);
        setCelsiusTemp(celsiusTemp);
        setFahTemp(fahTemp);
        setFechaTemp(fechaTemp);
    }    
    //getters
    public int getID() {return this.idTemp;}
    public float getCelsiusTemp() {return this.celsiusTemp;}
    public float getfahTemp() {return this.fahTemp;}
    public Timestamp getFechaTemp() {return this.fechaTemp;}   
    //setters
    public void setIdTemp(int idTemp) {this.idTemp = idTemp;}
    public void setCelsiusTemp(float celsiusTemp) {this.celsiusTemp = celsiusTemp;} 
    public void setFahTemp(float fahTemp) {this.fahTemp = fahTemp;} 
    public void setFechaTemp(Timestamp fechaTemp) {this.fechaTemp = fechaTemp;}    
}
