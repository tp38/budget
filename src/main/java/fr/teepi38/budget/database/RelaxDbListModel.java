package fr.teepi38.budget.database;


import java.util.Vector;

import javax.swing.AbstractListModel;

/**
 * a list model with RelaxDb object
 * @author : <a href="mailto:thierry.probst@free.fr">Thierry Probst</a>
 * @version 0.0 08/06/2026
 * @see javax.swing.AbstractListModel
 */
public class RelaxDbListModel extends AbstractListModel<RelaxDb> {

    private Vector<RelaxDb> data;

    /**
     * constructor
     * @param d a Vector of RelaxDb containing data
     */
    public RelaxDbListModel( Vector<RelaxDb> d ) {
        data = d;
    }

    /**
     * return the element at the specified index
     * @param index the specified index
     * @return a RelaxDb object
     */
    @Override
    public RelaxDb getElementAt(int index) {
        return data.elementAt(index);
    }

    /**
     * return the size of the Vector
     * @return the size
     */
    @Override
    public int getSize() {
        return data.size();
    }    
}
