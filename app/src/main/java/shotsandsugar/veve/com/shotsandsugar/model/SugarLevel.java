package shotsandsugar.veve.com.shotsandsugar.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class SugarLevel {

    @NonNull
    @PrimaryKey
    private long id;
    private float value;

    public SugarLevel(@NonNull long id, float value) {
        this.id = id;
        this.value = value;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}