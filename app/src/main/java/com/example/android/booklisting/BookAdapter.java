package com.example.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by baba on 9/8/2016.
 */
public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item
                    , parent, false);
        }
        // Find the Book at the given position in the list of books
        Book currentBook = getItem(position);
        // Find the text view with the id title
        TextView mTitle = (TextView) listItemView.findViewById(R.id.title);
        mTitle.setText(currentBook.getmTitle());
        TextView mAuthor = (TextView) listItemView.findViewById(R.id.author);
        mAuthor.setText(currentBook.getmAuthor());
        return listItemView;
    }
}
