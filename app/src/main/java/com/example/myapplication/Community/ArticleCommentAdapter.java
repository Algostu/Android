package com.example.myapplication.Community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Community.dataframe.ArticleCommentFrame;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ArticleCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ArticleCommentFrame> list = new ArrayList<ArticleCommentFrame>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public ArticleCommentAdapter(Context context, ArrayList<ArticleCommentFrame> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_comment_card, parent, false);
                return new CommentViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_recomment_card, parent, false);
                return new RecommentViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_comment_card, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (list.get(position).parentReplyID == 0) {
            CommentViewHolder holder1 = (CommentViewHolder) holder;
            holder1.nickName.setText(list.get(position).nickName);
            holder1.writtenTime.setText(list.get(position).writtenTime);
            holder1.content.setText(list.get(position).content);
            holder1.itemView.setTag(position);
        } else {
            RecommentViewHolder holder2 = (RecommentViewHolder) holder;
            holder2.nickName.setText(list.get(position).nickName);
            holder2.writtenTime.setText(list.get(position).writtenTime);
            holder2.content.setText(list.get(position).content);
            holder2.itemView.setTag(position);
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView nickName;
        protected TextView writtenTime;
        protected TextView content;
        protected ImageView im_comment;
        protected boolean recomment = false;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.nickName);
            writtenTime = itemView.findViewById(R.id.writtenTime);
            content = itemView.findViewById(R.id.content);
            im_comment = itemView.findViewById(R.id.im_comment);

            im_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recomment = true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });
        }
    }

    public class RecommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView nickName;
        protected TextView writtenTime;
        protected TextView content;

        public RecommentViewHolder(@NonNull View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.nickName);
            writtenTime = itemView.findViewById(R.id.writtenTime);
            content = itemView.findViewById(R.id.content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });
        }
    }

    public int getItemViewType(int position) {
        if (list.get(position).parentReplyID == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }
}