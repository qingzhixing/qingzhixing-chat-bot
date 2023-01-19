package org.qingzhixing;

public class Account {
    public long ID = 0;
    public String password = "";

    public Account(long ID) {
        this.ID = ID;
    }

    public Account(long ID, String password) {
        this.ID = ID;
        this.password = password;
    }

    public Account() {
    }
}
