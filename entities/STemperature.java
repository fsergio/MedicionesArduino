package entities;

 

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Clase que sirve para serializar un objeto JSON.
 * <p>
 * <u1>
 * <li>Serializacion personalizada.
 * <li>Convierte un objeto java, a objeto JSON.
 * <li>Debe tener instalado libreria GSON 2.2.4
 * </u1>
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 */
public class STemperature implements JsonSerializer<Temperature> {

    /**
     * Retorna un objeto Json, previamente serializado desde un objeto java.
     * <code>GET</code> y
     * <code>POST</code> metodos.
     * @param temp Objeto especifico Temperature a serializar.
     * @param typeOfSrc Objeto fuente(tipo de clase).
     * @param context Para clases anidadas.
     * @return JsonElement Retorna objeto java serializado.
     */    
    @Override
    public JsonElement serialize(final Temperature temp, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();//el objeto temperatura completo
        final JsonObject jsonObject2 = new JsonObject();//para retornar el array - "data":[{campo1, campo2}, {campo1, campo2}]
        final JsonArray jsonArray = new JsonArray();
        
        jsonObject.addProperty("id_temp", temp.getID());
        jsonObject.addProperty("celsius_temp", temp.getCelsiusTemp());
        jsonObject.addProperty("fah_temp", temp.getfahTemp());
        jsonObject.addProperty("fecha_temp", temp.getFechaTemp().toString());
        
        jsonArray.add(jsonObject);
        jsonObject2.add("data", jsonArray);
        return jsonObject2;
    }  
}
