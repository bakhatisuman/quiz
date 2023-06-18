package com.example.quizapp.coredi;

import android.content.Context;
import com.example.quizapp.network.RetrofitService;
import com.example.quizapp.repo.QuizRepo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    private final String HTTP_DIR_CACHE = "quiz";
    private final Long CACHE_SIZE = 10 * 1024 * 1024L;
//    private final String HEADER_TYPE = "Authorization";
    private final String API_BASE_URL = "https://opentdb.com/";


    @Singleton
    @Provides
    public RetrofitService provideRetrofitService(Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }

    @Singleton
    @Provides
    public QuizRepo provideRepo(RetrofitService service, @ApplicationContext Context context) {
        return new QuizRepo(service, context);
    }


    @Singleton
    @Provides
    public Cache provideCache(@ApplicationContext Context context) {
        return new Cache(new File(context.getCacheDir(), HTTP_DIR_CACHE), CACHE_SIZE);
    }


    @Singleton
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


    @Singleton
    @Provides
    public OkHttpClient provideHttpClient(
            @ApplicationContext Context context, HttpLoggingInterceptor httpLoggingInterceptor, Cache cache) {

        return new OkHttpClient.Builder().addInterceptor(chain -> {
                    Request original = chain.request();
                    String header = "";

                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(30, TimeUnit.DAYS)
                            .build();


                    Request.Builder builder = original.newBuilder()
//                        .addHeader(HEADER_TYPE, header)
//                        .addHeader("App-Type", "farmer")
                            .method(original.method(), original.body());

                    Request request;

                    String isCacheAvailable = original.header("isCacheAvailable");
                    if (isCacheAvailable != null) {
                        if (isCacheAvailable.equals("yes")) {
                            request = builder.cacheControl(cacheControl).build();
                        } else {
                            request = builder.build();
                        }
                    } else {
                        request = builder.build();
                    }

                    return chain.proceed(request);

                }).
                addInterceptor(httpLoggingInterceptor).readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .cache(cache)
                .build();

    }


    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

    @Singleton
    @Provides
    public GsonConverterFactory provideGSonConverterFactory(Gson gSon) {
        return GsonConverterFactory.create(gSon);
    }


    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient, GsonConverterFactory gSonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(gSonConverterFactory).client(okHttpClient).build();

    }


}
