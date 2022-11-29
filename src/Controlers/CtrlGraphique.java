package Controlers;

import Entities.DatasGraph;
import Tools.ConnexionBDD;

import javax.swing.plaf.PanelUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CtrlGraphique
{
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public CtrlGraphique() {
        cnx = ConnexionBDD.getCnx();
    }


    public HashMap<Integer, Double> getMoyByAge(){
        HashMap<Integer, Double> moyByAge = new HashMap<>();

        try {
            ps = cnx.prepareStatement("SELECT ageEmp, round(AVG(salaireEmp),2) as moyenneSalaire\n" +
                    "FROM employe\n" +
                    "GROUP BY ageEmp\n" +
                    "ORDER BY ageEmp");

            rs = ps.executeQuery();

            while (rs.next()){
                moyByAge.put(rs.getInt("ageEmp"),rs.getDouble("moyenneSalaire"));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return moyByAge;
    }

    public HashMap<String, Integer> getPrctHetF(){
        HashMap<String,Integer> HetFprct = new HashMap<>();

        try {
            ps = cnx.prepareStatement("select sexe,  100*count(sexe)/(SELECT COUNT(*) from employe) as pourcentage\n" +
                    "from employe\n" +
                    "GROUP BY sexe");

            rs = ps.executeQuery();

            while (rs.next()){
                HetFprct.put(rs.getString("sexe"),rs.getInt("pourcentage"));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return HetFprct;
    }

    public HashMap<String, HashMap<String, Integer>> getSexeByTrancheAge(){
        HashMap<String, HashMap<String, Integer>> sexeByTrangeAge = new HashMap<>();

        try {
            ps = cnx.prepareStatement("SELECT sexe,'+50' as tranche, COUNT(sexe) as nb\n" +
                    "FROM employe\n" +
                    "WHERE ageEmp > 50\n" +
                    "group by sexe\n" +
                    "UNION\n" +
                    "SELECT sexe,'10-19', COUNT(sexe)\n" +
                    "FROM employe\n" +
                    "WHERE ageEmp BETWEEN 10 and 19\n" +
                    "group by sexe\n" +
                    "UNION\n" +
                    "SELECT sexe,'20-29', COUNT(sexe)\n" +
                    "FROM employe\n" +
                    "WHERE ageEmp BETWEEN 20 and 29\n" +
                    "group by sexe\n" +
                    "UNION\n" +
                    "SELECT sexe,'30-39', COUNT(sexe)\n" +
                    "FROM employe\n" +
                    "WHERE ageEmp BETWEEN 30 and 39\n" +
                    "group by sexe\n" +
                    "UNION\n" +
                    "SELECT sexe,'40-49', COUNT(sexe)\n" +
                    "FROM employe\n" +
                    "WHERE ageEmp BETWEEN 40 and 49\n" +
                    "group by sexe");

            rs = ps.executeQuery();

            Integer nb;
            while (rs.next()){
                HashMap<String, Integer> trancheEtNb = new HashMap<>();
                if (rs.getString("sexe").compareTo("Homme") == 0){
                    nb = -rs.getInt("nb");
                }
                else {
                    nb = rs.getInt("nb");
                }
                if (sexeByTrangeAge.containsKey(rs.getString("sexe"))){

                    sexeByTrangeAge.get(rs.getString("sexe")).put(rs.getString("tranche"), nb);
                }
                else {
                    trancheEtNb.put(rs.getString("tranche"), nb);
                    sexeByTrangeAge.put(rs.getString("sexe"),trancheEtNb);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return sexeByTrangeAge;
    }

    public HashMap<Integer, String[]> getMontantByMagBySem(){
        HashMap<Integer, String[]> montantByMagBySem = new HashMap<>();

        try {
            ps = cnx.prepareStatement("select nomSemestre, nomMagasin, montant\n" +
                    "from vente\n" +
                    "GROUP BY nomSemestre, nomMagasin");

            rs = ps.executeQuery();

            int i = 1;
            while(rs.next())
            {
                montantByMagBySem.put(i, new String[]{rs.getString("nomSemestre"),rs.getString("nomMagasin"),rs.getString("montant")});
                i++;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return montantByMagBySem;
    }

}
