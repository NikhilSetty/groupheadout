package com.fartans.keyplus.db;

public class Schema {
    public static final String CREATE_AUTH_TABLE = "create table if not exists " + DbTableStrings.TABLE_NAME_AUTHENTICATION +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.USERNAME + " string, "
            + DbTableStrings.PASSWORD + " string) ";

    public static final String CREATE_VAULT = "create table if not exists " + DbTableStrings.TABLE_NAME_VAULT +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.VAULT_NAME + " string, "
            + DbTableStrings.VAULT_PASSWORD + " string, "
            + DbTableStrings.IS_SECURE + " int) ";

    public static final String CREATE_DATA_TABLE = "create table if not exists " + DbTableStrings.TABLE_NAME_DATA +
            "( _id integer primary key autoincrement, "
            + DbTableStrings.KEY + " string, "
            + DbTableStrings.VALUE + " string, "
            + DbTableStrings.VAULT_ID + " string) ";
            //+ "FOREIGN KEY ("+DbTableStrings.VAULT_ID+") REFERENCES "+DbTableStrings.TABLE_NAME_VAULT+" ("+DbTableStrings.VAULT_ID+"));";
}
