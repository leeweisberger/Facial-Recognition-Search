package lee.andrew.cerner.facial_recognition_search;

import lee.andrew.cerner.facial_recognition_search.CameraPreview.TakePictureTask;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CameraFragment extends Fragment {
    private Camera mCamera = null;
    private CameraPreview mPreview;
    private FrameLayout layout;
    protected static TextView scanning;
    protected static TextView identifying;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);
        inflater.inflate(R.layout.fragment_camera, container, false);
        layout = (FrameLayout) v.findViewById(R.id.camera_preview);
        initializeCameraPreview();
        scanning = (TextView) v.findViewById(R.id.scanning);
        identifying = (TextView) v.findViewById(R.id.identifying);

        Button captureButton = (Button) v.findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CameraPreview.makeDialogue(getActivity(), "Preparing Image", "Wait while we prepare your image...");

                TakePictureTask takePictureTask = CameraFragment.this.mPreview.new TakePictureTask();
                takePictureTask.execute();
            }
        });
        return v;
    }


    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
            Log.d("Lee", "camera opened");
        } catch (Exception e) {
        }
        return c;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            removeCameraPreview();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            initializeCameraPreview();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCamera != null) {
            removeCameraPreview();
        }
    }

    private void initializeCameraPreview() {
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getActivity(), mCamera);
        layout.addView(mPreview);
    }

    private void removeCameraPreview() {
        layout.removeView(mPreview);
        mCamera.setPreviewCallback(null);
        mPreview.getHolder().removeCallback(mPreview);
        mCamera.release();
        mCamera = null;
        Log.d("Lee", "Camera Released");
    }


    
    
    

}
