package com.grayditch.netarea.data.network;

import com.grayditch.netarea.domain.Subject;
import com.grayditch.netarea.domain.UserDetails;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by gerard on 9/04/16.
 */
public class NetareaClient {
    private static final String NETAREA_URL = "https://mitra.upc.es/SIA/ACCES_PERFIL.AUTENTIFICAR2";

    private final OkHttpClient client;
    private final NetareaPageParser netareaPageParser;

    @Inject
    public NetareaClient(OkHttpClient client, NetareaPageParser netareaPageParser) {
        this.client = client;
        this.netareaPageParser = netareaPageParser;
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
            if (response.isSuccessful()) {
                List<Subject> subjects = netareaPageParser.parse(response.body().string());
                callback.onSuccess(subjects);
            } else {
                callback.onError(new Exception(Integer.toString(response.code())));
            }
        } catch (IOException e) {
            callback.onError(e);
        }
    }

    public interface Callback {
        void onSuccess(List<Subject> subjects);

        void onError(Throwable e);
    }
}
