package backend;

import DB.DBconnection;

import java.sql.DriverManager;

import java.sql.*;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Invoice {
    private int part_id;
    private int d1, d2, d3, d4, d5, d6, d7;
    private int ctr;
    private int sales;

    public Invoice() {
        part_id = 0;
        sales = 0;
        d1 = d2 = d3 = d4 = d5 = d6 = d7 = 0;
        ctr = 0;
    }

    public void setctr() {
        int status = 0;
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;
            ResultSet rs;
            ps = con.prepareStatement("SELECT * FROM temp");
            rs = ps.executeQuery();
            rs.next();
            this.ctr = rs.getInt("ctr");
            ps.executeQuery();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getctr() { return this.ctr; }
    public void savectr() {
        int status = 0;
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("UPDATE temp SET ctr = ? WHERE ctr = ?");
            ps.setInt(1, (ctr) % 7 + 1);
            ps.setInt(2, ctr);
            ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int updateSales(int partID, int qty) {
        int status =0;
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;
            ResultSet rs;
            setctr();
            String day = "d" + ctr;
            ps = con.prepareStatement("SELECT ? FROM invoice WHERE part_id = ?");
            ps.setString(1, day);
            ps.setInt(2, partID);
            rs = ps.executeQuery();
            rs.next();
            this.sales = rs.getInt(day);
            ps = con.prepareStatement("UPDATE invoice SET d1 = ? WHERE part_id = ?");
            //ps.setString(1, day);
            ps.setInt(1, sales + qty);
            ps.setInt(2, partID);
            status = ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (status > 0) ? 1 : 0;
    }
}