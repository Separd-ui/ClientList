package com.example.clientlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientlist.Utills.Constans;
import com.example.clientlist.database.AppDataBase;
import com.example.clientlist.database.AppThread;
import com.example.clientlist.database.Client;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class editAct extends AppCompatActivity {
    private EditText ed_name,ed_sec,ed_tel,ed_notes;
    private CheckBox checkBox_im1,checkBox_im2,checkBox_im3,checkBox_special;
    private AppDataBase myDataBase;
    private int importance=2,special=0;
    private FloatingActionButton fab;
    private CheckBox[] checkBoxes= new  CheckBox[3];
    private boolean isEdit=false;
    private boolean new_user=false;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_client);
        init();
        getMyIntent();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImportance();
                if(!TextUtils.isEmpty(ed_name.getText().toString()) && !TextUtils.isEmpty(ed_sec.getText().toString()) && !TextUtils.isEmpty(ed_notes.getText().toString()) && !TextUtils.isEmpty(ed_tel.getText().toString()))
                {
                AppThread.getInstance().getDiscIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(isEdit)
                        {
                            Client client=new Client(ed_name.getText().toString(),ed_sec.getText().toString(),ed_tel.getText().toString(),importance,ed_notes.getText().toString(),special);
                            client.setId(id);
                            myDataBase.clientDAO().updateClient(client);
                           // Toast.makeText(editAct.this, "Клиент был обновлён", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Client client=new Client(ed_name.getText().toString(),ed_sec.getText().toString(),ed_tel.getText().toString(),importance,ed_notes.getText().toString(),special);
                            myDataBase.clientDAO().insertClient(client);
                           // Toast.makeText(editAct.this, "Клиент был создан", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                }
            }
        });


    }
    private void alertClient()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(R.string.message);
        builder.setTitle(R.string.delete);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppThread.getInstance().getDiscIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Client client=new Client(ed_name.getText().toString(),ed_sec.getText().toString(),ed_tel.getText().toString(),importance,ed_notes.getText().toString(),special);
                        client.setId(id);
                        myDataBase.clientDAO().deleteClient(client);
                        finish();
                    }
                });

            }
        });
        builder.show();

    }
    private void getMyIntent()
    {
        Intent i=getIntent();
        if(i!=null);
        {
            if(i.getStringExtra(Constans.NAME_KEY)!=null)
            {
                setIsClickable(false);
                id=i.getIntExtra(Constans.ID_KEY,0);
                ed_name.setText(i.getStringExtra(Constans.NAME_KEY));
                ed_sec.setText(i.getStringExtra(Constans.SEC_NAME_KEY));
                ed_tel.setText(i.getStringExtra(Constans.TEL_KEY));
                ed_notes.setText(i.getStringExtra(Constans.NOTES_KEY));
                checkBoxes[i.getIntExtra(Constans.IMPORTANCE_KEY,0)].setChecked(true);
                if(i.getIntExtra(Constans.SPECIAL_KEY,0)==1)
                {
                    checkBox_special.setChecked(true);
                }
                new_user=false;
            }
            else
            {
                new_user=true;
            }
        }
    }
    private void setIsClickable(boolean isClickable)
    {
        if(isClickable)
        {
            fab.show();
        }
        else
        {
            fab.hide();
        }
        isEdit=isClickable;
        checkBox_im1.setClickable(isClickable);
        checkBox_im2.setClickable(isClickable);
        checkBox_im3.setClickable(isClickable);
        checkBox_special.setClickable(isClickable);
        ed_name.setClickable(isClickable);
        ed_sec.setClickable(isClickable);
        ed_tel.setClickable(isClickable);
        ed_notes.setClickable(isClickable);
        ed_name.setFocusable(isClickable);
        ed_sec.setFocusable(isClickable);
        ed_tel.setFocusable(isClickable);
        ed_notes.setFocusable(isClickable);
        ed_name.setFocusableInTouchMode(isClickable);
        ed_notes.setFocusableInTouchMode(isClickable);
        ed_tel.setFocusableInTouchMode(isClickable);
        ed_sec.setFocusableInTouchMode(isClickable);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!new_user)
        {
            getMenuInflater().inflate(R.menu.main,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.edit)
        {
            setIsClickable(true);
        }
        else if(id==R.id.delete)
        {
            alertClient();
        }
        return true;
    }

    private void init()
    {
        fab = findViewById(R.id.fb_save);
        myDataBase=AppDataBase.getInstance(getApplicationContext());
        ed_name=findViewById(R.id.ed_name);
        ed_sec=findViewById(R.id.ed_sec);
        ed_notes=findViewById(R.id.ed_notes);
        ed_tel=findViewById(R.id.ed_tel);
        checkBox_im1=findViewById(R.id.check_imp1);
        checkBox_im2=findViewById(R.id.check_imp2);
        checkBox_im3=findViewById(R.id.check_imp3);
        checkBox_special=findViewById(R.id.check_special);
        checkBoxes[0]=checkBox_im1;
        checkBoxes[1]=checkBox_im2;
        checkBoxes[2]=checkBox_im3;
    }


    public void onClickImp(View view) {
        checkBox_im2.setChecked(false);
        checkBox_im3.setChecked(false);
    }

    public void onClickNor(View view) {
        checkBox_im1.setChecked(false);
        checkBox_im3.setChecked(false);
    }

    public void onClickNoImp(View view) {
        checkBox_im1.setChecked(false);
        checkBox_im2.setChecked(false);
    }
    private void getImportance()
    {
        if(checkBox_im1.isChecked())
        {
            importance=0;
        }
        else if(checkBox_im2.isChecked())
        {
            importance=1;
        }
        else if(checkBox_im3.isChecked())
        {
            importance=2;
        }
        if(checkBox_special.isChecked())
        {
            special=1;
        }
    }
}
