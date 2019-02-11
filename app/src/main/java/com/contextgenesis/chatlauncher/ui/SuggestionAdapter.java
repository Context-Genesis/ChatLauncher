package com.contextgenesis.chatlauncher.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.contextgenesis.chatlauncher.R;
import com.contextgenesis.chatlauncher.command.Command;
import com.contextgenesis.chatlauncher.database.entity.SuggestEntity;
import com.contextgenesis.chatlauncher.events.InputMessageEvent;
import com.contextgenesis.chatlauncher.rx.RxBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

@Singleton
public class SuggestionAdapter extends
        RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    @Inject
    Context context;
    @Inject
    RxBus rxBus;

    public List<SuggestEntity> getSuggestions() {
        return suggestions;
    }

    private final List<SuggestEntity> suggestions;

    @Inject
    public SuggestionAdapter() {
        this.suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View suggestionView = inflater.inflate(R.layout.item_suggestion, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(suggestionView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SuggestEntity suggestion = suggestions.get(position);
        String suggestionText = suggestion.getCommandName();

        holder.suggestionTextBtn.setText(suggestionText);

        if (suggestion.getArgType() == Command.ArgInfo.Type
                .ALIAS.getId()) {
            holder.suggestionTextBtn.setTextColor(Color.RED);
        } else if (suggestion.getArgType() == Command.ArgInfo.Type
                .COMMAND.getId()) {
            holder.suggestionTextBtn.setTextColor(Color.BLUE);
        } else {
            holder.suggestionTextBtn.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Button suggestionTextBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            suggestionTextBtn = (Button) itemView.findViewById(R.id.suggestion_item);

            suggestionTextBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                rxBus.post(new InputMessageEvent(suggestionTextBtn.getText().toString(), true, true));
            }
        }
    }


}