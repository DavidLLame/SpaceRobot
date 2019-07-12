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

    public final double cameraXOffset=-14.5;
    public final double cameraYOffset=8;


    private int numberTestFrames=0;
    private double cumulativeYawError=0;
    private final int testFramesToAverage=5;
    private double computedYawOffset=0;

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
         Io.writeToDebug("No data this time\r\n");
            return false;//No data. Go home.
    }
    boolean returnflag=false;
        try
        {
       String inputData= Io.jevoisPort.readString();
       System.out.println("Data received: "+inputData);
       Io.writeToDebug("New data: "+inputData+"\r\n");

       
       String[] allStrings= inputData.split("\\s+");//Spit by any whitespace.  Includes spaces or end of line
       Io.writeToDebug("Split to "+allStrings.length+"\r\n");
       for(int i=0;i<allStrings.length;i++)
       {
           Io.writeToDebug("Parsing "+allStrings[i]+" index is "+currentIndex+"\r\n");
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
                   Io.writeToDebug("Got lost - resetting index \r\n");
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
                   newObject.x=currentX-cameraXOffset;
                   Io.writeToDebug("x and offset "+currentX+" "+cameraXOffset+" "+newObject.x+"\r\n");
                   newObject.y=currentY-cameraYOffset;
                   newObject.theta=currentTheta-computedYawOffset; //compensate for camera mismount
                   newObject.timeOfDetection=System.currentTimeMillis();
                   mostRecentObject=newObject;
                   Io.writeToDebug("New Object detected\n");
                   returnflag=true;

                   if (this.numberTestFrames<this.testFramesToAverage)
                   {
                       this.cumulativeYawError+=currentTheta;
                       numberTestFrames++;
                       if (numberTestFrames==this.testFramesToAverage)
                       {
                           computedYawOffset=cumulativeYawError/(double)this.testFramesToAverage;
                       }
                   }
                   
                   
               }
               break;
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
    Io.writeToDebug("Current target position\r\n");
    Io.writeToDebug("x: "+mostRecentObject.x+"\r\n");
    Io.writeToDebug("y: "+mostRecentObject.y+"\r\n");
    Io.writeToDebug("theta: "+mostRecentObject.theta+"\r\n"
    
    
    
    );
    return returnflag;
    
    
        
 }
}