package com.example.notebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NoteListFragment.Contract, EditNoteFragment.Contract {
    private static final String NOTES_LIST_FRAGMENT_TAG = "NOTES_LIST_FRAGMENT_TAG";
    private boolean isTwoPanelMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isTwoPanelMode = findViewById(R.id.option_fragment_container) != null;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showNoteList();

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                return false;
            }
        });

        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Toast.makeText(this, "настройки", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }

    private void showNoteList() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new NoteListFragment(), NOTES_LIST_FRAGMENT_TAG)
                .commit();
    }

    private void showEditNote() {
        showEditNote(null);
    }

    private void showEditNote(@Nullable NoteEntity note) {
        if (!isTwoPanelMode) {
            setTitle(note == null ? R.string.create_note_title : R.string.edit_note_title);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!isTwoPanelMode) {
            transaction.addToBackStack(null);
        }
        transaction
                .add(isTwoPanelMode ? R.id.option_fragment_container : R.id.fragment_container, EditNoteFragment.newInstance(note))
                .commit();
    }


    @Override
    public void createNewNote() {
        showEditNote();
    }

    @Override
    public void updateNote(NoteEntity note) {
        showEditNote(note);
    }

    @Override
    public void saveNote(NoteEntity note) {
        setTitle(R.string.app_name);
        getSupportFragmentManager().popBackStack();
        NoteListFragment noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentByTag(NOTES_LIST_FRAGMENT_TAG);
        noteListFragment.addNote(note);
    }
}
