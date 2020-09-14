package com.redbravo.inventorylist;

public class Sales {
    private String itemname;
    private int itemprice;
    private String saledate;
    private int solditems;
    private int totalgained;
    private String mkey;
    public Sales(){

    }

    public Sales(String name, int price, String date, int sold, int total){
        this.itemname = name;
        this.itemprice = price;
        this.saledate = date;
        this.solditems = sold;
        this.totalgained = total;

    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getItemprice() {
        return itemprice;
    }

    public void setItemprice(int itemprice) {
        this.itemprice = itemprice;
    }

    public String getSaledate() {
        return saledate;
    }

    public void setSaledate(String saledate) {
        this.saledate = saledate;
    }

    public int getSolditems() {
        return solditems;
    }

    public void setSolditems(int solditems) {
        this.solditems = solditems;
    }

    public int getTotalgained() {
        return totalgained;
    }

    public void setTotalgained(int totalgained) {
        this.totalgained = totalgained;
    }


    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }
}
