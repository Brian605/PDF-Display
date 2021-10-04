package com.return0.filesviewer;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.return0.filesviewer.databinding.DocsItemBinding;

import java.io.File;
import java.util.List;

public class DocsAdapter extends RecyclerView.Adapter<DocsAdapter.Holder> {
private List<Document> documentList;
private ClickListener listener;

    public DocsAdapter(List<Document> documentList, ClickListener listener) {
        this.documentList = documentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DocsItemBinding binding=DocsItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Document document=documentList.get(position);
holder.binding.fileName.setText(document.getDocumentName());

       new  Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               holder.binding.progressBar.hide();
           }
       },3000);
holder.binding.getRoot().setOnClickListener((view -> {
    listener.onClick(document);
}));
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        DocsItemBinding binding;
        public Holder(DocsItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;

        }
    }
}
