package com.duaruang.pnmportal.adapter;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.activity.EventMateriActivity;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.Faq;
import com.duaruang.pnmportal.data.Material;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class MaterialAdapter extends
        RecyclerView.Adapter<MaterialAdapter.MyViewHolder> {

    private List<Material> list_item ;
    public Context mcontext;



    public MaterialAdapter(List<Material> list, Context context) {

        list_item = list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public MaterialAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_event_materi, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position ) {

        final Material material = list_item.get(position);
        viewHolder.tvNamaFile.setText(material.getNamaFile());
        viewHolder.tvTipeFile.setText("Tipe file: " + material.getTipeFile());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (mcontext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.v(TAG, "Permission is granted");

                        if (isDownloadManagerAvailable(mcontext)){
                            Toast.makeText(mcontext, "Downloading material event", Toast.LENGTH_SHORT).show();

                            String url = Config.KEY_URL_DOWNLOAD_MATERIAL+material.getNamaFile();
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                            request.setDescription("Materi Event");
                            request.setTitle("Download Material Event");
                            // in order for this if to run, you must use the android 3.2 to compile your app
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            }
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, material.getNamaFile());

                            // get download service and enqueue file
                            DownloadManager manager = (DownloadManager) mcontext.getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);
                        } else {
                            Toast.makeText(mcontext, "Download Manager tidak tersedia!", Toast.LENGTH_LONG).show();
                        }

                    } else {

                        Log.v(TAG, "Permission is revoked");
                        Toast.makeText(mcontext, "Error, penyimpanan file tidak diperbolehkan", Toast.LENGTH_LONG).show();

                    }
                } else { //permission is automatically granted on sdk<23 upon installation
                    Log.v(TAG, "Permission is granted");

                }


            }
        });
    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNamaFile;
        public TextView tvTipeFile;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvNamaFile = (TextView) itemLayoutView.findViewById(R.id.materi_namafile);
            tvTipeFile = (TextView) itemLayoutView.findViewById(R.id.materi_tipefile);

        }
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }
}