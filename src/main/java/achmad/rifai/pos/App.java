/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package achmad.rifai.pos;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AmMas
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            achmad.rifai.pos.utils.Uang u=new achmad.rifai.pos.utils.Uang("Rp10.000,00");
            System.out.println("" + u.getVal());
        } catch (ParseException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
