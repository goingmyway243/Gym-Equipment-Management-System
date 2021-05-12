
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
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
public class PieChart extends Thread {
    
    AdminDashBoard admDb;
    
    public PieChart(java.awt.Frame parent) {
        admDb = (AdminDashBoard) parent;
    }
    
    @Override
    public void run() {
        org.knowm.xchart.PieChart chart = new PieChartBuilder().width(800).height(600).title("Biểu Đồ Thiết Bị").theme(ChartTheme.XChart).build();
        // Customize Chart
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setAnnotationType(AnnotationType.LabelAndPercentage);
        chart.getStyler().setAnnotationDistance(1.15);
        chart.getStyler().setPlotContentSize(.7);
        chart.getStyler().setStartAngleInDegrees(90);
        
        String query = "SELECT status, count(status) from gym_equipments GROUP BY status";
        
        class StatusAndNumber {
            
            String status;
            float number;
            
            public StatusAndNumber(String status, int number) {
                this.status = status;
                this.number = number;
            }
            
        }
        List<StatusAndNumber> statusNumber = new ArrayList<>();
        try {
            Connection con = ConnectMysql.getConnectDB();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                statusNumber.add(new StatusAndNumber(rs.getString(1), rs.getInt(2)));
            }
            
            float total = 0;
            for (StatusAndNumber sN : statusNumber) {
                total += sN.number;
            }
            
            for (StatusAndNumber sN : statusNumber) {
                // Series
                chart.addSeries(sN.status, Math.round(sN.number / total * 100));
            }
            
        } catch (Exception e) {
        }

        // Show it
        SwingWrapper<org.knowm.xchart.PieChart> sWPC = new SwingWrapper<org.knowm.xchart.PieChart>(chart);
        
        JFrame frame = sWPC.displayChart();
        javax.swing.SwingUtilities.invokeLater(
                () -> frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)
        );
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                admDb.setIsPopupShow(false);
            }
        });
        
        try {
            // Save it
            BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.PNG);
        } catch (IOException ex) {
            Logger.getLogger(PieChart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            // or save it in high-res
            BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapFormat.PNG, 300);
        } catch (IOException ex) {
            Logger.getLogger(PieChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
