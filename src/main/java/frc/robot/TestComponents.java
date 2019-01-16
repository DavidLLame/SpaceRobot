package frc.robot;

public class TestComponents
{

    long stateTransitionTime;
    TestFiniteStates currentState = TestFiniteStates.IDLE;
     final int  DEFAULTSTATETIME = 2000;

    private void changeState(TestFiniteStates newstate)
    
    {
        stateTransitionTime = System.currentTimeMillis();
        currentState = newstate;
}

public void operateTest()
{
    ComputeNewState();
    setOutputs();
}

private void ComputeNewState()
{

    long now=System.currentTimeMillis();
    switch(currentState)
    {
         case IDLE:
            changeState(TestFiniteStates.DRIVELEFTFRONT);
            break;
         case   DRIVELEFTFRONT:
            if (now-stateTransitionTime>DEFAULTSTATETIME)
            changeState(TestFiniteStates.DRIVELEFTREAR);
            break;
         case   DRIVELEFTREAR:
            if (now-stateTransitionTime>DEFAULTSTATETIME)
            changeState(TestFiniteStates.DRIVERIGHTFRONT);
            break;
         case   DRIVERIGHTFRONT:
            if (now-stateTransitionTime>DEFAULTSTATETIME)
            changeState(TestFiniteStates.DRIVERIGHTREAR);
            break;
         case   DRIVERIGHTREAR:
            if (now-stateTransitionTime>DEFAULTSTATETIME)
            changeState(TestFiniteStates.DRIVEMECANUM);
            break;
         case   DRIVEMECANUM:
            if (now-stateTransitionTime>DEFAULTSTATETIME)
            changeState(TestFiniteStates.STOP);
            break;
        case   STOP:

            break;
    }
}

private void setOutputs()
{
   switch(currentState)
   {
       case DRIVELEFTFRONT:
           Io.frontLeftMotor.set(1);
           Io.frontRightMotor.set(0);
           Io.rearLeftMotor.set(0);
           Io.rearRightMotor.set(0);
           break;
       case DRIVELEFTREAR:
           Io.frontLeftMotor.set(0);
           Io.frontRightMotor.set(0);
           Io.rearLeftMotor.set(1);
           Io.rearRightMotor.set(0);
           break;
       case DRIVERIGHTFRONT:
           Io.frontLeftMotor.set(0);
           Io.frontRightMotor.set(1);
           Io.rearLeftMotor.set(0);
           Io.rearRightMotor.set(0);
           break;
       case DRIVERIGHTREAR:
          Io.frontLeftMotor.set(0);
           Io.frontRightMotor.set(0);
           Io.rearLeftMotor.set(0);
           Io.rearRightMotor.set(1);
           break;
       case DRIVEMECANUM:
           Io.frontLeftMotor.set(1);
           Io.frontRightMotor.set(1);
           Io.rearLeftMotor.set(1);
           Io.rearRightMotor.set(1);
           break;
       case STOP:
           Io.frontLeftMotor.set(0);
           Io.frontRightMotor.set(0);
           Io.rearLeftMotor.set(0);
           Io.rearRightMotor.set(0);
           break;
   }
}
}