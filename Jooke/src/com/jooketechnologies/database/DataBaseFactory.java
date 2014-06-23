package com.jooketechnologies.database;

import android.content.Context;

public class DataBaseFactory
{
  private static DataBase db = null;
  
  public static DataBase get(Context paramContext)
  {
    if (db == null) {
      db = new DataBase(paramContext);
    }
    return db;
  }
}

