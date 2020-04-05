package com.example.vault.objects;

public class Categories {
    private String Name;
    private long rowid;
    private int Colour;

    public Categories()
    {
        this.Name="";
        this.Colour=0;
    }

    public Categories(String Name, int Colour, long rowid)
    {
        this.Name=Name;
        this.Colour=Colour;
        this.rowid=rowid;
    }

    public int getColour() {
        return Colour;
    }

    public void setColour(int colour) {
        Colour = colour;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setRowid(long rowid) {
        this.rowid = rowid;
    }

    public long getRowid() {
        return rowid;
    }
}
