package com.example.gahui.httptest;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



class VideoNewsService {

static List<News> newses = new ArrayList<>();
static List<News> sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                    // 指定访问的服务器地址是电脑本机
                            .url("http://云服务器公网IP:8080/VideoNews/ListServelet")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                                        parseXML(responseData);

                } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                            }
        }).start();
        return newses;
        }

        private static List<News> parseXML(String xmlData) throws Exception {
        News news = null;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlData));
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_TAG:

                    if ("id".equals(parser.getName())) {
                        int id = Integer.valueOf(parser.nextText());
                        news = new News();
                        news.setId(id);
                        } else if ("title".equals(parser.getName())) {
                                                news.setTitle(parser.nextText());
                        } else if ("timelength".equals(parser.getName())) {
                                                news.setTimelength(Integer.valueOf(parser.nextText()));
                                            }
                    break;
                case XmlPullParser.END_TAG: {
                    if ("news".equals(parser.getName())) {
                        newses.add(news);
                        news = null;
                        }
                    break;
                }
                default:
                    break;
            }
            event = parser.next();

        }
        return newses;

        }
}
