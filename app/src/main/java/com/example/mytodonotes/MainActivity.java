package com.example.mytodonotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mytodonotes.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Use the custom Factory to create NoteViewModel
        NoteViewModel.Factory factory = new NoteViewModel.Factory(getApplication());
        noteViewModel = new ViewModelProvider(this, factory).get(NoteViewModel.class);

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DataInsertActivity.class);
                intent.putExtra("type" , "addMode");
                startActivityForResult(intent, 1);
            }
        });

        binding.Rv.setLayoutManager(new LinearLayoutManager(this));
        binding.Rv.setHasFixedSize(true);
        RVAdapter adapter = new RVAdapter();
        binding.Rv.setAdapter(adapter);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.RIGHT){
//                    Toast.makeText(MainActivity.this, "Updating", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this,DataInsertActivity.class);
                    intent.putExtra("type","update");
                    intent.putExtra("title" , adapter.getNote(viewHolder.getAdapterPosition()).getTitle());
                    intent.putExtra("disp" , adapter.getNote(viewHolder.getAdapterPosition()).getDisp());
                    intent.putExtra("id" , adapter.getNote(viewHolder.getAdapterPosition()).getId());
                    startActivityForResult(intent,2);
                    Toast.makeText(MainActivity.this, "Updating Note", Toast.LENGTH_SHORT).show();


                }
                else {
                    noteViewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(binding.Rv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String disp = data.getStringExtra("disp");
            Note note = new Note(title, disp);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == 2){
            String title = data.getStringExtra("title");
            String disp = data.getStringExtra("disp");
            Note note = new Note(title, disp);
            note.setId(data.getIntExtra("id",0));
            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
