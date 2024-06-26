// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.actions;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.openstreetmap.josm.actions.mapmode.DrawAction;
import org.openstreetmap.josm.command.ChangeNodesCommand;
import org.openstreetmap.josm.command.SelectCommand;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.UndoRedoHandler;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.tools.Shortcut;
import org.openstreetmap.josm.tools.Utils;

/**
 * Follow line action - Makes easier to draw a line that shares points with another line
 *
 * Aimed at those who want to draw two or more lines related with
 * each other, but carry different information (i.e. a river acts as boundary at
 * some part of its course. It preferable to have a separated boundary line than to
 * mix totally different kind of features in one single way).
 *
 * @author Germán Márquez Mejía
 */
public class FollowLineAction extends JosmAction {

    /**
     * Constructs a new {@code FollowLineAction}.
     */
    public FollowLineAction() {
        super(
                tr("Follow line"),
                "followline",
                tr("Continues drawing a line that shares nodes with another line."),
                Shortcut.registerShortcut("tools:followline", tr(
                "Tools: {0}", tr("Follow")),
                KeyEvent.VK_F, Shortcut.DIRECT), true);
    }

    @Override
    protected void updateEnabledState() {
        updateEnabledStateOnCurrentSelection();
    }

    @Override
    protected void updateEnabledState(Collection<? extends OsmPrimitive> selection) {
        updateEnabledStateOnModifiableSelection(selection);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        DataSet ds = getLayerManager().getEditDataSet();
        if (ds == null)
            return;
        MapFrame map = MainApplication.getMap();
        if (!(map.mapMode instanceof DrawAction)) return; // We are not on draw mode

        Collection<Node> selectedPoints = ds.getSelectedNodes();
        Collection<Way> selectedLines = ds.getSelectedWays();
        if ((selectedPoints.size() > 1) || (selectedLines.size() != 1)) // Unsuitable selection
            return;

        Node last = ((DrawAction) map.mapMode).getCurrentBaseNode();
        if (last == null)
            return;
        Way follower = selectedLines.iterator().next();
        if (follower.isClosed())    /* Don't loop until OOM */
            return;
        Node prev = follower.getNode(1);
        boolean reversed = true;
        if (follower.lastNode().equals(last)) {
            prev = follower.getNode(follower.getNodesCount() - 2);
            reversed = false;
        } else if (!follower.firstNode().equals(last)) {
            return; // see #23442
        }
        List<OsmPrimitive> referrers = last.getReferrers();
        if (referrers.size() < 2) return; // There's nothing to follow

        Node newPoint = null;
        for (final Way toFollow : Utils.filteredCollection(referrers, Way.class)) {
            if (toFollow.equals(follower)) {
                continue;
            }
            Set<Node> points = toFollow.getNeighbours(last);
            points.remove(prev);
            if (points.isEmpty())     // No candidate -> consider next way
                continue;
            if (points.size() > 1)    // Ambiguous junction?
                return;

            // points contains exactly one element
            Node newPointCandidate = points.iterator().next();

            if ((newPoint != null) && (newPoint != newPointCandidate))
                return;         // Ambiguous junction, force to select next

            newPoint = newPointCandidate;
        }
        if (newPoint != null) {
            List<Node> modNodes = follower.getNodes();
            if (reversed) {
                modNodes.add(0, newPoint);
            } else {
                modNodes.add(newPoint);
            }
            boolean isClosed = modNodes.size() >= 3 && modNodes.get(modNodes.size()-1) == modNodes.get(0);
            UndoRedoHandler.getInstance().add(new SequenceCommand(tr("Follow line"),
                    new ChangeNodesCommand(ds, follower, modNodes),
                    new SelectCommand(ds, isClosed // see #10028 - unselect last node when closing a way
                            ? Arrays.<OsmPrimitive>asList(follower)
                            : Arrays.<OsmPrimitive>asList(follower, newPoint)
                    ))
            );
            // "viewport following" mode for tracing long features
            // from aerial imagery or GPS tracks.
            if (DrawAction.VIEWPORT_FOLLOWING.get()) {
                map.mapView.smoothScrollTo(newPoint.getEastNorth());
            }
        }
    }
}
