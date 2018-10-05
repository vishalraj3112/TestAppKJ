package com.example.android.testappkj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.FirebaseVisionText.TextBlock;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static Button selectImage;
    private static Button process;
    private static ImageView imageView;
    private static TextView textView;

    private static Uri imageUri = null;
    private Bitmap bmp;

    private final int select_photo = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setListeners();

    }

    private void init() {
        selectImage = (Button) findViewById(R.id.select_image);
        process =(Button) findViewById(R.id.process);

        imageView = (ImageView) findViewById(R.id.share_imageview);
        textView =(TextView) findViewById(R.id.textview);

    }

    private void setListeners() {
        selectImage.setOnClickListener(this);
        process.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_image:
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
//                if (in.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(in,"SELECT IMAGE TO CONTINUE"), select_photo);
//                }

                break;


            case R.id.process  :
                if(bmp==null){
                    Toast.makeText(MainActivity.this,"First upload image",Toast.LENGTH_SHORT).show();
                }else{

                    FirebaseVisionImage image=FirebaseVisionImage.fromBitmap(bmp);
                    FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                            .getOnDeviceTextRecognizer();

                    textRecognizer.processImage(image)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText result) {

                                  //  Log.d("Image Data",result.getText().toString()+" ");

                                    String resultText = result.getText();
                                    textView.setText(resultText);
                                    for (TextBlock block: result.getTextBlocks()) {
                                        String blockText = block.getText();
                                        Float bloxckConfidence = block.getConfidence();
                                        //String txt = textView.getText().toString()+"\n";
                                        //txt = txt + blockText.toString();

                                        Log.d("Block Text",blockText+" ");
                                        List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
                                        Point[] blockCornerPoints = block.getCornerPoints();
                                        Rect blockFrame = block.getBoundingBox();
                                        for (FirebaseVisionText.Line line: block.getLines()) {
                                            String lineText = line.getText();


                                            Float lineConfidence = line.getConfidence();
                                            List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                                            Point[] lineCornerPoints = line.getCornerPoints();
                                            Rect lineFrame = line.getBoundingBox();
                                            for (FirebaseVisionText.Element element: line.getElements()) {
                                                String elementText = element.getText();
                                                Float elementConfidence = element.getConfidence();
                                                List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                                                Point[] elementCornerPoints = element.getCornerPoints();
                                                Rect elementFrame = element.getBoundingBox();
                                            }
                                        }
                                    }
                                    // Task completed successfully
                                    // ...
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                        }
                                    });



                }





        }
    }


    protected void onActivityResult(int requestcode, int resultcode, Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case select_photo:
                if (resultcode == RESULT_OK) {

//                    Bitmap bitmap = (Bitmap) imagereturnintent.getExtras().get("data");
//                    bmp=bitmap;

//                    if (bitmap != null) {
                        //imageView.setImageBitmap(bitmap);// Set image over
                        // bitmap
//                    } else {
//                        Toast.makeText(MainActivity.this,
//                                "Error while decoding image.",
//                                Toast.LENGTH_SHORT).show();
//                    }

                    Uri imageUri = imagereturnintent.getData();


                    //String path = MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(), bitmap, "Title", null);
                    //imageUri = Uri.parse(path);

                    CropImage
                            .activity(imageUri)
                            .setOutputCompressQuality(100)
                            .start(this);

                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE :
                CropImage.ActivityResult res = CropImage.getActivityResult(imagereturnintent);
                Uri resultUri = res.getUri();
                //File file = new File(resultUri.getPath());
                Bitmap bb = BitmapFactory.decodeFile(resultUri.getPath());
                bmp = bb;
                imageView.setImageBitmap(bb);
                break;
        }

    }
}