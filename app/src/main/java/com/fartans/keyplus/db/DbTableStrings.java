package com.fartans.keyplus.db;

public class DbTableStrings {
    public static final String DATABASE_NAME="secure_provider";
 public static final int DATABASE_VERSION=1;

 public  static final String TABLE_NAME_AUTHENTICATION="authentication";

    public static final String TABLE_NAME_VAULT = "vault";
    public static final String TABLE_NAME_DATA = "data";
    public static final String ID = "id";
    public static final String DATA_URI="content://com.phoenix.securekey.db.SecureProvider/data";
    public static final String VAULT_URI = "content://com.phoenix.securekey.db.SecureProvider/vault";
    public static final String AUTH_URI="content://com.phoenix.securekey.db.SecureProvider/authentication";



    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String VAULT_ID = "vault_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String VAULT_NAME = "vault_name";
    public static final String VAULT_PASSWORD = "vault_password";
    public static final String IS_SECURE = "isSecure";
}
