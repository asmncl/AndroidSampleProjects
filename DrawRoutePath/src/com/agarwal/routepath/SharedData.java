/**
 * 
 */
package com.agarwal.routepath;

/**
 * @author Agarwal
 *
 */
public class SharedData {

    // this is a singleton class that provides a global data share for all of the
    // activities and services in the MDWrapper application       
    private static SharedData instance = null;


    private SharedData() {
        //randomizeServers();
    }

    // data to be shared
    private String APIKEY = "";
    private double src_lat = -1;
    private double src_lng = -1;
    private double dest_lat = -1;
    private double dest_lng = -1;
   
    /**
     * @return the aPIKEY
     */
    public String getAPIKEY() {
        return APIKEY;
    }

    /**
     * @param aPIKEY the aPIKEY to set
     */
    public void setAPIKEY(String aPIKEY) {
        APIKEY = aPIKEY;
    }

    /**
     * @return the src_lat
     */
    public double getSrc_lat() {
        return src_lat;
    }

    /**
     * @param src_lat the src_lat to set
     */
    public void setSrc_lat(double src_lat) {
        this.src_lat = src_lat;
    }

    /**
     * @return the src_lng
     */
    public double getSrc_lng() {
        return src_lng;
    }

    /**
     * @param src_lng the src_lng to set
     */
    public void setSrc_lng(double src_lng) {
        this.src_lng = src_lng;
    }

    /**
     * @return the dest_lat
     */
    public double getDest_lat() {
        return dest_lat;
    }

    /**
     * @param dest_lat the dest_lat to set
     */
    public void setDest_lat(double dest_lat) {
        this.dest_lat = dest_lat;
    }

    /**
     * @return the dest_lng
     */
    public double getDest_lng() {
        return dest_lng;
    }

    /**
     * @param dest_lng the dest_lng to set
     */
    public void setDest_lng(double dest_lng) {
        this.dest_lng = dest_lng;
    }

    public static SharedData getInstance() {
        if (null == instance) {
            instance = new SharedData();
        }

        return instance;
    }

}
