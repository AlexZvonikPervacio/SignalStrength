package pervacio.com.signalstrength.ui.activty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pervacio.com.signalstrength.R;
import pervacio.com.signalstrength.ui.fragment.MainContentFragment;

public class MainWifiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pager);

        MainContentFragment myFrag = new MainContentFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, myFrag).commit();
    }

}
