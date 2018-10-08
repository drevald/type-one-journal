package shotsandsugar.veve.com.shotsandsugar.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class SugarLevel {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private float value;

    public SugarLevel(float value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

}