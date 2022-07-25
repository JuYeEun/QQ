package com.example.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

///07.25///
public class MainActivity extends AppCompatActivity {
    //(2)
    RecyclerView rvStation;
    static RequestQueue requestQueue;
    String TAG = "STATION LIST";
    StationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        adapter = new StationAdapter(); // 1. adapter 생성 / StationAdapterActivity 확인/
        rvStation = findViewById(R.id.recyclerview); //main_activity xml 의 recyclerview 위젯
        rvStation.setLayoutManager(layoutManager); // recyclerview 에 레이아웃 매니저
        rvStation.setAdapter(adapter); // 리사이클러뷰의 어댑터 할당

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //
        Button Btn = (Button)findViewById(R.id.Btn);
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(
                        //api 요청 url //
                        Request.Method.GET, "http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId?ServiceKey=RIUiLOdxJGSqOn7vg2HIx7pCUNarmHxnDEr6MzGJbgpLZv86ih6ZdA4%2Fil6yW5ptYbh4KjixAZJQxXN40CsSUQ%3D%3D&stId=120000001&stNm=북가좌&busRouteId=100100118",

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String curr_tag = "Model";

                                //item 객체의 새로운 메모리 할당. (???), item = Model

                                StationAdapter stationAdapter = new StationAdapter();
                                stationAdapter.clearItems();

                                try {


                                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                                    factory.setNamespaceAware(true);

                                    XmlPullParser xpp = factory.newPullParser();
                                    //
                                    xpp.setInput(new StringReader(response));
                                    int eventType = xpp.getEventType(); // eventType <= xpp
                                    while (eventType != XmlPullParser.END_DOCUMENT) {
                                        //
                                        if (eventType == XmlPullParser.START_DOCUMENT) {

                                        } else if (eventType == XmlPullParser.START_TAG) {
                                            curr_tag = xpp.getName();
                                            if (xpp.getName().equals("Model")) {
                                                stationAdapter = new StationAdapter();
                                            }

                                        } else if (eventType == XmlPullParser.END_TAG) {
                                            if (xpp.getName().equals("Model")) {
                                                if (stationAdapter.checkRecAllData()) {
                                                    adapter.addItem(stationAdapter);
                                                }
                                            }
                                            curr_tag = "Model";
                                        } else if (eventType == XmlPullParser.TEXT) {

                                            switch (curr_tag) {
                                                case "plainNo1":
                                                    stationAdapter.plainNo1 = xpp.getText();
                                                    break;
                                                case "stNm":
                                                    stationAdapter.stNm = xpp.getText();
                                                    break;
                                            }
                                        }
                                        eventType = xpp.next(); // 처음에 next(); X , Document를 못만남
                                    }
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

/*

    ///0725 -2 //

    //버튼을 클릭하면 실행 (문서읽어오기 )
    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.button:

                new Thread(new Runnable() {
                    class PullParser {
                    }

                    @Override
                    public void run() {


                        String adress = "http://ws.bus.go.kr/api/rest/buspos/getLowBusPosByRtid"
                                + "?key=" + key + "&stId=112000001";
//xml문서의 주소에 스트림 연결 - > 데이터를 읽어온다
                        //key, 필수값을 문서에서 확인

                        try {

                            URL url = new URL(adress); //URL 객체를 만들어줌

                            InputStream is = url.openStream();// is = 바이트스트림 ( 문자열로 받기위해 isr 필요)
                            InputStreamReader isr = new InputStreamReader(is);
                            //is = byte stream -> isr =  StringStream => PullParser

                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            XmlPullParser xpp = (XmlPullParser) factory.newPullParser();
                            xpp.setInput(isr);
                            //xpp를 이용 (xml/isr/을 분석)

                            int eventType = xpp.getEventType();

                            String tagName;
                            StringBuffer buffer = null;

                            while (eventType != XmlPullParser.END_DOCUMENT) {

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(MainActivity.this, "파싱시작", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                        break;
                                    ///xpp <= isr
                                    case XmlPullParser.START_TAG:
                                        tagName = xpp.getName();
                                        if (tagName.equals("getLowBusPos")) {
                                            buffer = new StringBuffer();

                                        } else if (tagName.equals("arrmsg1")) {
                                            buffer.append("도착정보 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText() + "\n");
                                        } else if (tagName.equals("stNm")) {
                                            buffer.append("정류소명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText() + "\n");
                                        }
                                        break;

                                    case XmlPullParser.TEXT:
                                        break;

                                    case XmlPullParser.END_TAG:
                                        tagName = xpp.getName();
                                        if (tagName.equals("getLowBusPos")) {

                                            edit.setText(buffer.toString());


                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(MainActivity.this, "end", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                }
                            }
                        } catch (MalformedURLException malformedURLException) {
                            malformedURLException.printStackTrace();
                        } catch (XmlPullParserException xmlPullParserException) {
                            xmlPullParserException.printStackTrace();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                    }
                });
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
}
*/