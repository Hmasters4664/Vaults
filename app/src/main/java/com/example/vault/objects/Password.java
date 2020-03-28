package com.example.vault.objects;

public class Password {
    private String title;
    private String username;
    private String website ;
    private String password;
    private String notes;
    private String group;
    private long rowid;

    public Password() {
        this.group="";
        this.notes="";
        this.password="";
        this.username="";
        this.website="";
        this.title="";
    }

    public Password(String title,String group, String notes, String password, String username, String website ) {
        this.group=group;
        this.notes=notes;
        this.password=password;
        this.username=username;
        this.website=website;
        this.title=title;
    }

    public String getGroup() {
        return group;
    }

    public String getUsername() {
        return username;
    }

    public String getWebsite() {
        return website;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNotes() {
        return notes;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public long getRowid() {
        return rowid;
    }

    public void setRowid(long rowid) {
        this.rowid = rowid;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
