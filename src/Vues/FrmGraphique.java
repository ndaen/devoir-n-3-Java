package Vues;

import Controlers.CtrlGraphique;
import Tools.ConnexionBDD;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class FrmGraphique extends JFrame{
    private JPanel pnlGraph1;
    private JPanel pnlGraph2;
    private JPanel pnlGraph3;
    private JPanel pnlGraph4;
    private JPanel pnlRoot;
    CtrlGraphique ctrlGraphique;

    public FrmGraphique() throws SQLException, ClassNotFoundException {
        this.setTitle("Devoir graphique");
        this.setContentPane(pnlRoot);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);


        ConnexionBDD cnx = new ConnexionBDD();
        ctrlGraphique = new CtrlGraphique();

        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        for (Integer value: ctrlGraphique.getMoyByAge().keySet()){
            dataset1.setValue(ctrlGraphique.getMoyByAge().get(value), (Comparable) "", value.toString());
        }

        JFreeChart chart1 =ChartFactory.createLineChart(
        "Moyenne des salaires par âge",
                "Âge",
                "Salaire(€)",
                dataset1,
                PlotOrientation.VERTICAL, false,false, false
        );

        CategoryAxis axis = chart1.getCategoryPlot().getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        ChartPanel graph1 = new ChartPanel(chart1);
        pnlGraph1.add(graph1);
        pnlGraph1.validate();


        DefaultKeyedValues2DDataset dataset2 = new DefaultKeyedValues2DDataset ();

        for (String sexe: ctrlGraphique.getSexeByTrancheAge().keySet()){
            for (String tranche : ctrlGraphique.getSexeByTrancheAge().get(sexe).keySet()){
                dataset2.setValue(ctrlGraphique.getSexeByTrancheAge().get(sexe).get(tranche), sexe, tranche);
            }
        }

        JFreeChart chart2 = ChartFactory.createStackedBarChart(
                "Pyramide des âges",
                "Âges",
                "Hommes/Femmes",
                dataset2,
                PlotOrientation.HORIZONTAL,
                false,true, false
        );


        ChartPanel graph2 = new ChartPanel(chart2);
        pnlGraph2.add(graph2);
        pnlGraph2.validate();




        Double pourcent;
        String sexe;

        DefaultPieDataset dataset3 = new DefaultPieDataset<>();
        for (String value: ctrlGraphique.getPrctHetF().keySet()){
            pourcent = Double.valueOf(ctrlGraphique.getPrctHetF().get(value));
            sexe = value;
            dataset3.setValue(sexe, pourcent);
        }

        JFreeChart chart3 = ChartFactory.createRingChart(
                "Pourcentage des femmes et des hommes",
                dataset3,
                true, true, false
        );

        RingPlot plot = (RingPlot) chart3.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
        int i = 0;
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.yellow);
        colors.add(Color.magenta);
        for (String value: ctrlGraphique.getPrctHetF().keySet()){
            plot.setSectionPaint(value, colors.get(i));
            i++;
        }


        ChartPanel graph3 = new ChartPanel(chart3);
        graph3.setMouseWheelEnabled(true);
        pnlGraph3.add(graph3);
        pnlGraph3.validate();


        DefaultCategoryDataset dataset4 = new DefaultCategoryDataset();

        for (Map.Entry valeur: ctrlGraphique.getMontantByMagBySem().entrySet()){
            Double montant = Double.parseDouble(((String[])valeur.getValue())[2].toString());
            String nomSemestre = ((String[])valeur.getValue())[0].toString();
            String nomMagasin = ((String[])valeur.getValue())[1].toString();
            dataset4.setValue(montant, nomMagasin, nomSemestre);
        }

        JFreeChart chart4 = ChartFactory.createBarChart(
                "Montant des ventes par magasin",
                "Magasins",
                "Montant",
                dataset4,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel graph4 = new ChartPanel(chart4);
        pnlGraph4.add(graph4);
        pnlGraph4.validate();

    }
}
