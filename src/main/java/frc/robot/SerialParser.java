package frc.robot;

public class SerialParser
{
    public boolean checkForInstructions(Drive drive)
    {
        String cameraInstructions=Io.jevoisPort.readString();
        if (cameraInstructions!=null)
        {
            System.out.println("From jevois: "+cameraInstructions);
        }
        if ((cameraInstructions==null)||(cameraInstructions.length()==0))
        {
            return false;
        }
        else
        {
//String[] stringPieces=cameraInstructions.split(" ");
 //           if (stringPieces[0].equals("Ball"))
               if (cameraInstructions.startsWith("Ball"))
            {
                //double angleFound=Double.parseDouble(stringPieces[1]);
                double angleFound=31.1;
                drive.driveToAngle(angleFound);
                return true;
            }
            else return false;
        }


    }
}