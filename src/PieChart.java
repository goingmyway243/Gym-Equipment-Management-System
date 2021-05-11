
import java.io.IOException;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.Styler.ChartTheme;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author skqist225
 */
public class PieChart {

    public PieChart() {
    }
    
    

    public static void main(String[] args) throws IOException {

        // Create Chart
        org.knowm.xchart.PieChart chart = new PieChartBuilder().width(800).height(600).title("My Pie Chart").theme(ChartTheme.GGPlot2).build();

        // Customize Chart
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAnnotationType(AnnotationType.LabelAndPercentage);
        chart.getStyler().setAnnotationDistance(1.15);
        chart.getStyler().setPlotContentSize(.7);
        chart.getStyler().setStartAngleInDegrees(90);

        // Series
        chart.addSeries("Prague", 2);
        chart.addSeries("Dresden", 4);
        chart.addSeries("Munich", 34);
        chart.addSeries("Hamburg", 22);
        chart.addSeries("Berlin", 29);

        // Show it
        new SwingWrapper(chart).displayChart();

        // Save it
        BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.PNG);

        // or save it in high-res
        BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.PNG, 300);
    }

}
