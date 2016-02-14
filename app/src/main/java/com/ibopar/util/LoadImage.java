package com.ibopar.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ibopar.R;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class LoadImage {

    private static ImageLoader loader = null;
    private static final String LOG = LoadImage.class.getSimpleName().toString();

    public static void load( ImageView view, Context context, String url){

        Log.i(LOG, "Iniciando carregamento da imagem de URL: " + url);

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.sem_programacao)
                .showImageOnFail(R.drawable.sem_programacao)
                .showImageOnLoading(R.drawable.gif_load)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(displayImageOptions)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .threadPoolSize(2)
                .writeDebugLogs()
                .build();

        loader = ImageLoader.getInstance();
        loader.init(conf);
        loader.displayImage(url, view);
        loader.loadImage(url, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.i(LOG, "Carregamento da URL " + imageUri + " finalizado");
            }
        });

        //Bitmap bmp = loader.loadImageSync(url);
    }
}
