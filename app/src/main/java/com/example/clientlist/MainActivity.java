package com.example.clientlist;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.clientlist.Settings.SettingsActivity;
import com.example.clientlist.Utills.Constans;
import com.example.clientlist.database.AppDataBase;
import com.example.clientlist.database.AppThread;
import com.example.clientlist.database.Client;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppDataBase myDataBase;
    private DataAdapter adapter;
    private List<Client> clientList;
    private RecyclerView recyclerView;
    private DrawerLayout drawer;
    private DataAdapter.AdapterOnItem adapterOnItem;
    private  Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i=new Intent(MainActivity.this,editAct.class);
                    startActivity(i);
            }
        });

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        adapterOnItem= new DataAdapter.AdapterOnItem() {
            @Override
            public void onAdapterClick(int position) {
                Intent i =new Intent(MainActivity.this,editAct.class);
                i.putExtra(Constans.NAME_KEY,clientList.get(position).getName());
                i.putExtra(Constans.SEC_NAME_KEY,clientList.get(position).getSec_name());
                i.putExtra(Constans.NOTES_KEY,clientList.get(position).getNotes());
                i.putExtra(Constans.TEL_KEY,clientList.get(position).getNumber());
                i.putExtra(Constans.IMPORTANCE_KEY,clientList.get(position).getImportance());
                i.putExtra(Constans.SPECIAL_KEY,clientList.get(position).getSpecial());
                i.putExtra(Constans.ID_KEY,clientList.get(position).getId());
                startActivity(i);
            }
        };

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager= (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView= (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AppThread.getInstance().getDiscIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        clientList=myDataBase.clientDAO().get_client_name_search(newText);
                        AppThread.getInstance().getMainIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                if(adapter!=null)
                                {
                                    adapter.updateAdapter(clientList);
                                }
                            }
                        });
                    }
                });
                return true;
            }
        });
        toolbar.setTitle(R.string.client_base);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.help)
        {
            callHelp();
        }
        return super.onOptionsItemSelected(item);
    }
    private void callHelp()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.message_help);
        builder.setNeutralButton("Понятно", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        AppThread.getInstance().getDiscIO().execute(new Runnable() {
            @Override
            public void run() {
                clientList=myDataBase.clientDAO().get_client_list();
                AppThread.getInstance().getMainIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(adapter!=null)
                        {
                            adapter.updateAdapter(clientList);
                        }
                    }
                });
            }
        });
    }

    private void init()
    {
        recyclerView=findViewById(R.id.rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myDataBase=AppDataBase.getInstance(getApplicationContext());
        clientList=new ArrayList<>();
        adapter=new DataAdapter(clientList,adapterOnItem,this);
        recyclerView.setAdapter(adapter);

    }


    private void web_go(String url)
    {
        Intent brIntent,chooser;
        brIntent=new Intent(Intent.ACTION_VIEW);
        brIntent.setData(Uri.parse(url));
        chooser=Intent.createChooser(brIntent,"Открыть с помощью:");
        if(brIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivity(chooser);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.client)
        {
            toolbar.setTitle(R.string.client_base);
            AppThread.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    clientList=myDataBase.clientDAO().get_client_list();
                    AppThread.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(adapter!=null)
                            {
                                adapter.updateAdapter(clientList);
                            }
                        }
                    });
                }
            });
        }
        else if(id==R.id.web)
        {
            web_go("https://else.fcim.utm.md/");
        }
        else if(id==R.id.gen_set)
        {
            Intent i=new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        }
        else if(id==R.id.special)
        {
            toolbar.setTitle(R.string.special_v);
            AppThread.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    clientList=myDataBase.clientDAO().get_client_special();
                    AppThread.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(adapter!=null)
                            {
                                adapter.updateAdapter(clientList);
                            }
                        }
                    });
                }
            });
        }
        else if(id==R.id.important)
        {
            get_important(0,R.string.important_v0);
        }
        else if(id==R.id.normal)
        {
            get_important(1,R.string.important_v1);
        }
        else if(id==R.id.no_important)
        {
            get_important(2,R.string.important_v2);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void get_important(int number,int title)
    {
        toolbar.setTitle(title);
        AppThread.getInstance().getDiscIO().execute(new Runnable() {
            @Override
            public void run() {
                clientList=myDataBase.clientDAO().get_client_important(number);
                AppThread.getInstance().getMainIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(adapter!=null)
                        {
                            adapter.updateAdapter(clientList);
                        }
                    }
                });
            }
        });
    }
}