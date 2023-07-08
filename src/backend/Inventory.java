/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.sql.DriverManager;

import java.sql.*;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author achcha
 */

// Inventory class, modelled as a singleton
import DB.DBconnection;
public class Inventory {
    public static HashMap<String, HashMap<Integer, HashMap<String, Item>>> searchMap = new HashMap<String, HashMap<Integer, HashMap<String, Item>>>();
    public static HashMap<Integer, Item> itemsList = new HashMap<Integer, Item>();
    public static HashMap<Integer, Manufacturer> manufacturersList = new HashMap<Integer, Manufacturer>();
    public static HashMap<String, Integer> manufacturerIDList = new HashMap<String, Integer>();
    public static HashMap<Integer, Item> OrderList = new HashMap<Integer, Item>();
    public static HashMap<String, Integer> itemIDList = new HashMap<String, Integer>();
    private static Inventory instance = null;

    // private constructor for a singleton class
    private Inventory() {
    }

    public static Inventory type() {
        if(instance == null)
            instance = new Inventory();
        return instance;
    }

    // Function to reteieve the existing data from the database when the software is started
    public void retrieveData(){
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;
            ResultSet rs;

            ps = con.prepareStatement("SELECT * FROM manufacturer");
            rs = ps.executeQuery();
            while(rs.next()) {
                int uID = rs.getInt("manufacturer_id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String phoneno= rs.getString("phone_no");
                Manufacturer manObj = new Manufacturer();
                manObj.setname(name);
                manObj.setaddress(address);
                manObj.setphone_no(phoneno);
                Inventory.manufacturersList.put(uID, manObj);
                Inventory.manufacturerIDList.put(name, uID);
            }

            ps = con.prepareStatement("SELECT * FROM item");
            rs = ps.executeQuery();
            while(rs.next()) {
                int uID = rs.getInt("part_id");
                String type = rs.getString("type");
                int manufacturerID = rs.getInt("manufacturer_id");
                int price = rs.getInt("price");
                int quantity = rs.getInt("quantity");
                String rackName = rs.getString("rack_name");
                String vehicleType = rs.getString("vehicle_type");
                Item itemObj = new Item();
                itemObj.setpart_id(String.valueOf(uID));
                itemObj.settype(type);
                itemObj.setmanufacturer_id(String.valueOf(manufacturerID));
                itemObj.setvehicle_type(vehicleType);
                itemObj.setprice(String.valueOf(price));
                itemObj.setquantity(String.valueOf(quantity));
                itemObj.setrack_name(rackName);

                Inventory.itemsList.put(uID, itemObj);

                if(Inventory.searchMap.containsKey(type)) {
                    if(Inventory.searchMap.get(type).containsKey((manufacturerID))) {
                        Inventory.searchMap.get(type).get(manufacturerID).put(vehicleType, itemObj);
                    }
                    else {
                        HashMap<String, Item> temp1 = new HashMap<>();
                        temp1.put(vehicleType, itemObj);

                        Inventory.searchMap.get(type).put(manufacturerID, temp1);
                    }
                }
                else {
                    HashMap<String, Item> temp1 = new HashMap<>();
                    temp1.put(vehicleType, itemObj);

                    HashMap<Integer, HashMap<String, Item>> temp2 = new HashMap<>();
                    temp2.put(manufacturerID, temp1);

                    Inventory.searchMap.put(type, temp2);
                }
            }

            ps.close();

        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getOrderList() {
        try {
            Connection con = DBconnection.getConnection();
            PreparedStatement ps;
            ResultSet rs;

            ps = con.prepareStatement("SELECT * FROM item");
            rs = ps.executeQuery();
            while (rs.next()) {
                int part_id = rs.getInt("part_id");
                int quantity= rs.getInt("quantity");
                Item iObj = new Item();

                if(quantity < iObj.getThreshold(part_id)) {
                    String type = rs.getString("type");
                    int manufacturer_id = rs.getInt("manufacturer_id");
                    String vehicle_type = rs.getString("vehicle_type");
                    int price = rs.getInt("price");
                    String rack_name = rs.getString("rack_name");

                    iObj.setpart_id("" + part_id);
                    iObj.settype("" + type);
                    iObj.setprice("" + price);
                    iObj.setquantity("" + quantity);
                    iObj.setrack_name("" + rack_name);
                    iObj.setmanufacturer_id("" + manufacturer_id);
                    iObj.setvehicle_type("" + vehicle_type);

                    Inventory.OrderList.put(part_id, iObj);
                }
//                Inventory.manufacturerIDList.put(name, uID);
            }
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int removeItem(int itemUID){
        int status = 0;
        try {
            Item currItem = Inventory.itemsList.get(itemUID);
            Manufacturer currManufacturer = Inventory.manufacturersList.get(currItem.getmanufacturer_id());

            Inventory.searchMap.get(currItem.gettype()).get(currItem.getmanufacturer_id()).remove(currItem.getvehicle_type());
            Inventory.itemsList.remove(currItem.getpart_id());

            Connection con = DBconnection.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM item WHERE part_id = ?");
            ps.setInt(1, currItem.getpart_id());
            status = ps.executeUpdate();

            ps.close();
        } catch (java.sql.SQLException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (status > 0) ? 1 : 0;
    }

}