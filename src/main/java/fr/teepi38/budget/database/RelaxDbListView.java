package fr.teepi38.budget.database;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * A panel with a list of known RelaxDb and a button to connect to the selected one.
 * 
 * @author : <a href="mailto:thierry.probst@free.fr">Thierry Probst</a>
 * @version 0.0, 20/05/2026
 * @see javax.swing.JPanel
 * @see java.awt.event.ActionListener
 */
public class RelaxDbListView extends JPanel {
    private Frame parent;
    private RelaxDbListModel model;
    private JList<RelaxDb> db_list;
    private JButton connect_btn;
    
    /**
     * constructor
     * @param parent the parent JFrame
     * @param model a RelaxDbListModel containing the data
     * @see java.awt.Frame 
     * @see fr.teepi38.budget.database.RelaxDbListModel 
     */
    public RelaxDbListView( Frame parent, RelaxDbListModel model ) {
        super( new GridBagLayout() );
        this.parent = parent;
        this.model = model;

        setName("db_chooser_panel");
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = GridBagConstraints.WEST;

        // databases label
        JLabel databases_lbl = new JLabel("Databases :");
        databases_lbl.setName("databases_lbl");

        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = GridBagConstraints.REMAINDER;
        add( databases_lbl, gc );

        // databases list
        db_list = new JList<>(this.model);
        db_list.setName("db_list");
        db_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        db_list.addListSelectionListener( new RelaxDbListSelectionListener() );
        db_list.clearSelection();
        db_list.setCellRenderer( new DbCellRenderer() );

        JScrollPane sc = new JScrollPane( db_list );
        sc.setMinimumSize( new Dimension(600, 480));

        gc.gridx = 0; gc.gridy = 1;
        add(sc, gc);

        // connect button
        connect_btn = new JButton("Connexion");
        connect_btn.setName("connect_btn");
        // db_connect_btn.addActionListener( this );
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 1;
        add( connect_btn, gc );        
    }


    /**
     * cell renderer for DbList
     * @version 0.0 08/06/2026
     * @see javax.swing.ListCellRenderer
     * @see javax.swing.JLabel
     */
    class DbCellRenderer extends JLabel implements ListCellRenderer<Object> {

        /**
         * constructor
         */
        public DbCellRenderer() {
            setOpaque( true );
        }

        /**
         * render the cell
         * @param list the selected JList
         * @param value the object
         * @param index the index of the object in the list
         * @param isSelected boolean true if the cell is selected
         * @param cellHasFocus boolean true if the cell has focus
         * @return return a JLabel for DbCellRenderer
         */
        public Component getListCellRendererComponent( JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
            Color background;
            Color foreground;
            
            setText( ((RelaxDb)value).summary() );

            if (isSelected) {
                background = Color.LIGHT_GRAY;
                foreground = Color.BLUE;
            } else {
                background = Color.WHITE;
                foreground = Color.DARK_GRAY;
            };

            setBackground(background);
            setForeground(foreground);        
            
            return this;
        }
    }

    /**
     * list selection listener
     * @version 0.0 08/06/2026
     * @see javax.awt.event.ListSelectionListener
     */
    class RelaxDbListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            CredentialDialog cd = new CredentialDialog( parent );
            cd.setVisible(true);
            if( cd.isCredentialValid() ) {
                JOptionPane.showMessageDialog( parent, "This is ok", "Success", JOptionPane.PLAIN_MESSAGE );
                Credential cred = cd.getCredential();
                db_list.getSelectedValue().credential(cred);
            }
        }

    }
    
}
