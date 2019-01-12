package frc.robot;

/**
 * The class for all driving related functions
 */
public class Drive{

    //Notice that class names start with capital letters.
    //Members of the class (variables and methods) start with a lower case letter, but subsequent words have an upper case letter.
    //The initial lower case followed by upper case words is called "camel case"
    //constants are all upper case.
    //Those casing "rules" aren't requirements.  They're just conventions that most Java programmers use.
    //More people will be able to understand your code if you follow the same patterns.

    //Of course, conventions like those just happen when somebody writes a book saying it's a good idea,
    //and a lot of people use the book.  So, some day, someone else will write a different book and people
    //will start doing it the way that book says.  Also, those conventions are only standard for Java, not
    //for C#.  The C# programmers read different books.

    /**
     * This function is called to do "normal", teleperated, driving.
     */
    public void driveByJoystick()
    {
       //Io.leftDriveMotor.set(Io.leftJoystick.getRawAxis(1));  //Notice the things that come from Io are referenced using the class name.
                                                            //Static variables are common to the class so, you use the class name to reference them.
       //Io.rightDriveMotor.set(Io.rightJoystick.getRawAxis(1));

      //Notice also that if you compare this code to last year's code, last year this class had an instance
      //of Joystick inside it, and it got initialized in the class constructor.  This year, we are calling
      //the static member of the Io class.
      //Neither way is "right"  or "wrong".  Both do the same thing.  Which one to use depends on a lot of things
      //like whether you intend to reuse the code somewhere else, versus the chance of making a mistake if code has
      //to change right now, very fast.  For us, we won't reuse the code, and we may very well have to make 
      //changes in a very short time.  If we do it this way, there's a slightly smaller chance of error.

      //It really comes down to personal preference and whatever the team agrees to.

      
      
      Io.meccDrive.driveCartesian(Io.joystick.getRawAxis(0), -1 * Io.joystick.getRawAxis(1), Io.joystick.getRawAxis(0),Io.joystick.getRawAxis(0));


    }

    /**
     * This function is used to drive under computer control, and set the values of the
     * motors based on desired speed and rotation.
     * @param desiredSpeed - The forward rate of motion, -1 to 1
     * @param desiredRotationRate -The turn rate, -1 to 1
     */
    public void driveAutonomous(double desiredSpeed, double desiredRotationRate)
    {
        //Calculate the desired motor speeds based on the desired input speeds
    }

}