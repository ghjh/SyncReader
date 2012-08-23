package com.gregghz.SyncReader;

import java.io.IOException;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gregghz.SyncReader.data.SRBook;
import com.gregghz.SyncReader.data.SRBookStore;

public class BookDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    SRBook mItem;
    View mRootView;

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
        mRootView = inflater.inflate(R.layout.fragment_book_detail, container, false);
        if (mItem != null) {
            ((TextView) mRootView.findViewById(R.id.book_detail)).setText(mItem.getTitle());
            new LoadImageTask().execute();
        }
        return mRootView;
    }
    
    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

    	private ProgressDialog mProgress = new ProgressDialog(getActivity());
    	
		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap image = null;
			try {
				image = BitmapFactory.decodeStream(mItem.getCoverImage().getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return image;
		}

		@Override
		protected void onPreExecute() {
			mProgress.setMessage("Loading image...");
			mProgress.show();
			Log.d("SyncReader", "here we go ...");
			super.onPreExecute();
		}



		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				Log.d("SyncReader", "we made it ...");
				TextView tv = (TextView) mRootView.findViewById(R.id.book_detail);
				tv.setCompoundDrawables(null, null, null, new BitmapDrawable(getResources(), result));
				ImageView image = (ImageView)mRootView.findViewById(R.id.book_image);
				image.setImageBitmap(result);
				
				Log.d("SyncReader", Integer.toString(result.getHeight()));
				Log.d("SyncReader", Integer.toString(result.getWidth()));
			}
			
			mProgress.hide();
			Log.d("SyncReader", "annnnnd it's gone.");
			super.onPostExecute(result);
		}
    	
    }
}
