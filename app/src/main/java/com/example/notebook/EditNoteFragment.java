package com.example.notebook;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

public class EditNoteFragment extends Fragment {

    private static final String ARG_CARD_DATA = "Param_CardData";

    private NoteEntity noteEntity;      // Данные по карточке
    private Publisher publisher;    // Паблишер, с его помощью обмениваемся данными

    private TextInputEditText title;
    private TextInputEditText description;
    private DatePicker datePicker;

    // Для редактирования данных
    public static EditNoteFragment newInstance(NoteEntity noteEntity) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD_DATA, noteEntity);
        fragment.setArguments(args);
        return fragment;
    }

    // Для добавления новых данных
    public static EditNoteFragment newInstance() {
        EditNoteFragment fragment = new EditNoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteEntity = getArguments().getParcelable(ARG_CARD_DATA);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        initView(view);
        // если cardData пустая, то это добавление
        if (noteEntity != null) {
            populateView();
        }
        return view;
    }

    // Здесь соберем данные из views
    @Override
    public void onStop() {
        super.onStop();
        noteEntity = collectCardData();
    }

    // Здесь передадим данные в паблишер
    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(noteEntity);
    }

    private NoteEntity collectCardData() {
        String title = this.title.getText().toString();
        String description = this.description.getText().toString();
        Date date = getDateFromDatePicker();
        if (noteEntity != null) {
            NoteEntity answer;
            answer = new NoteEntity(title, description, noteEntity.isLike(), date);
            answer.setId(noteEntity.getId());
            return answer;
        } else {
            return new NoteEntity(title, description, false, date);
        }
    }

    // Получение из DatePicker
    private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime();
    }

    private void initView(View view) {
        title = view.findViewById(R.id.inputTitle);
        description = view.findViewById(R.id.inputDescription);
        datePicker = view.findViewById(R.id.inputDate);
    }

    private void populateView() {
        title.setText(noteEntity.getTitle());
        description.setText(noteEntity.getDescription());
        initDatePicker(noteEntity.getDate());
    }

    // Установка даты в DatePicker
    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }
}