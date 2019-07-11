package frc.robot;

import javax.lang.model.util.ElementScanner6;

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

    public final double cameraXOffset=14.5;
    public final double cameraYOffset=8;

    public DetectedObject mostRecentObject;
    /**
     * All reports will be of the form [typeOfObject, XPosition, Yposition, theta]
     * e.g.  "LeftTarget 10.2 11.3 -45.0"
     * @return
     */
    public boolean checkForInstructions()
    {
/*
        DetectedObject fake=new DetectedObject();
        fake.x=30;
        fake.y=30;
        fake.theta=0;
        fake.typeOfOjbect="Target";
        fake.timeOfDetection=System.currentTimeMillis();

        mostRecentObject=fake;
        return;*/

        if (Io.jevoisPort.getBytesReceived()==0) 

        {System.out.println("No data receieved");
            return false;//No data. Go home.
    }
        try
        {
       String inputData= Io.jevoisPort.readString();
       System.out.println("Data received: "+inputData);
       Io.writeToDebug(inputData);

       
       String[] allStrings= inputData.split("\\s+");//Spit by any whitespace.  Includes spaces or end of line
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
               else if (allStrings[i].equals("targets"))
               {
                   //System.out.println("Target");
                   currentObject=allStrings[i];
                   currentIndex++;
               }
               else
               {
                   //System.out.println("Something else: "+allStrings[i]);
                   currentIndex=0;//You are lost.  Go back to the beginning
               }
               break;
               case 1:
               {
                   //System.out.println("Index 1: "+allStrings[i]);
                   currentX=Double.parseDouble(allStrings[i]);
                   currentIndex++;
               }
               break;
               case 2:
               {
                   //System.out.println("Index 2: "+allStrings[i]);
                   currentY=Double.parseDouble(allStrings[i]);
                   currentIndex++;
               }
               break;
               case 3:
               {
                   //System.out.println("Index 3: "+ allStrings[i]);
                   currentTheta=Double.parseDouble(allStrings[i]);
                   currentIndex=0;
                   DetectedObject newObject=new DetectedObject();
                   newObject.typeOfOjbect=currentObject;
                   newObject.x=currentX+cameraXOffset;
                   newObject.y=currentY+cameraYOffset;
                   newObject.theta=currentTheta;
                   newObject.timeOfDetection=System.currentTimeMillis();
                   mostRecentObject=newObject;
                   Io.writeToDebug("New Object detected");
                   return true;
               }
               default:
                    System.out.println("What the hell? "+currentIndex);
                    Io.writeToDebug("What the hell? "+currentIndex);
                    currentIndex=0;
           }
       }
    }
    catch(Exception ex)
    {
        //Something went wrong.  Start again and hope for the best
        System.out.println("Exception reading serial port");
        Io.writeToDebug("Exception reading serial port: "+ex.getMessage());
        currentIndex=0;
    }
    return false;
    
    
        
 }
}