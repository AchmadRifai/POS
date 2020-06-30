/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package achmad.rifai.pos.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 *
 * @author AmMas
 */
public class Uang {
    private BigDecimal val;

    public Uang(BigDecimal val) {
        this.val = val;
    }

    public Uang(String s) throws ParseException {
        DecimalFormat df=rupiah();
        val=BigDecimal.valueOf((long)df.parse(s));
    }

    @Override
    public String toString() {
        DecimalFormat df=rupiah();
        return df.format(val);
    }

    public BigDecimal getVal() {
        return val;
    }

    public void setVal(BigDecimal val) {
        this.val = val;
    }

    private DecimalFormat rupiah() {
        DecimalFormat df=(DecimalFormat) NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs=DecimalFormatSymbols.getInstance();
        df.setDecimalFormatSymbols(dfs);
        dfs.setCurrencySymbol("Rp");
        dfs.setDecimalSeparator(',');
        dfs.setMonetaryDecimalSeparator('.');
        df.setDecimalSeparatorAlwaysShown(true);
        return df;
    }
}
