package com.tangdi.rxjavafroandroid.network;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;

/**
 * Created by tangdi on 9/23/17.
 */

public interface NetApi {
    @HTTP(method = "GET",path = "/",hasBody = true)
    Observable<LogingResult> Login(@Body LogingRequest request);
}
