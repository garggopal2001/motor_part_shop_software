package backend;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.*;
import java.time.LocalDate;
//import java.util.Date;
import DB.DBconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Revenue {
    public java.util.Date date;
    public int total_revenue;

    public Revenue() {
        date = new java.util.Date();
        total_revenue = 0;
    }

    public void getLastRow() {
        int status =0;
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;
            ResultSet rs;
            ps = con.prepareStatement("SELECT * FROM revenue ORDER BY date DESC");

            rs = ps.executeQuery();
            rs.next();
            total_revenue = rs.getInt("total_revenue");
            date = rs.getDate("date");
            ps.executeQuery();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int endday() {
        getLastRow();
        long timeadj = 24*60*60*1000;
        Date newDate = new Date (date.getTime ()+timeadj);
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;

            ps = con.prepareStatement("INSERT INTO revenue(date, total_revenue) VALUES(?, ?)");
            ps.setDate(1, newDate);
            ps.setInt(2, 0);
            ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }

        Invoice inv = new Invoice();
        inv.setctr();
        inv.savectr();
        return total_revenue;
    }

    public int addrevenue(int Revenue) {
        getLastRow();
        int status = 0;
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("UPDATE revenue SET total_revenue = ? WHERE date = ?");
            ps.setInt(1, total_revenue + Revenue);
            ps.setDate(2, (Date) date);
            status = ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (status > 0) ? 1 : 0;
    }
}