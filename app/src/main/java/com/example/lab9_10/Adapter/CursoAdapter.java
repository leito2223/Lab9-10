package com.example.lab9_10.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lab9_10.Logica.Curso;
import com.example.lab9_10.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.MyViewHolder> implements Filterable {
    private List<Curso> CursoList;
    private List<Curso> CursoListFiltered;
    private CursoAdapterListener listener;
    private Curso deletedItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo1, titulo2, description;
        //two layers
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;


        public MyViewHolder(View view) {
            super(view);
            titulo1 = view.findViewById(R.id.titleFirstLbl);
            titulo2 = view.findViewById(R.id.titleSecLbl);
            description = view.findViewById(R.id.descriptionLbl);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(CursoListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public CursoAdapter(List<Curso> CursoList, CursoAdapterListener listener) {
        this.CursoList = CursoList;
        this.listener = listener;
        //init filter
        this.CursoListFiltered = CursoList;
    }

    public void updateCursos(List<Curso> Cursos) {
        this.CursoList = Cursos;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // rendering view
        final Curso Curso = CursoListFiltered.get(position);
        holder.titulo1.setText(Curso.getId());
        holder.titulo2.setText(Curso.getDescripcion());
        holder.description.setText(String.valueOf(Curso.getCreditos()));
    }


    @Override
    public int getItemCount() {
        return CursoListFiltered.size();
    }

    public void removeItem(int position) {
        deletedItem = CursoListFiltered.remove(position);
        Iterator<Curso> iter = CursoList.iterator();
        while (iter.hasNext()) {
            Curso aux = iter.next();
            if (deletedItem.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (CursoListFiltered.size() == CursoList.size()) {
            CursoListFiltered.add(position, deletedItem);
        } else {
            CursoListFiltered.add(position, deletedItem);
            CursoList.add(deletedItem);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Curso getSwipedItem(int index) {
        if (this.CursoList.size() == this.CursoListFiltered.size()) { //not filtered yet
            return CursoList.get(index);
        } else {
            return CursoListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (CursoList.size() == CursoListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(CursoList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(CursoList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(CursoListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(CursoListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    CursoListFiltered = CursoList;
                } else {
                    List<Curso> filteredList = new ArrayList<>();
                    for (Curso row : CursoList) {
                        // filter use two parameters
                        if (row.getId().toLowerCase().contains(charString.toLowerCase()) || row.getDescripcion().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    CursoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = CursoListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                CursoListFiltered = (ArrayList<Curso>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CursoAdapterListener {
        void onContactSelected(Curso Curso);
    }
}
