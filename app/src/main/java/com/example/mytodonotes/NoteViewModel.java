package com.example.mytodonotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

// ViewModel class
public class NoteViewModel extends ViewModel {

    private NoteRepo noteRepo;
    private LiveData<List<Note>> noteList;

//     Constructor receives the NoteRepo
    public NoteViewModel(NoteRepo noteRepo) {
        this.noteRepo = noteRepo;
        this.noteList = noteRepo.getAllData();
    }
    // Methods to manipulate data
    public void insert(Note note){
        noteRepo.insertData(note);
    }

    public void delete(Note note){
        noteRepo.deleteData(note);
    }

    public void update(Note note){
        noteRepo.updateData(note);
    }

    public LiveData<List<Note>> getAllNotes(){
        return noteList;
    }
    public static class Factory implements ViewModelProvider.Factory{


        @NonNull

        private final Application application;

        public Factory(@NonNull Application application) {
            this.application = application;
        }
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(NoteViewModel.class)){
                NoteRepo noteRepo = new NoteRepo(application);
                return (T) new NoteViewModel(noteRepo);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
