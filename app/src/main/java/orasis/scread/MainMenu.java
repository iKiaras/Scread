package orasis.scread;

/**
 * Created by ikiaras on 31/3/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;

import static android.content.ContentValues.TAG;

/**
 * Created by ikiaras on 24/3/2017.
 */

public class MainMenu extends Activity implements View.OnClickListener{

    private CameraSource mCameraSource;
    private static final int RC_OCR_CAPTURE = 9003;
    private Button photo;
    private Button read;

    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intro.mTts.stop();

//        setContentView(R.layout.activity_main);
        setContentView(R.layout.main_menu);

//        statusMessage = (TextView)findViewById(R.id.status_message);
//        textValue = (TextView)findViewById(R.id.text_value);
//        textValue = (TextView)findViewById(R.id.textView2);
//
//        read = (Button) findViewById(R.id.buttonRead);

        photo = (Button) findViewById(R.id.buttonPhoto);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.buttonPhoto).setOnClickListener(this);
        Intro.mTts.speak("Press anywhere in the screen to initiate the detection.",  TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.read_text) {
        if (v.getId() == R.id.buttonPhoto) {
            // launch Ocr capture activity.
            Intent intent = new Intent(this, OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, true);
            //intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());
            Intro.mTts.stop();
            startActivityForResult(intent, RC_OCR_CAPTURE);
        }
    }

//    private void createCameraSource(boolean autoFocus, boolean useFlash) {
//        Context context = getApplicationContext();
//
//        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
//
//        // TODO: Set the TextRecognizer's Processor.
//        textRecognizer.setProcessor(new OcrDetectorProcessor(null));
//        // TODO: Check if the TextRecognizer is operational.
//        if (!textRecognizer.isOperational()) {
//
//            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
//            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;
//
//            if (hasLowStorage) {
//                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
//            }
//        }
//
//        // TODO: Create the mCameraSource using the TextRecognizer.
//        mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
//                .setFacing(CameraSource.CAMERA_FACING_BACK)
//                .setRequestedPreviewSize(1280,1024)
//                .setRequestedFps(15.0f)
//                .setAutoFocusEnabled(true)
//                .build();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);

//                    statusMessage.setText(R.string.ocr_success);
                    textValue.setText(text);
//                    String text = data.getStringExtra(OcrDetectorProcessor.TextBlockObject);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    //statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
//                statusMessage.setText(String.format(getString(R.string.ocr_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}

