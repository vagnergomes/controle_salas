/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converters;

import br.com.controlesalas.entities.Role;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


/**
 *
 * @author vagner.gomes
 */
@FacesConverter(value = "roleConverter", forClass = Role.class )
public class RoleConverter implements Converter{

     @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            if (value == null || value.isEmpty()) {
                return null;
            }

            Long id = Long.valueOf(value);

            Role role = new Role();
            role.setIdRole(id);

            return role;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null
                || !(value instanceof Role)) {
            return null;
        }

        return ((Role) value).getIdRole().toString();
    }
    
}
