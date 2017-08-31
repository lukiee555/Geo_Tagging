package lokeshsoni.com.geo_tagging;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    Button read,set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        read = (Button)findViewById(R.id.main_read);
        set = (Button)findViewById(R.id.main_set);



    }
}
