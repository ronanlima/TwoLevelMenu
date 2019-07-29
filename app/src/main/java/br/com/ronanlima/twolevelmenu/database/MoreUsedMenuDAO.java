package br.com.ronanlima.twolevelmenu.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.ronanlima.twolevelmenu.model.Menu;

@Dao
public interface MoreUsedMenuDAO {
    @Query("SELECT * FROM moreUsedMenu WHERE userId = :userId ORDER BY qtdClick DESC LIMIT :limit")
    List<Menu> getMoreUsed(String userId, Integer limit);

    @Query("SELECT * FROM moreUsedMenu WHERE userId = :userId")
    Menu getById(String userId);

    @Query("SELECT qtdClick FROM moreUsedMenu WHERE userId = :userId AND idMenu = :idMenu")
    int getQtdClickFromMenu(String userId, Integer idMenu);

    @Insert
    void insert(Menu menusMaisUtilizados);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Menu menusMaisUtilizados);

    @Delete
    int delete(Menu menusMaisUtilizados);

    @Query("DELETE FROM moreUsedMenu")
    void clearTable();
}
