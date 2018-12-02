/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.greenrobot.greendao.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import org.greenrobot.greendao.example.model.db.DBManager;
import org.greenrobot.greendao.example.model.db.dao.NoteDao;
import org.greenrobot.greendao.example.model.db.entity.Note;
import org.greenrobot.greendao.query.Query;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "yinxm";

    private TextView tv_debug;
    private EditText editText;
    private View addNoteButton, btn_add_batch, btn_del_all, btn_query,btn_update, btn_replace;

    private NoteDao noteDao;
    private Query<Note> notesQuery;
    private NotesAdapter notesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setUpViews();

//        // get the note DAO
//        DaoSession daoSession = ((App) getApplication()).getDaoSession();
//        noteDao = daoSession.getNoteDao();
        noteDao = DBManager.getInstance().getDaoSession().getNoteDao();

        // query all notes, sorted a-z by their text
        notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Text).build();

        //封装Dao
        updateNotes();
    }

    private void updateNotes() {
        List<Note> notes = notesQuery.list();
        notesAdapter.setNotes(notes);
    }

    protected void setUpViews() {
        tv_debug = (TextView) findViewById(R.id.tv_debug);
        tv_debug.setText(DbDebugUtil.getDebugInfo());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotes);
        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesAdapter = new NotesAdapter(noteClickListener);
        recyclerView.setAdapter(notesAdapter);

        addNoteButton = findViewById(R.id.btn_add);
        btn_add_batch = findViewById(R.id.btn_add_batch);
        btn_del_all = findViewById(R.id.btn_del_all);
        btn_query = findViewById(R.id.btn_query);
        btn_update = findViewById(R.id.btn_update);
        btn_replace = findViewById(R.id.btn_replace);

        addNoteButton.setOnClickListener(this);
        btn_add_batch.setOnClickListener(this);
        btn_del_all.setOnClickListener(this);
        btn_query.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_replace.setOnClickListener(this);



        //noinspection ConstantConditions
        addNoteButton.setEnabled(false);

        editText = (EditText) findViewById(R.id.editTextNote);
        editText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addNote();
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addNoteButton.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        Note note = new Note();
        note.setText(noteText);
        note.setComment(comment);
        note.setDate(new Date());
        note.setType(NoteType.TEXT);
        noteDao.insert(note);
        Log.d(TAG, "Inserted new note, ID: " + note.getId());

        updateNotes();
    }

    NotesAdapter.NoteClickListener noteClickListener = new NotesAdapter.NoteClickListener() {
        @Override
        public void onNoteClick(int position) {
            Note note = notesAdapter.getNote(position);
            Long noteId = note.getId();

            noteDao.deleteByKey(noteId);
            Log.d(TAG, "Deleted note, ID: " + noteId);

            updateNotes();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                addNote();
                break;
            case R.id.btn_add_batch:
                addBatch();
                break;
            case R.id.btn_del_all:
                delAll();
                break;
            case R.id.btn_update:
                update();
                break;
            case R.id.btn_query:
                query();
                break;
            case R.id.btn_replace:
                replace();
                break;
            
        }
    }

    /**
     * 获取用户输入
     * @return
     */
    private String getInput() {
        if (editText != null) {
            return editText.getText().toString();
        }
        return null;
    }

    /**
     * 清空用户输入
     */
    private void clearInput() {
        if (editText != null) {
            editText.setText("");
        }
    }

    //批量添加
    private void addBatch() {
        String input = getInput();
        clearInput();
        int num = 0;
        try {
            num = Integer.valueOf(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num <= 0) {
            num = 10;
        }
        Log.d(TAG, "insertBatch start num="+num);

        List<Note> list = new ArrayList<>();
        for (int i=0; i<num; i++) {
            final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            String comment = "Added on " + df.format(new Date());

            Note note = new Note();
            note.setText("批量添加数据"+i);
            note.setComment(comment);
            note.setDate(new Date());
            note.setType(NoteType.TEXT);
//            noteDao.insert(note);
//            Log.d(TAG, "Inserted new note, ID: " + note.getId());
            list.add(note);
        }
        DBManager.getInstance().getNoteDataSource().insertBatch(list);
        Log.d(TAG, "insertBatch end");

        query();

    }

    private void replace() {
        //默认更新第二条数据
        List<Note> list = DBManager.getInstance().getNoteDataSource().query(0, 10);
        if (list != null && list.size() > 1) {
            Note note = list.get(1);
            Log.d(TAG, "replace 默认更新第二条数据 before="+note);
            note.setText("replace before id= "+note.getId());
            note.setComment("replace time= "+System.currentTimeMillis());
            note.setDate(new Date());
            Log.d(TAG, "replace start "+note);

            DBManager.getInstance().getNoteDataSource().insertOrReplace(note);
            Log.d(TAG, "replace end");

//            note.setId((long) 0);//新插入
//            note.setComment("replace new Insert");
//            DBManager.getInstance().getNoteDataSource().insertOrReplace(note);

            query();
        }
    }

    private void query() {
        //查询参数
        String input = getInput();//输入查询条数
        clearInput();
        List<Note> list = null;
        Log.d(TAG, "query start");
        if (TextUtils.isEmpty(input)) {
            list = DBManager.getInstance().getNoteDataSource().queryAll();
        } else {
            int num = 0;
            try {
                num = Integer.valueOf(input);
            }catch (Exception e) {
                e.printStackTrace();
            }
            if (num <=0 ) {
                num = 10;
            }

            list = DBManager.getInstance().getNoteDataSource().query(0, 10);
        }



        if (list != null) {
            Log.d(TAG, "query end size="+list.size());
            notesAdapter.setNotes(list);
        }

    }

    private void update() {
        //默认修改第一条数据
        List<Note> list = DBManager.getInstance().getNoteDataSource().query(0, 10);
        if (list != null && list.size() > 0) {
            Note note = list.get(0);
            Log.d(TAG, "update 默认修改第一条数据 before="+note);
            note.setComment("new 默认修改第一条数据 update "+System.currentTimeMillis());
            note.setDate(new Date());
            Log.d(TAG, "update start ");

            DBManager.getInstance().getNoteDataSource().update(note);
            Log.d(TAG, "update end ");

            query();
        }
    }

    private void delAll() {
        //根据id清空数据

        DBManager.getInstance().getNoteDataSource().delBatch(null);

        query();
    }

}