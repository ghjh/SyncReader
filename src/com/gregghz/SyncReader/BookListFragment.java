package com.gregghz.SyncReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gregghz.SyncReader.data.SRBook;
import com.gregghz.SyncReader.dummy.DummyContent;

public class BookListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;

    public interface Callbacks {

        public void onItemSelected(String id);
    }
    
    private static Callbacks sDummyCallbacks = new Callbacks() {
		public void onItemSelected(String id) {
		}
	};

    public BookListFragment() {
    }

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                DummyContent.ITEMS));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState
                .containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }
    
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.list_fragment, menu);
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.scan:
    		new ScanTask().execute(new String[]{});
    		break;
    	}
    	
		return super.onOptionsItemSelected(item);
	}

	@Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onItemSelected(((SRBook)getListAdapter().getItem(position)).path());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
    
    private class ScanTask extends AsyncTask<String, Integer, List<SRBook>> {

    	ProgressDialog progress = new ProgressDialog(BookListFragment.this.getActivity());
    	
		@Override
		protected void onPostExecute(List<SRBook> result) {
			setListAdapter(new ArrayAdapter<SRBook>(getActivity(),
	                android.R.layout.simple_list_item_activated_1,
	                android.R.id.text1,
	                result));
			progress.hide();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			progress.setMessage("Scanning...");
			progress.setCancelable(false);
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		private void getEpubs(File root, List<SRBook> epubs) {
			File[] files = root.listFiles();
			String abs_path = root.getAbsolutePath();
			for (int i = 0; i < files.length; i++) {
				File temp_file = new File(abs_path, files[i].getName());
				
				if (files[i].getName().endsWith(".epub")) {
					epubs.add(new SRBook(abs_path + "/" + files[i].getName()));
				} else if (temp_file.isDirectory()) {
					getEpubs(temp_file, epubs);
				}
			}
		}

		@Override
		protected List<SRBook> doInBackground(String... paths) {
			// @TODO: current ignoring paths ... make this an option
			
			File root = Environment.getExternalStorageDirectory();
			List<SRBook> epubs = new ArrayList<SRBook>();
			getEpubs(root, epubs);
			
			return epubs;
		}
    	
    }
}
