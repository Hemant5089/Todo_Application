package com.example.mytodonotes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepo {

    private NoteDao noteDao;
    private LiveData<List<Note>> noteList;
    private ExecutorService executorService; // Executor for background operations

    public NoteRepo(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        noteList = noteDao.getAllData();

        executorService = Executors.newSingleThreadExecutor(); // Initialize executor
    }

    public void insertData(final Note note) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.insert(note);
            }
        });
    }

    public void deleteData(final Note note) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.delete(note);
            }
        });
    }

    public void updateData(final Note note) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);
            }
        });
    }

    public LiveData<List<Note>> getAllData() {
        return noteList;

    }
}