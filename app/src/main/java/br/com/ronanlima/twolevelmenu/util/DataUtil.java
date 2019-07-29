package br.com.ronanlima.twolevelmenu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
    public static final String PADRAO_DATE_ISO = "yyyy-MM-dd";
    public static final String PADRAO_DATE_BR = "dd/MM/yyyy";

    /**
     * Tenta converter a data recebida no formato Android, caso não dê certo, tenta
     * converter no formato ios.
     *
     * @param data the data
     * @return the date
     */
    public static Date transformaData(String data) {
        Date dt2 = null;
        if (data != null && !data.trim().equals("")) {
            try {
                dt2 = parse(data, PADRAO_DATE_BR);
            } catch (ParseException e) {
                try {
                    dt2 = parse(data, PADRAO_DATE_ISO);
                } catch (ParseException e1) {
                    dt2 = null;
                }
            }
        }
        return dt2;
    }

    /**
     * Parses the.
     *
     * @param data    the data
     * @param formato the formato
     * @return the date
     * @throws ParseException the parse exception
     */
    private static Date parse(String data, String formato) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(formato);
        df.setLenient(false);
        return df.parse(data);
    }
}
