package com.htyhbz.yhyg.net;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by zongshuo on 2017/7/21.
 */
public class HighRequest extends StringRequest {
    private Priority mPriority = Priority.HIGH;

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public HighRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }
}
