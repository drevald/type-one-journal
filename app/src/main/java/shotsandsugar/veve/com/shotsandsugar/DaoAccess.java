package shotsandsugar.veve.com.shotsandsugar;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import shotsandsugar.veve.com.shotsandsugar.model.SugarLevel;

@Dao
public interface DaoAccess {

    @Insert
    void insertSugarLevel (SugarLevel record);

    @Insert
    void insertSugarLevels (List<SugarLevel> records);

    @Query("SELECT * FROM SugarLevel WHERE Id = :id")
    SugarLevel fetchSugarLevel (int id);

    @Update
    void updateMovie (SugarLevel record);

    @Delete
    void deleteSugarLevel(SugarLevel record);

}