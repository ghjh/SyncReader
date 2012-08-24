package com.gregghz.SyncReader.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

public class SRBook {
	private String mPath;
	private Book mBook;
	
	public SRBook(String path) {
		mPath = path;
		SRBookStore.addItem(this);
		
		try {
			File epub = new File(mPath);
			InputStream in = new BufferedInputStream(new FileInputStream(epub));
			mBook = (new EpubReader()).readEpub(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String path() {
		return mPath;
	}
	
	public String description() {
		Metadata md = mBook.getMetadata();
		if (md != null) {
			List<String> descs = md.getDescriptions();
			if (descs.size() > 0)
				return descs.get(0);
		}
		return "";
	}
	
	public String getTitle() {
		return mBook.getTitle();
	}
	
	public Resource getCoverImage() {
		return mBook.getCoverImage();
	}
	
	public String toString() {
		return getTitle();
	}
}
