package fr.teepi38.budget;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateSelector extends JPanel {
    SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy");
    JButton btn;
    SpinnerDateModel dm;

    public DateSelector() {
        super();

        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        try {
            Date earliestDate = sdf.parse( "01/01/2024" );
            Date latestDate = sdf.parse( "31/12/2126" );
            dm = new SpinnerDateModel( initDate, earliestDate, latestDate, Calendar.DAY_OF_MONTH );
            JSpinner spinner = new JSpinner(dm);
            spinner.setName( "date_spinner" );
            spinner.setPreferredSize( new Dimension(150, 20) );
            spinner.setEditor( new JSpinner.DateEditor(spinner, "dd/MM/yyyy") );
            add( spinner );            
        } 
        catch ( ParseException e ) {
            e.printStackTrace();
        }

        btn = new JButton( "Go" );
        btn.setName( "date_valid_btn" );
        add(btn);
    }

    void addActionListener( ActionListener l ) {
        btn.addActionListener( l );
    }

    String getDate() {
        return sdf.format( dm.getDate() );
    }
    
    String getLastDayOfMonth() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime( dm.getDate() );
        calendar.set( Calendar.DATE, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );
        return sdf.format( calendar.getTime() );
    }
}
