// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.dialogs;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.RelationMember;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.dialogs.ConflictDialog.ConflictPainter;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.testutils.annotations.Main;
import org.openstreetmap.josm.testutils.annotations.Projection;

/**
 * Unit tests of {@link ConflictDialog} class.
 */
@Main
@Projection
class ConflictDialogTest {
    /**
     * Unit test of {@link ConflictDialog#ConflictDialog}.
     */
    @Test
    void testConflictDialog() {
        assertDoesNotThrow(ConflictDialog::new);
    }

    /**
     * Unit test of {@link ConflictDialog#getColor} method.
     */
    @Test
    void testGetColor() {
        assertEquals(Color.gray, ConflictDialog.getColor());
    }

    /**
     * Unit tests of {@link ConflictPainter} class.
     */
    @Test
    void testConflictPainter() {
        MainApplication.getLayerManager().addLayer(new OsmDataLayer(new DataSet(), "", null));
        ConflictPainter cp = new ConflictPainter(MainApplication.getMap().mapView,
                new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR).createGraphics());
        Node n1 = new Node(1, 1);
        n1.setCoor(new LatLon(1, 1));
        Node n2 = new Node(2, 1);
        n2.setCoor(new LatLon(2, 2));
        Way w = new Way(1, 1);
        w.addNode(n1);
        w.addNode(n2);
        Relation r = new Relation(1, 1);
        r.addMember(new RelationMember("outer", w));
        cp.visit(n1);
        cp.visit(n2);
        cp.visit(w);
        cp.visit(r);
    }
}
