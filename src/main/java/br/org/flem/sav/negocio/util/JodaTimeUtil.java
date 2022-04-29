/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.flem.sav.negocio.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.joda.time.MutableDateTime;


/**
 *
 * @author ilfernandes
 */
public class JodaTimeUtil {
    public static final SimpleDateFormat DATE_FORMAT_DIA_MES_ANO = new SimpleDateFormat("dd/MM/yyyy");

    public static Boolean compararDiaMesAno(DateTime data1, DateTime data2) {
        return DATE_FORMAT_DIA_MES_ANO.format(data1.toDate()).equals(DATE_FORMAT_DIA_MES_ANO.format(data2.toDate()));
    }

    public static Double diferencaApenasHoras(DateTime data1, DateTime data2) {
        //iguala os dias e mantém a diferença de horas para calcular a diferença
        data1 = data1.monthOfYear().setCopy(data2.getMonthOfYear());
        data1 = data1.dayOfMonth().setCopy(data2.getDayOfMonth());
        data1 = data1.year().setCopy(data2.getYear());
        return (Minutes.minutesBetween(data1, data2).getMinutes() / 60.0);
    }

    public static boolean mesmoDiaeHoraMaior(DateTime data1, DateTime data2) {
        boolean retorno = false;
        if (!compararDiaMesAno(data1, data2)) {
            retorno = data2.isAfter(data1);
        }
        return retorno;
    }

    public static Integer calculaQuantidadeDiasSemHoras(DateTime data1, DateTime data2) {
        data1 = data1.hourOfDay().setCopy(data2.getHourOfDay());
        data1 = data1.minuteOfHour().setCopy(data2.getMinuteOfHour());
        data1 = data1.secondOfMinute().setCopy(data2.getSecondOfMinute());
        return Days.daysBetween(data1, data2).getDays();
    }

    public static Date obterPrimeiroDiaHoraDoMes(Date data) {
        MutableDateTime data1 = new MutableDateTime(data);
        data1.setDayOfMonth(01);
        data1.setHourOfDay(00);
        data1.setMinuteOfHour(00);
        return data1.toDate();
    }
     public static Date obterUltimoDiaHoraDoMes(Date data) {
        MutableDateTime data1 = new MutableDateTime(data);
        data1.setDayOfMonth(data1.dayOfMonth().getMaximumValue());
        data1.setHourOfDay(23);
        data1.setMinuteOfHour(59);
        return data1.toDate();
    }
}
