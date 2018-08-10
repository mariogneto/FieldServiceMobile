package br.com.hitss.fieldservicemobile.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import br.com.hitss.fieldservicemobile.converters.DateConverter;
import br.com.hitss.fieldservicemobile.model.UserLocationHistory;

@Database(entities = {UserLocationHistory.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class FieldserviceDataBase extends RoomDatabase {

    public abstract UserLocationHistoryDAO userLocationHistoryDAO();
}
