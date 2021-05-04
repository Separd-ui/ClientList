package com.example.clientlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientlist.database.Client;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolderData> {
    private List<Client> clientList;
    private int[] color_imp={R.drawable.ic_happy,R.drawable.ic_med,R.drawable.ic_sad};
    private AdapterOnItem adapterOnItem;
    private Context context;
    private SharedPreferences def_s;

    public DataAdapter(List<Client> clientList, AdapterOnItem adapterOnItem, Context context) {
        this.clientList = clientList;
        this.adapterOnItem=adapterOnItem;
        this.context=context;
        def_s= PreferenceManager.getDefaultSharedPreferences(context);

    }

    @NonNull
    @Override

    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter,parent,false);
        return new ViewHolderData(view,adapterOnItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.setData(clientList.get(position));
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_name,text_sec_name;
        TextView text_tel;
        ImageView image_imp,image_special;
        private AdapterOnItem adapterOnItem;

        public ViewHolderData(@NonNull View itemView,AdapterOnItem adapterOnItem) {
            super(itemView);
            text_name=itemView.findViewById(R.id.text_name);
            text_sec_name=itemView.findViewById(R.id.text_sec_name);
            text_tel=itemView.findViewById(R.id.text_tel);
            image_imp=itemView.findViewById(R.id.image_imp);
            image_special=itemView.findViewById(R.id.img_special);
            this.adapterOnItem=adapterOnItem;
            itemView.setOnClickListener(this);

        }

        public void setData(Client client) {
            text_name.setTextColor(Color.parseColor(def_s.getString(context.getResources().getString(R.string.list_key_text_color_name),"#FF000000")));
            text_tel.setTextColor(Color.parseColor(def_s.getString(context.getResources().getString(R.string.list_key_text_color_tel),"#FF000000")));
            text_sec_name.setTextColor(Color.parseColor(def_s.getString(context.getResources().getString(R.string.list_key_text_color_sec_name),"#FF000000")));
            text_name.setText(client.getName());
            text_sec_name.setText(client.getSec_name());
            text_tel.setText(client.getNumber());
            image_imp.setImageResource(color_imp[client.getImportance()]);
            if(client.getSpecial()==1)
            {
                image_special.setVisibility(View.VISIBLE);
            }
            else
            {
                image_special.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            adapterOnItem.onAdapterClick(getAdapterPosition());

        }


    }
    public interface AdapterOnItem
    {
        void onAdapterClick(int position);

    }
    public void updateAdapter(List<Client> client)
    {
            clientList.clear();
            clientList.addAll(client);
            notifyDataSetChanged();

    }

}
