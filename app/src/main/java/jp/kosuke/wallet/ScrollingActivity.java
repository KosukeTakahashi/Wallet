package jp.kosuke.wallet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    boolean FLAG_CREATING = false;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RelativeLayout panel = (RelativeLayout)findViewById(R.id.panel);
        final RelativeLayout create = (RelativeLayout)findViewById(R.id.createPanel);
        final RelativeLayout info = (RelativeLayout)findViewById(R.id.infoPanel);
        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        final RecyclerView recycler = (RecyclerView)findViewById(R.id.recycler);

        create.setVisibility(View.INVISIBLE);

        openDB(getApplicationContext());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FLAG_CREATING) {
                    EditText editChange = (EditText)create.findViewById(R.id.editChange);
                    final EditText editWhen = (EditText)create.findViewById(R.id.editWhen);
                    TextInputLayout editWhenLayout = (TextInputLayout) create.findViewById(R.id.editWhenLayout);
                    EditText editDescription = (EditText)create.findViewById(R.id.editDescription);

                    final int[] when = new int[3];
                    String description = editDescription.getText().toString();
                    int change;

                    try {
                        change = Integer.parseInt(editChange.getText().toString());
                    } catch (NumberFormatException e) {
                        change = 0;
                    }

                    editWhenLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog(editWhen, when);
                        }
                    });


                    int cx = (fab.getLeft() + fab.getRight()) / 2;
                    int cy = (fab.getTop() + fab.getBottom()) / 2;
                    int initialRadius = panel.getWidth();

                    Animator anim = ViewAnimationUtils.createCircularReveal(panel, cx, cy, initialRadius, 64);
                    anim.setDuration(300L);
                    anim.start();
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            fab.setImageResource(R.drawable.plus);
                            info.setVisibility(View.VISIBLE);
                            create.setVisibility(View.INVISIBLE);
                        }
                    });

                    FLAG_CREATING = false;
                } else {
                    int cx = (fab.getLeft() + fab.getRight()) / 2;
                    int cy = (fab.getTop() + fab.getBottom()) / 2;
                    int finalRadius = Math.max(panel.getHeight(), panel.getWidth());

                    Animator anim = ViewAnimationUtils.createCircularReveal(panel, cx, cy, 64, finalRadius);
                    anim.setDuration(300L);
                    anim.start();
                    create.setVisibility(View.VISIBLE);
                    info.setVisibility(View.INVISIBLE);
                    fab.setImageResource(R.drawable.check);

                    FLAG_CREATING = true;
                }
            }
        });
    }

    public void showDialog(final EditText editWhen, final int[] when) {
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //set
                try {
                    when[0] = i;
                    when[1] = i1 + 1;
                    when[2] = i2;
                } catch (NumberFormatException e) {
                    when[0] = calendar.get(Calendar.YEAR);
                    when[1] = calendar.get(Calendar.MONTH);
                    when[2] = calendar.get(Calendar.DAY_OF_MONTH);
                }
                editWhen.setText(i + "/" + (i1 + 1) + "/" + i2);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void openDB(Context context) {
        SQLOpenHelper helper = new SQLOpenHelper(context);
        db = helper.getWritableDatabase();
    }


    public interface RecyclerListener {
        void onItemClicked(View view, int position);
    }

    public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

        public List<EventItem> dataArray;
        public RecyclerListener listener;

        public ViewAdapter(List<EventItem> dataArray, RecyclerListener listener) {
            this.dataArray = dataArray;
            this.listener = listener;
        }

        @Override
        public ViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewAdapter.ViewHolder holder, int position) {
            EventItem event = dataArray.get(position);

            holder.change.setText(String.valueOf(event.getChange()));
            holder.description.setText(event.getDescription());
            holder.when.setText(String.format("%d/$d/$d", event.getYear(), event.getMonth(), event.getDay()));

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 16/07/10 編集アクティビティへ
                    // TODO: 16/07/10 MaterialDesign
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 16/07/10 削除
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataArray == null ? 0 : dataArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView change;
            TextView when;
            TextView description;
            Button btnEdit;
            Button btnDelete;

            public ViewHolder(View itemView) {
                super(itemView);
                change = (TextView)itemView.findViewById(R.id.change);
                when = (TextView)itemView.findViewById(R.id.when);
                description = (TextView)itemView.findViewById(R.id.description);
                btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
                btnDelete = (Button)itemView.findViewById(R.id.btnDelete);
            }
        }
    }

    public static class SQLOpenHelper extends SQLiteOpenHelper {
        public SQLOpenHelper(Context context) {
            super(context, EventItemCatHands.TABLE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(EventItemCatHands.SQL_CREATE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            db.execSQL("DROP TABLE " + EventItemCatHands.TABLE_NAME);
            onCreate(db);
        }
    }
}
