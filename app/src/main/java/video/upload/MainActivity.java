package video.upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

     private ProgressBar progressBar;

     private VideoView videoView;
     private Uri videoUris;
     MediaController mediaController;
     private StorageReference mstorageref;
     Button uploadbtn;
     Button choosebuttons;
     private static final int PICK_Video=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      choosebuttons= findViewById(R.id.choose);
      uploadbtn=findViewById(R.id.upload);
      videoView= findViewById(R.id.Videoviews);
      progressBar=findViewById(R.id.progress);

      mediaController=new MediaController(this);
      mstorageref= FirebaseStorage.getInstance().getReference("videos");

      videoView.setMediaController(mediaController);

      mediaController.setAnchorView(videoView);

      videoView.start();

      choosebuttons.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              choosesvideo();

          }


      });

      uploadbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              uploadvideos();
          }
      });



    }

    private void choosesvideo(){

        Intent intents=new Intent();
        intents.setType("video/*");
        intents.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intents,PICK_Video);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_Video && resultCode==RESULT_OK && data!=null &&data.getData()!=null){
            videoUris=data.getData();
            videoView.setVideoURI(videoUris);


        }
    }
    private String getfileextensions(Uri videoUris){
        ContentResolver contentResolvers=getContentResolver();
        MimeTypeMap mimeTypess= MimeTypeMap.getSingleton();
        return mimeTypess.getExtensionFromMimeType(contentResolvers.getType(videoUris));

    }
    private void uploadvideos(){
       progressBar.setVisibility(View.VISIBLE);

       if (videoUris !=null){

           StorageReference references=mstorageref.child(System.currentTimeMillis()+"."+getfileextensions(videoUris));

           references.putFile(videoUris).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 progressBar.setVisibility(View.INVISIBLE);
                   Toast.makeText(MainActivity.this,"sucess",Toast.LENGTH_SHORT).show();


               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();


               }
           });
       }else{

           Toast.makeText(MainActivity.this,"No file selected",Toast.LENGTH_SHORT).show();


       }

    }

}