/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters;

import br.com.controlesalas.entities.Sala;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


/**
 *
 * @author vagner.gomes
 */
@FacesConverter(value = "salaConverter", forClass = Sala.class )
public class SalaConverter implements Converter{

     @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            if (value == null || value.isEmpty()) {
                return null;
            }

            Long id = Long.valueOf(value);

            Sala sala = new Sala();
            sala.setIdSala(id);

            return sala;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null
                || !(value instanceof Sala)) {
            return null;
        }

        return ((Sala) value).getIdSala().toString();
    }
    
}
