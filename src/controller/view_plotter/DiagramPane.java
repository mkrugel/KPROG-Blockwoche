package controller.view_plotter;



import controller.controller_plotter.DrawAssistant;
import controller.controller_plotter.ScaleManager;
import controller.model_plotter.CustomPoint;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This class paints the diagram with help of the DrawAssistant. It
 * implements a ResizeListener with what the Panel gets resized in the
 * ScrollPane.
 */
public class DiagramPane extends JPanel implements ResizeListener {
    /** DrawAssistant to draw the components of the diagram */
    private DrawAssistant drawAssistant;
    /** Label for the x axis */
    private JLabel jLabelX;
    /** Label for the y axis */
    private JLabel jLabelY;
    /** Identifier to show lines */
    private boolean showLines;

    /**
     * Constructor initializes the DrawPane and the the Labels
     * @param dataPoints the dataPoints with x and y coordinates.
     * @param width the width of the pane
     * @param height the height of the pane
     * @param showLines identifier to show lines between the datapoints.
     * @param xLabel
     * @param yLabel
     */
    public DiagramPane(List<CustomPoint> dataPoints,
                       int width,
                       int height,
                       boolean showLines,
                       String xLabel,
                       String yLabel) {
        setPreferredSize(new Dimension(width, height));
        ScaleManager scaleManager = new ScaleManager(width, height);
        this.drawAssistant = new DrawAssistant(this, dataPoints, scaleManager);
        this.showLines = showLines;

        this.jLabelX = new JLabel(xLabel);
        this.jLabelY = new JLabel(yLabel);
        this.add(jLabelX);
        this.add(jLabelY);
    }

    /**
     * Method that draws all components of the diagram.
     * @param graphics the graphics object, which creates the graphical components
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // init scales and pass it to the drawAssistant
        ScaleManager scaleManager = new ScaleManager(getWidth(), getHeight());
        this.drawAssistant.setScales(scaleManager);

        // draw diagram
        setLabelLocations(scaleManager.getXLabelPosition(), scaleManager.getYLabelPosition());
        this.drawAssistant.drawBackground(g2);
        this.drawAssistant.drawAxes(g2);
        this.drawAssistant.drawGrid(g2);
        this.drawAssistant.drawPoints(g2);
        if (this.showLines) {
            this.drawAssistant.drawLines(g2);
        }
    }

    /**
     * Resizes the DiagramPane and gets called in ResizeListener.
     * @param dimension the new dimension, which contains the width and height.
     */
    @Override
    public void resizePane(Dimension dimension) {
        this.setPreferredSize(dimension);
        this.revalidate();
    }

    /**
     * Sets the location of the labels.
     * @param xLabelPos the x pos as a Point with x and y coords.
     * @param yLabelPos the y pos as a Point with x and y coords.
     */
    private void setLabelLocations(Point xLabelPos, Point yLabelPos) {
        this.jLabelX.setLocation(xLabelPos);
        this.jLabelX.setHorizontalAlignment(SwingConstants.RIGHT);

        this.jLabelY.setLocation(yLabelPos);
    }

    /**
     * Replaces the current dataPoints list and repaints the diagram according to the new points
     * @param points list which contains all new datapoints.
     */
    protected void addPointList(List<CustomPoint> points) {
        this.drawAssistant.setDataPoints(points);
        this.revalidate();
    }

    /**
     * Adds a point to the current diagram.
     * @param point the new point.
     */
    protected void addPoint(CustomPoint point) {
        List<CustomPoint> dataPoints = this.drawAssistant.getDataPoints();
        dataPoints.add(point);
        this.drawAssistant.setDataPoints(dataPoints);
        this.revalidate();
    }
}
