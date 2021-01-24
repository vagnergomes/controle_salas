/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controlesalas.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author vagner.gomes
 */
public class DateUtil {
    
    public static Timestamp data_Timestamp(){
    Date data = new Date(System.currentTimeMillis());
    Timestamp dateTime = new Timestamp(data.getTime());
    return dateTime;
    }
    
}
