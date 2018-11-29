package controller.controller_plotter;



import controller.model_plotter.CustomPoint;
import controller.view_plotter.ResizeListener;

import java.awt.*;
import java.util.List;

/**
 * A class to separate the drawing logic and calculations.
 */
public class DrawAssistant {
    /** Listener to resize the DiagramPane */
    private ResizeListener resizeListener;
    /** Datapoints to draw */
    private List<CustomPoint> dataPoints;
    /** Holds dimension and scaling parameters */
    private ScaleManager scales;
    /** The biggest x values of the datapoints array */
    private int maxX;
    /** The biggest y values of the datapoints array */
    private int maxY;
    /** Value for scaling the axes numbers */
    private int maxXValue;
    /** The maximums of the last iteration */
    private Point lastMax;
    /** colors for drawing */
    private final Color LINE_COLOR = new Color(44, 102, 230, 180);
    private final Color POINT_COLOR = new Color(100, 100, 100, 180);
    private final Color GRID_COLOR = new Color(200, 200, 200, 200);
    /** Lines stroke */
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    /** Radius of dot */
    private final int DOT_RADIUS = 10;

    /**
     * Constructor for initialization of the DrawAssistant.
     * @param resizeListener object to call if the DiagramPane needs to be resized.
     * @param dataPoints list where points are stored
     * @param scales object which holds scale information
     */
    public DrawAssistant(ResizeListener resizeListener, List<CustomPoint> dataPoints, ScaleManager scales) {
        this.resizeListener = resizeListener;
        this.lastMax = new Point(0,0);
        setScales(scales);
        setDataPoints(dataPoints);
    }

    /**
     * Sets the points to draw. Checks if the DiagramPane needs to be resized according to
     * the values in the dataPoints list.
     * @param dataPoints list, which holds point data.
     */
    public void setDataPoints(List<CustomPoint> dataPoints) {
        this.dataPoints = dataPoints;
        Point max = getMax(dataPoints);
        this.maxX = max.x + 1;
        this.maxY = max.y + 1;
        checkForResizing();
        System.out.println("new datapoints set! Max values are: " + max);
    }

    public List<CustomPoint> getDataPoints() {
        return this.dataPoints;
    }

    /**
     * Sets the scaleManager object. Defines the new maxXValue.
     * @param scales holds scale information.
     */
    public void setScales(ScaleManager scales) {
        this.scales = scales;
        this.maxXValue = this.scales.getDIAGRAM_WIDTH() / 20;
    }

    /**
     * calculates the biggest x and y values of the points list.
     * @param points list of data points.
     * @return the max values.
     */
    private Point getMax(List<CustomPoint> points) {
        int maxX = 0;
        int maxY = 0;
        if (points.size() > 1) {
            for (int i = 0; i < points.size(); i++) {
                int x = points.get(i).x;
                int y = points.get(i).getIntY();
                if (x > maxX) {
                    maxX = x;
                }
                if (y > maxY) {
                    maxY = y;
                }
            }
        }
        return new Point(maxX,  maxY);
    }

    /**
     * Checks if the DiagramPane needs to be resized.
     */
    private void checkForResizing() {
        if (this.maxX > this.lastMax.x && this.maxY > this.maxXValue) {
            Dimension newDimension = this.scales.calcNewDimension(this.maxX, this.maxXValue, this.maxY);
            System.out.println("new DImension: " + newDimension);
            resizeListener.resizePane(newDimension);
        }
        this.lastMax = new Point(this.maxX, this.maxY);
    }

    /**
     * Draws the background.
     * @param g2 the graphics object, which can draw components.
     */
    public void drawBackground(Graphics2D g2) {
        // draw background
        g2.setColor(Color.WHITE);
        g2.fillRect(this.scales.getLEFT_GAP(),
                this.scales.getTOP_GAP(),
                this.scales.getDIAGRAM_WIDTH(),
                this.scales.getDIAGRAM_HEIGHT());
    }

