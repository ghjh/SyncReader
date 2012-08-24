package com.gregghz.SyncReader;

import java.io.IOException;

import nl.siegmann.epublib.domain.Resource;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gregghz.SyncReader.data.SRBook;
import com.gregghz.SyncReader.data.SRBookStore;

public class BookDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    SRBook mItem;

    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = SRBookStore.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_book_detail, container, false);
        if (mItem != null) {
        	// start up the image loading task
            new LoadImageTask().execute();

            // load the title
        	TextView title = (TextView)root.findViewById(R.id.book_detail);
            title.setText(mItem.getTitle());
            
            // load the description
            TextView desc = (TextView)root.findViewById(R.id.book_description);
            desc.setText(mItem.description());
            
            // set up the event listener for the read button
            Button read_button = (Button)root.findViewById(R.id.read_button);
            read_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
				}
            	
            });
        }
        return root;
    }
    
    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap image = null;
			try {
				Resource coverImage = mItem.getCoverImage();
				if (coverImage != null) {
					image = BitmapFactory.decodeStream(coverImage.getInputStream());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return image;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}



		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				//TextView tv = (TextView)getView().findViewById(R.id.book_detail);
				//tv.setCompoundDrawables(null, null, null, new BitmapDrawable(getResources(), result));
				ImageView image = (ImageView)(getView().findViewById(R.id.book_image));
				image.setImageBitmap(result);
			}
			
			super.onPostExecute(result);
		}
    	
    }
}
