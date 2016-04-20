package com.grayditch.netarea.data.network;

import android.net.ConnectivityManager;

import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.UserDetails;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gerard on 9/04/16.
 */
public class NetareaClient {
    private static final String NETAREA_URL = "https://mitra.upc.es/SIA/ACCES_PERFIL.AUTENTIFICAR2";
    private static final CharSequence LOGOUT_OPTION = "Surt / Campus Digital";

    private final OkHttpClient client;
    private final NetareaPageParser netareaPageParser;
    private final NetworkUtils networkUtils;

    @Inject
    public NetareaClient(OkHttpClient client, NetareaPageParser netareaPageParser, NetworkUtils networkUtils) {
        this.client = client;
        this.netareaPageParser = netareaPageParser;
        this.networkUtils = networkUtils;
    }

    public void qualifications(UserDetails userDetails, Callback callback) {
        RequestBody requestBody = this.getRequestBody(userDetails.getUsername(), userDetails.getPassword());
        Request request = this.generateRequestWithRequestBody(requestBody);
        this.executeRequest(request, callback);
    }

    private RequestBody getRequestBody(String username, String password) {
        return new FormBody.Builder()
                .add("v_procediment", "NETAREA.INICI")
                .add("v_username", username)
                .add("v_password", password)
                .build();
    }

    private Request generateRequestWithRequestBody(RequestBody requestBody) {
        return new Request.Builder()
                .url(NETAREA_URL)
                .post(requestBody)
                .build();
    }

    private void executeRequest(final Request request, final Callback callback) {
        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            if (body.contains(LOGOUT_OPTION)) {
                List<Subject> subjects = netareaPageParser.parse(body);
                callback.onSuccess(subjects);
            } else {
                callback.onError(new Exception("Login was not successful"));
            }
        } catch (IOException e) {
            callback.onError(e);
        }
    }

    public boolean isNetworkAvailable() {
        return networkUtils.isNetworkAvailable();
    }

    public interface Callback {
        void onSuccess(List<Subject> subjects);

        void onError(Throwable e);
    }
}
