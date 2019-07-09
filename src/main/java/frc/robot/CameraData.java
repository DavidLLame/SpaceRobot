package frc.robot;

import javax.lang.model.util.ElementScanner6;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This class will read the serial port and if there are strings found
 * from the jevois camera, will parse the into detected objects.
 */
public class CameraData
{



    int currentIndex=0;  //Will be used to count fields
    String currentObject;
    double currentX;
    double currentY;
    double currentTheta;

    /**
     * All reports will be of the form [typeOfObject, XPosition, Yposition, theta]
     * e.g.  "LeftTarget 10.2 11.3 -45.0"
     * @return
     */
    public void checkForInstructions()
    {
        
        try
        {
       SmartDashboard.putString("dumbserial","Hi"); 
       String inputData= Io.jevoisPort.readString();
       System.out.println(inputData);
       SmartDashboard.putString("serialdata", inputData);
       /*String[] allStrings= inputData.split("\\s+");//Spit by any whitespace.  Includes spaces or end of line
       for(int i=0;i<allStrings.length;i++)
       {
           switch(currentIndex)
           {
               case 0: 
                if (allStrings[i].equals("LeftTarget")||allStrings[i].equals("RightTarget"))
               {
                   currentObject=allStrings[i];
                   currentIndex++;
               }
               else
               {
                   currentIndex=0;//You are lost.  Go back to the beginning
               }
               break;
               case 1:
               {
                   currentX=Double.parseDouble(allStrings[i]);
                   currentIndex++;
               }
               break;
               case 2:
               {
                   currentY=Double.parseDouble(allStrings[i]);
                   currentIndex++;
               }
               break;
               case 3:
               {
                   currentTheta=Double.parseDouble(allStrings[i]);
                   currentIndex=0;
                   DetectedObject newObject=new DetectedObject();
                   newObject.typeOfOjbect=currentObject;
                   newObject.x=currentX;
                   newObject.y=currentY;
                   newObject.theta=currentTheta;
                   newObject.timeOfDetection=System.currentTimeMillis();
                   //Now, I don't know what I'm going to do with this object,
                   //so for the moment, I'm going to do nothing.
               }
           }
       }*/
    }
    catch(Exception ex)
    {
        //Something went wrong.  Start again and hope for the best
        currentIndex=0;
        SmartDashboard.putString("serialdata", "threw an exception seeking serial data "+ex.getMessage());
        System.out.println("Exception reading data: "+ex.getMessage());
    }
    
    
        
    }
}