package frc.robot;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class CameraStream
{

    private UsbCamera camera1;
    private UsbCamera camera2;
    private CvSink cvSink;
    private CvSource outputStream;

    public void initCamera()
    {
        System.out.println("About to init");
        camera1 = CameraServer.getInstance().startAutomaticCapture(0);
       // camera2 = CameraServer.getInstance().startAutomaticCapture(1);
        
      System.out.println("It didn't crash");
        camera1.setResolution(320, 240);
      //  camera2.setResolution(320, 240);
        cvSink = CameraServer.getInstance().getVideo();
        outputStream = CameraServer.getInstance().putVideo("Blur", 320, 240);
        lastSwitch=System.currentTimeMillis();
        cvSink.setSource(camera1);
    }

    private long lastSwitch;
    private boolean cam1=true;
    public void grabFrame()
    {
/*
      if (System.currentTimeMillis()-lastSwitch>5000)
      {
        System.out.println("Grabbing");
        if (cam1)
        {
          System.out.println("switch to 2");
          cam1=false;
          cvSink.setSource(camera2);
        }
        else
        {
          System.out.println("SWITCH TO 1");
          cam1=true;
          cvSink.setSource(camera1);
        }
        lastSwitch=System.currentTimeMillis();
      }*/
        Mat source = new Mat();
        cvSink.grabFrame(source);
        outputStream.putFrame(source);

    }

}