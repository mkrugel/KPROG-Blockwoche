package controller.view_plotter;

import java.awt.Dimension;

/**
 * Interface to listen for resize changes.
 */
public interface ResizeListener {
    void resizePane(Dimension dimension);
}
