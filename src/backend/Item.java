package backend;
import DB.DBconnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Item {
    private Integer part_id;
    private String type;
    private Integer manufacturer_id;
    private String vehicle_type;
    private Integer price;
    private Integer quantity;
    private String rack_name;
    private int Threshold;

    public Item() {
        this.part_id = 0;
        this.type = "Not Set";
        this.manufacturer_id = 0;
        this.vehicle_type = "Not Set";
        this.price = 0;
        this.quantity = 0;
        this.rack_name = "Not Set";
        this.Threshold = 0;
    }

    public Integer getpart_id() {
        return this.part_id;
    }

    public Integer getquantity() {
        return this.quantity;
    }

    public String gettype() {
        return this.type;
    }

    public Integer getprice() {
        return this.price;
    }

    public String getvehicle_type() {
        return this.vehicle_type;
    }

    public String getrack_name() {
        return this.rack_name;
    }

    public Integer getmanufacturer_id() { return manufacturer_id; }

    public int settype(String Type) {
        if(Type.matches("[a-zA-Z]+") == true) {
            this.type = Type;
            return 1;
        }
        return 0;
    }

    public int setprice(String Price) {
        if(Price.matches("[0-9]+") == true) {
            this.price = Integer.parseInt(Price);
            return 1;
        }
        return 0;
    }

    public int setquantity(String Quantity) {
        if(Quantity.matches("[0-9]+") == true) {
            this.quantity = Integer.parseInt(Quantity);
            return 1;
        }
        return 0;
    } 

    public int setvehicle_type(String VehicleType) {
        if(VehicleType.matches("[a-zA-Z]+") == true) {
            this.vehicle_type = VehicleType;
            return 1;
        }
        return 0;
    }

    public int setrack_name(String Rack_Name) {
        this.rack_name = Rack_Name;
        return 1;
    }

    public int setmanufacturer_id(String manID) {
        this.manufacturer_id = Integer.parseInt(manID);
        return 1;
    }

    public int setpart_id(String partID) {
        this.part_id = Integer.parseInt(partID);
        return 1;
    }

    public int getlastpartID() {
        int status = 0;
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;
            ResultSet rs;
            ps = con.prepareStatement("SELECT * FROM item ORDER BY part_id DESC");
            rs = ps.executeQuery();
            rs.next();
            this.part_id = rs.getInt("part_id");
            ps.executeQuery();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (status > 0) ? 1 : 0;
    }

    public int save() {
        int status = 0;
        try {
            Connection con = DBconnection.getConnection();

            PreparedStatement ps = con.prepareStatement("INSERT INTO item(part_id, type, manufacturer_id,  vehicle_type, price, quantity, rack_name) VALUES(?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, this.part_id);
            ps.setString(2, this.type);
            ps.setInt(3, this.manufacturer_id);
            ps.setString(4, this.vehicle_type);
            ps.setInt(5, this.price);
            ps.setInt(6, this.quantity);
            ps.setString(7, rack_name);
            status = ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Connection con = DBconnection.getConnection();
            getlastpartID();
            PreparedStatement ps = con.prepareStatement("INSERT INTO invoice(part_id, d1, d2, d3, d4, d5, d6, d7) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, this.part_id);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.setInt(8, 0);
            status = status * ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ((status > 0) ? 1 : 0);
    }

    public int delete() {
        int status = 0;
        try {
            Connection con = DBconnection.getConnection();

            PreparedStatement ps = con.prepareStatement("DELETE FROM item WHERE part_id = ?");
            ps.setInt(1, this.part_id);
            status = ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public int addManufacturer(Integer Manufacturer_ID) {
        this.manufacturer_id = Manufacturer_ID;
        return 1;
    }

    public int updateStock(int change) {
        int status = 0;
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("UPDATE item SET quantity = ? WHERE part_id = ?");
            ps.setInt(1, this.quantity + change);
            ps.setInt(2, this.part_id);
            status = ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(change < 0) {
            try {
                Connection con = DBconnection.getConnection();
                ResultSet rs;
                Invoice inv = new Invoice();
                inv.setctr();
                String day = "d" + inv.getctr();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM invoice WHERE part_id = ?");
                ps.setInt(1, this.part_id);

                rs = ps.executeQuery();
                rs.next();
                int currSale = rs.getInt(day);
                switch(inv.getctr()) {
                    case 1:
                        ps = con.prepareStatement("UPDATE invoice SET d1 = ? WHERE part_id = ?");
                        break;
                    case 2:
                        ps = con.prepareStatement("UPDATE invoice SET d2 = ? WHERE part_id = ?");
                        break;
                    case 3:
                        ps = con.prepareStatement("UPDATE invoice SET d3 = ? WHERE part_id = ?");
                        break;
                    case 4:
                        ps = con.prepareStatement("UPDATE invoice SET d4 = ? WHERE part_id = ?");
                        break;
                    case 5:
                        ps = con.prepareStatement("UPDATE invoice SET d5 = ? WHERE part_id = ?");
                        break;
                    case 6:
                        ps = con.prepareStatement("UPDATE invoice SET d6 = ? WHERE part_id = ?");
                        break;
                    case 7:
                        ps = con.prepareStatement("UPDATE invoice SET d7 = ? WHERE part_id = ?");
                        break;
                    default:
                }
                ps.setInt(1, currSale - change);
                ps.setInt(2, this.part_id);
                ps.executeUpdate();
                ps.close();
            } catch (java.sql.SQLException ex) {
                Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return (status > 0) ? 1 : 0;
    }

    public int getThreshold(int partID) {
        int avg = 0;
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;
            ResultSet rs;
            ps = con.prepareStatement("SELECT * FROM invoice WHERE part_id = ?");
            ps.setInt(1, partID);
            rs = ps.executeQuery();
            rs.next();
            avg += rs.getInt("d1");
            avg += rs.getInt("d2");
            avg += rs.getInt("d3");
            avg += rs.getInt("d4");
            avg += rs.getInt("d5");
            avg += rs.getInt("d6");
            avg += rs.getInt("d7");

            avg = avg / 7;
            ps.executeQuery();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        return avg;
    }
}

