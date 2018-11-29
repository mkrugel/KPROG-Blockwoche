package controller.view_plotter;



import controller.model_plotter.CustomPoint;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import java.util.List;

import javax.swing.*;

/**
 * @author Marvin Hasenmayer
 *
 */
public class PlotterPane extends JFrame implements GraphPlotter {
    private final int SCROLLBAR_PAD = 10;
    private final DiagramPane diagramPane;

    /**
	 * Creates a frame and a ScollPane inside of it. The diagram will be drawn inside the ScrollPane.
	 */
	public PlotterPane(List<CustomPoint> dataPoints,
                       int diagramWidth,
                       int diagramHeight,
                       boolean showLines,
                       String xLabel,
                       String yLabel,
                       String title) {
        this.diagramPane = new DiagramPane(dataPoints, diagramWidth, diagramHeight, showLines, xLabel, yLabel);

        JScrollPane scrollPane = new JScrollPane(diagramPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 0, diagramWidth + SCROLLBAR_PAD, diagramHeight + SCROLLBAR_PAD);

        WindowResizeListener windowResizeListener = new WindowResizeListener();
        addComponentListener(windowResizeListener);

        //create a window, in which the contentPane is stored.
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);
        setVisible(true);
	}

    @Override
    public void addPointList(List<CustomPoint> points) {
        this.diagramPane.addPointList(points);
    }


    @Override
    public void addPoint(CustomPoint point) {
        this.diagramPane.addPoint(point);
    }

    /**
     * This class listens for resize changes of the JFrame window.
     */
	private class WindowResizeListener extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent componentEvent) {
            int width = componentEvent.getComponent().getWidth();
            int height = componentEvent.getComponent().getHeight();

            diagramPane.resizePane(new Dimension(width, height));
            //diagramPane = new DiagramPane(dataPoints, width, height, showLines);

            System.out.println(componentEvent.paramString() + " | WIDTH: " + width + " | HEIGHT: " + height);
            // create DiagramPane and add it to a ScrollbarPane.

        }
    }

    /**
     * Method to sort points.
     * @param points the points to sort.
     * @return sorted points list.
     */
	private List<CustomPoint> sortPoints(List<CustomPoint> points) {
        Collections.sort(points, new Comparator<CustomPoint>() {
            public int compare(CustomPoint firsPoint, CustomPoint secondPoint) {
                return Double.compare(firsPoint.getX(), secondPoint.getX());
            }
        });
        return points;
    }
}
