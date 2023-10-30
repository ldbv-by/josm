// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.tagging.ac;

import java.awt.AWTEvent;

/**
 * This event is generated by an AutoCompTextField when an autocomplete occurred.
 *
 * @see AutoCompTextField
 * @see AutoCompListener
 * @since 18221
 */
public class AutoCompEvent extends AWTEvent {

    /**
     * The first number in the range of ids used for autoComp events.
     */
    public static final int AUTOCOMP_FIRST = 5900;

    /**
     * The last number in the range of ids used for autoComp events.
     */
    public static final int AUTOCOMP_LAST = 5901;

    /**
     * This event id indicates that an autocomp is about to start.
     */
    public static final int AUTOCOMP_BEFORE = AUTOCOMP_FIRST;

    /**
     * This event id indicates that an autocomp completed.
     */
    public static final int AUTOCOMP_DONE = AUTOCOMP_FIRST + 1;

    /*
     * JDK 1.1 serialVersionUID
     */
    private static final long serialVersionUID = 3745384758753475838L;

    /** the selected autocomplete item */
    private Object item;

    /**
     * Constructs a <code>AutoCompEvent</code> object.
     * <p> This method throws an
     * <code>IllegalArgumentException</code> if <code>source</code>
     * is <code>null</code>.
     *
     * @param source The (<code>AutoCompTextField</code>) object that
     *               originated the event
     * @param id     An integer that identifies the event type.
     *               For information on allowable values, see
     *               the class description for {@link AutoCompEvent}
     * @param item   The item selected for autocompletion.
     * @throws IllegalArgumentException if <code>source</code> is null
     * @see #getSource()
     * @see #getID()
     */
    public AutoCompEvent(Object source, int id, Object item) {
        super(source, id);
        this.item = item;
    }

    /**
     * Returns the item selected for autocompletion.
     *
     * @return the item selected for autocompletion
     */
    public Object getItem() {
        return item;
    }

    /**
     * Returns a parameter string identifying this text event.
     * This method is useful for event-logging and for debugging.
     *
     * @return a string identifying the event and its attributes
     */
    @Override
    public String paramString() {
        String typeStr;
        switch(id) {
            case AUTOCOMP_BEFORE:
                typeStr = "AUTOCOMP_BEFORE";
                break;
            case AUTOCOMP_DONE:
                typeStr = "AUTOCOMP_DONE";
                break;
          default:
                typeStr = "unknown type";
        }
        return typeStr;
    }
}
