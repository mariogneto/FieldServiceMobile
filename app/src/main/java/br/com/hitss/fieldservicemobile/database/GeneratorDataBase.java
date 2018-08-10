package br.com.hitss.fieldservicemobile.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class GeneratorDataBase {

    public FieldserviceDataBase generate(Context context){
        return Room.databaseBuilder(context, FieldserviceDataBase.class, "FieldserviceDB")
                .allowMainThreadQueries()
                .build();
    }
}
