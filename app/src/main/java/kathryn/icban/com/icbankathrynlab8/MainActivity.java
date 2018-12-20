package kathryn.icban.com.icbankathrynlab8;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText eName, eGender, eAge;
    TextView tName, tAge, tGender;
    //FirebaseDatabase db;
    DatabaseReference db;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance().getReference("student");

        eName = findViewById(R.id.etName);
        eGender = findViewById(R.id.etGender);
        eAge = findViewById(R.id.etAge);

        tName = findViewById(R.id.tvName);
        tGender = findViewById(R.id.tvGender);
        tAge = findViewById(R.id.tvAge);
    }

    public void save(View v) {
        String name, age, gender, key;

        name = beautifyTextField(eName);
        age = beautifyTextField(eAge);
        gender = beautifyTextField(eGender);

        key = db.push().getKey();

        db.child(key).setValue(new Person(name, age, gender));

        Toast("Record has been added");
    }

    public void search(View v) {
        final String name = beautifyTextField(eName).toLowerCase();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ss : dataSnapshot.getChildren()) {
                    Person p = ss.getValue(Person.class);
                    String pname = p.getName().toLowerCase();

                    if(!pname.equals(name))
                        continue;

                    else {
                        tName.setText(p.getName());
                        tAge.setText(p.getAge());
                        tGender.setText(p.getGender());
                        Toast("Record has been found");
                        return;
                    }
                }

                tName.setText("");
                tAge.setText("");
                tGender.setText("");
                Toast("Record cannot be found...");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        db.addValueEventListener(listener);
    }


    protected void Toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    protected String beautifyTextField(EditText et) {
        return et.getText().toString().trim();
    }
}

