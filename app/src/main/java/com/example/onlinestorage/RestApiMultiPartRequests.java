package com.example.onlinestorage;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RestApiMultiPartRequests<T> extends Request<T> {

    private final Map<String, String> mStringParts;
    private final Map<String, File> mFileParts;
    private final Response.Listener<T> mListener;
    private MultipartEntityBuilder mBuilder;

    public RestApiMultiPartRequests(String url,
                                    Map<String, String> stringParts,
                                    Map<String, File> fileParts,
                                    Response.Listener<T> listener,
                                    Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mStringParts = stringParts;
        mFileParts = fileParts;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        if (mBuilder != null) {
            mBuilder = null;
        }
        mBuilder = MultipartEntityBuilder.create();
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setBoundary("_____" + System.currentTimeMillis() + "_____");
        mBuilder.setCharset(Consts.UTF_8);
        if (mStringParts != null) {
            for (Map.Entry<String, String> entry : mStringParts.entrySet()) {
                mBuilder.addTextBody(entry.getKey(), entry.getValue(), ContentType.create("text/plain", StandardCharsets.UTF_8));
            }
        }

        Log.e("Size", "Size: " + mFileParts.size());
        for (Map.Entry<String, File> entry : mFileParts.entrySet()) {
            ContentType imageContentType = ContentType.create("image/*");//MULTIPART_FORM_DATA;
            Log.d("", "Key " + entry.getKey());
            Log.d("", "Value " + entry.getValue());
            Log.d("", "Name " + entry.getValue().getName());
            //"userfile"
            mBuilder.addBinaryBody(entry.getKey(), entry.getValue(), imageContentType, entry.getValue().getName());
        }
    }

    @Override
    public String getBodyContentType() {
        return mBuilder.build().getContentType().getValue();
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mBuilder.build().writeTo(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }


    public HttpEntity getEntity() {
        return mBuilder.build();
    }


    @SuppressWarnings("unchecked")
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return (Response<T>) Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);

    }


}
