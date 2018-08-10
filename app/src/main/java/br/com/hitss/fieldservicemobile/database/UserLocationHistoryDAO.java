package br.com.hitss.fieldservicemobile.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.hitss.fieldservicemobile.model.UserLocationHistory;

@Dao
public interface UserLocationHistoryDAO {

    @Insert
    void insert(UserLocationHistory userLocationHistory);

    @Delete
    void delete(List<UserLocationHistory> userLocationHistories);

    @Query("Select * from UserLocationHistory where idUserLocationHistory = :idUserLocationHistory")
    UserLocationHistory getById(Long idUserLocationHistory);

    @Query("Select * from UserLocationHistory where idUserLocationHistory = (Select max(idUserLocationHistory) from UserLocationHistory)")
    UserLocationHistory getLastUserLocationHistory();

    @Query("Select * from UserLocationHistory")
    List<UserLocationHistory> getAll();
}
