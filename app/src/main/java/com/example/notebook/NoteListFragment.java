package com.example.notebook;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NoteListFragment extends Fragment {

    private static final int MY_DEFAULT_DURATION = 1000;
    private NotesSource data;
    private NotesAdapter adapter;
    private RecyclerView recyclerView;
    private Navigation navigation;
    private Publisher publisher;
    // признак, что при повторном открытии фрагмента
    // (возврате из фрагмента, добавляющего запись)
    // прыгнуть на первую запись
    private boolean moveToFirstPosition;

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        initView(view);
        setHasOptionsMenu(true);
        data = new NotesSourceFirebaseImpl().init(new NotesSourceResponse() {
            @Override
            public void initialized(NotesSource cardsData) {
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setDataSource(data);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity)context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes_menu, menu);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        initRecyclerView();
    }

    private void initRecyclerView(){

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        adapter = new NotesAdapter(this);
        recyclerView.setAdapter(adapter);

        // Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),  LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        // Установим анимацию. А чтобы было хорошо заметно, сделаем анимацию долгой
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        if (moveToFirstPosition && data.size() > 0){
            recyclerView.scrollToPosition(0);
            moveToFirstPosition = false;
        }

        // Установим слушателя
        adapter.SetOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), String.format("Позиция - %d", position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    private boolean onItemSelected(int menuItemId){
        switch (menuItemId){
            case R.id.action_add:
                navigation.addFragment(EditNoteFragment.newInstance(), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateCardData(NoteEntity noteEntity) {
                        data.addNoteData(noteEntity);
                        adapter.notifyItemInserted(data.size() - 1);
                        // это сигнал, чтобы вызванный метод onCreateView
                        // перепрыгнул на начало списка
                        moveToFirstPosition = true;
                    }
                });
                return true;
            case R.id.action_update:
                final int updatePosition = adapter.getMenuPosition();
                navigation.addFragment(EditNoteFragment.newInstance(data.getCardData(updatePosition)), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateCardData(NoteEntity noteEntity) {
                        data.updateNoteData(updatePosition, noteEntity);
                        adapter.notifyItemChanged(updatePosition);
                    }
                });
                return true;
            case R.id.action_delete:
                new AlertDialog.Builder(getContext()).setTitle(R.string.alert_dialog_title)
                        .setMessage(R.string.alert_dialog_message)
                        .setCancelable(true)
                        .setPositiveButton(R.string.yes, (d, i) -> {
                            Toast.makeText( getContext() , "Удалено", Toast.LENGTH_SHORT).show();
                            int deletePosition = adapter.getMenuPosition();
                            data.deleteNoteData(deletePosition);
                            adapter.notifyItemRemoved(deletePosition);
                        })
                        .setNegativeButton(R.string.no, (d, i) -> {
                            Toast.makeText( getContext() , "Не удалено", Toast.LENGTH_SHORT).show();
                        })
                        .show();

                return true;
            case R.id.action_clear:
                data.clearNoteData();
                adapter.notifyDataSetChanged();
                return true;
        }
        return false;
    }
}