    /**
     * Draws the x and y axes.
     * @param g2 the graphics object, which can draw components.
     */
    public void drawAxes(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawLine(this.scales.getLEFT_GAP(),
                this.scales.getTOP_GAP(),
                this.scales.getLEFT_GAP(),
                this.scales.getBOTTOM_GAP()); // y axes
        g2.drawLine(this.scales.getLEFT_GAP(),
                this.scales.getBOTTOM_GAP(),
                this.scales.getRIGHT_GAP(),
                this.scales.getBOTTOM_GAP());  // x axes
    }

    /**
     * Draws the grid lines and the numbers of the axes.
     * @param g2 the graphics object, which can draw components.
     */
    public void drawGrid(Graphics2D g2) {
        FontMetrics metrics = g2.getFontMetrics();
        //horizontal strokes
        for (int i = 0; i <= maxY; i++) {
            int x1 = this.scales.getLEFT_GAP();
            int y1 = this.scales.getBOTTOM_GAP() - (i * this.scales.getYScale(maxY));
            int x2 = this.scales.getRIGHT_GAP();
            int y2 = y1;
            // x axes numbers
            if (i % ((maxY / this.maxXValue) + 1) == 0) {
                g2.setPaint(GRID_COLOR);
                g2.drawLine(x1, y1, x2, y2);
                g2.setColor(Color.BLACK);
                String yLabel = i + "";
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x1 - labelWidth - 5, y1 + (metrics.getHeight() / 2) - 3);
            }
        }

        //vertical strokes
        for (int i = 0; i <= maxX; i++) {
            int x1 = this.scales.getLEFT_GAP() + (i * this.scales.getXScale(maxX));
            int y1 = this.scales.getBOTTOM_GAP();
            int x2 = x1;
            int y2 = this.scales.getTOP_GAP();
            if (i % ((maxX / this.maxXValue) + 1) == 0) {
                g2.setPaint(GRID_COLOR);
                g2.drawLine(x1, y1, x2, y2);

                // y axes numbers
                g2.setColor(Color.BLACK);
                String xLabel = i + "";
                int labelWidth = metrics.stringWidth(xLabel);
                g2.drawString(xLabel, x1 - labelWidth / 2, y1 + metrics.getHeight() + 3);
            }
        }
    }

    /**
     * Draws all Points, according to the given list of CustomPoints.
     * @param g2 the graphics object, which can draw components.
     */
    public void drawPoints(Graphics2D g2) {
        if (this.dataPoints.size() > 0) {
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(oldStroke);
            g2.setColor(POINT_COLOR);
            for (CustomPoint point: this.dataPoints) {
                int x = (this.scales.getLEFT_GAP() +
                        (point.getX() * this.scales.getXScale(maxX)) - DOT_RADIUS / 2);
                int y = (int) (this.scales.getBOTTOM_GAP() -
                        (point.getY() * this.scales.getYScale(maxY) + DOT_RADIUS / 2));
                g2.fillOval(x, y, DOT_RADIUS, DOT_RADIUS);
            }
        }

    }

    /**
     * Draws lines between the data points.
     * @param g2 the graphics object, which can draw components.
     */
    public void drawLines(Graphics2D g2) {
        if (this.dataPoints.size() > 1) {
            g2.setColor(LINE_COLOR);
            g2.setStroke(GRAPH_STROKE);
            for (int i = 0; i < this.dataPoints.size() - 1; i++) {
                CustomPoint point = this.dataPoints.get(i);
                CustomPoint nextPoint = this.dataPoints.get(i + 1);
                int x1 = this.scales.getLEFT_GAP() + (point.getX() * this.scales.getXScale(maxX));
                int y1 = (int) (this.scales.getBOTTOM_GAP() - (point.y * this.scales.getYScale(maxY)));
                int x2 = this.scales.getLEFT_GAP() + (nextPoint.getX() * this.scales.getXScale(maxX));
                int y2 = (int) (this.scales.getBOTTOM_GAP() - (nextPoint.y * this.scales.getYScale(maxY)));
                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }


}
