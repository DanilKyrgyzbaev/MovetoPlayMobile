package com.movetoplay.db;

import androidx.room.Insert;
import androidx.room.Query;

public abstract class UserDao {

    //TODO написать методы для взаимодействия с БД

    // Добавление time в бд
    @Insert
    abstract void insertAll(UserEntity... userEntities);
}
