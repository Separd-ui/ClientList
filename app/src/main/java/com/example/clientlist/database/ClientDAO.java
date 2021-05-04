package com.example.clientlist.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ClientDAO {
    @Query("Select * from client_list")
    List<Client> get_client_list();
    @Query("Select * from client_list where special is 1")
    List<Client> get_client_special();
    @Query("Select * from client_list where importance  is:importance")
    List<Client> get_client_important(int importance);
    @Query("Select * from client_list where name Like '%' ||:name || '%'")
    List<Client> get_client_name_search(String name);
    @Insert
    void insertClient(Client client);
    @Update
    void updateClient(Client client);
    @Delete
    void deleteClient(Client client);
}
