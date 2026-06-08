package fr.teepi38.budget.accounts;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import fr.teepi38.budget.common.DateSelector;
import fr.teepi38.budget.database.RelaxDb;
import fr.teepi38.budget.database.Result;

public class AccountSummaryPanel extends JPanel {

    // AccountSummaryTableModel model;
    DateSelector ds;
    AccountSummaryTableView tableView;
    RelaxDb db;

    public AccountSummaryPanel( RelaxDb db, AccountPanelCallBack cb )  {
        super( new BorderLayout() );

        this.db = db;

        ds = new DateSelector();
        ds.addActionListener( new btnActionListener() );
        add( ds, BorderLayout.NORTH );

        Calendar cal = ds.getDate();
        AccountSummaryTableModel model = getTableModelFor(cal);
        tableView = new AccountSummaryTableView(null, model, cb );
        add( tableView, BorderLayout.CENTER );
    }

    private AccountSummaryTableModel getTableModelFor( Calendar cal ) {

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

    class btnActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Calendar cal = ds.getDate();
            AccountSummaryTableModel newModel = getTableModelFor( cal );
            tableView.setUpTableData(newModel);
        }
    }

}