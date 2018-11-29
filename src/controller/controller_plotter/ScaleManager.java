package controller.controller_plotter;

import java.awt.*;

/**
 * Class to hold information about the drawing scales.
 */
public class ScaleManager {
    private final int WIDTH;
    private final int HEIGHT;
    /** Gap between the left of the window and the start drawing point of the diagram */
    private final int LEFT_GAP;
    /** Gap between the top of the window and the start drawing point of the diagram */
    private final int TOP_GAP;
    /** Padding to Frame */
    private final int PADDING = 30;
    /** Height of label */
    private final int LABEL_HEIGHT = 15;
    /** Width of label */
    private final int LABEL_WIDTH = 100;
    /** Padding of a label */
    private final int LABEL_PAD = 20;

    /**
     * Constructor
     * @param width the width
     * @param height the height
     */
    public ScaleManager(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.LEFT_GAP = PADDING + LABEL_PAD;
        this.TOP_GAP = PADDING;
        System.out.println("Scale Manger created. WIDTH: " + width + "| HEIGHT: " + height);
    }

    public Dimension calcNewDimension(int biggestX, int maxXValue, int biggestY) {
        int maxXScale = getDIAGRAM_WIDTH() / maxXValue;
        int diagramWidth = maxXScale * biggestX;

        int maxYScale = getDIAGRAM_HEIGHT() / maxXValue;
        int diagramHeight = maxYScale * biggestY;

        int height = diagramHeight + (2 * PADDING) + LABEL_PAD;
        int width = diagramWidth + (2 * PADDING) + LABEL_PAD;
        return new Dimension(width, height);
    }

    public int getXScale (int maxX) {
        return maxX > 0 ? getDIAGRAM_WIDTH() / maxX : 0;
    }

    public int getYScale (int maxY) {
        return maxY > 0 ? getDIAGRAM_HEIGHT() / maxY : 0;
    }

    public int getLEFT_GAP() {
        return this.LEFT_GAP;
    }

    public int getRIGHT_GAP() {
        return this.WIDTH - this.PADDING;
    }

    public int getTOP_GAP() {
        return this.TOP_GAP;
    }

    public int getBOTTOM_GAP() {
        return this.HEIGHT - this.PADDING - this.LABEL_PAD;
    }

    public int getDIAGRAM_WIDTH() {
        return this.WIDTH - (2 * this.PADDING) - this.LABEL_PAD;
    }

    public int getDIAGRAM_HEIGHT() {
        return this.HEIGHT - (2 * this.PADDING) - this.LABEL_PAD;
    }

    public Point getXLabelPosition() {
        return new Point(getRIGHT_GAP() - this.LABEL_WIDTH, getBOTTOM_GAP() + this.LABEL_HEIGHT);
    }

    public Point getYLabelPosition() {
        return new Point(this.LABEL_WIDTH / 2, this.LABEL_HEIGHT / 2);
    }

}
