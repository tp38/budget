package fr.teepi38.budget.accounts;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import fr.teepi38.budget.DateSelector;
import fr.teepi38.budget.database.RelaxDb;
import fr.teepi38.budget.database.Result;

public class AccountSummaryPanel extends JPanel {

    AccountSummaryTableModel model;
    DateSelector ds;

    public AccountSummaryPanel(RelaxDb db )  {
        super( new BorderLayout() );

        ds = new DateSelector();
        add( ds, BorderLayout.NORTH );

        model = initTableModel(db);
        add( new AccountSummaryTableView(null, model), BorderLayout.CENTER );
    }

    private AccountSummaryTableModel initTableModel( RelaxDb db ) {
        Calendar cal = ds.getDate();

        Date desiredDate = cal.getTime();
        DateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );

        Vector<AccountSummary> data = new Vector<>();
        Result[] res = null;
        try {
            // get account list
            res = db.getAccountList();
            if( res != null ) {
                // for each retrieve account values (check, done, upcoming)
                for (Result result : res) {
                    Date closeDate = null;
                    if( ! result.value().equals( "0000-00-00" ) ) {
                        closeDate = format.parse( result.value() );
                    } else {
                        closeDate = format.parse( "2069-12-31" );
                    }
                    if( desiredDate.before(closeDate) ) {
                        try {
                            AccountSummary as = db.getSummaryForAccount( result.key(), cal );
                            data.add( as );
                        } catch (Exception e) {
                            System.out.println( e.getMessage() );
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }
        return new AccountSummaryTableModel(data);
    }
}
