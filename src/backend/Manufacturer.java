package backend;
import DB.DBconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Manufacturer {
    private Integer manufacturer_id;
    private String name;
    private String address;
    private String phone_no;

    public Manufacturer() {
        this.manufacturer_id = 0;
        this.name = "Not Set";
        this.address = "Not Set";
        this.phone_no = "Not Set";
    }

    public Integer getmanufacturer_id() {
        return this.manufacturer_id;
    }

    public String getname() {
        return this.name;
    }

    public String getaddress() {
        return this.address;
    }

    public String getphone_no() {
        return this.phone_no;
    }

    public int setname(String Name) {
        if(Name.matches("[a-zA-Z]+") == true) {
            this.name = Name;
            return 1;
        }
        return 0;
    }

    public int setaddress(String Address) {
        this.address = Address;
        return 1;
    }

    public int setphone_no(String Phone_No) {
        if(Phone_No.length() != 10) return 0;
        if(Phone_No.matches("[0-9]+") == false) return 0;
        this.phone_no = Phone_No;
        return 1;
    }

    public int save() {
        int status = 0;
        try {
            Connection con = DBconnection.getConnection();

            PreparedStatement ps = con.prepareStatement("INSERT INTO manufacturer(name, address, phone_no) VALUES(?, ?, ?)");
            ps.setString(1, this.name);
            ps.setString(2, this.address);
            ps.setString(3, this.phone_no);
            status = ps.executeUpdate();
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

            PreparedStatement ps = con.prepareStatement("DELETE FROM item WHERE manufacturer_id = ?");
            ps.setInt(1, this.manufacturer_id);
            status = ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

}

