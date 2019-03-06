package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manip {


    public double CURRENTSPIKETHRESHOLD=60; //Above this value, the ball will be considered "stuck" during intake


    long statetime;
    FiniteStates nowstate = FiniteStates.IDLE;
   
public void Init()
{
    nowstate=FiniteStates.IDLE;
}

public void runtime(){
    //shooter();//Compute next state
    stateTransition();
    todolist();//Set actuators
}
    public void changeState(FiniteStates newstate){     
    nowstate = newstate;
    statetime = System.currentTimeMillis();
    }    



public void stateTransition()
{
    long now=System.currentTimeMillis();
    SmartDashboard.putNumber("Channel 12 PDP", Io.pdp.getCurrent(Io.INTAKE_PDP_CHANNEL));
    SmartDashboard.putString("Manip State",nowstate.toString());
    SmartDashboard.putNumber("Intake Motor command", Io.intake.get());
    //Sequence of events:
    //intake on.
    //Wait for timeout or sensor
    //push firing arm, continue rolling
    //pause briefly
    //Roll again

    switch (nowstate)
    {
        case IDLE:
           if (UserCom.intakeButtons())
           {
               changeState(FiniteStates.PICKUPA);
           }
           else if (UserCom.primaryFire())
           {
               changeState(FiniteStates.FIRINGA);
           }
           else if (UserCom.hatchPickup())
           {
               changeState(FiniteStates.HATCHLOADED);
           }
           else if (UserCom.resetCarriageState())
           {
               changeState(FiniteStates.RESET);
           }
           break;
        case PICKUPA: //Roll in until either 2 secs, or user stops, or high current detected
        if ((!UserCom.intakeButtons())||(Io.pdp.getCurrent(Io.INTAKE_PDP_CHANNEL)>CURRENTSPIKETHRESHOLD)) //Add || sensor triggered
        {
                changeState(FiniteStates.PICKUPB);
        }
        break;
        case PICKUPB://put the piston in place, and roll until time expires
        if (now-statetime>1000)
        {
            changeState(FiniteStates.PICKUPC);
        }
        break;
        case PICKUPC:
        if (now-statetime>500)
        {
            changeState(FiniteStates.PICKUPD);
        }
break;
        case PICKUPD:
        if (now-statetime>600)
        {
            changeState(FiniteStates.LOADED);
        }
    break;
        case LOADED:
        if (UserCom.primaryFire())
        {
            changeState(FiniteStates.FIRINGA);
        }
        else if (UserCom.resetCarriageState())
        {
            changeState(FiniteStates.RESET);
        }
        break;
        case FIRINGA:
        if (!UserCom.primaryFire())
        {
            changeState(FiniteStates.RESET);
        }
        break;
        case RESET:
        if (now-statetime>100)
        {
            changeState(FiniteStates.IDLE);
        }
        break;
        case HATCHLOADED:
        {
            if (UserCom.primaryFire())
            {
                changeState(FiniteStates.HATCHFIRING);
            }
            else if (UserCom.resetCarriageState())
            {
                changeState(FiniteStates.RESET);
            }
        }
        break;
        case HATCHFIRING:
        {
            if ((now - statetime) >1000)
            {
                changeState(FiniteStates.RESET);
            }
        }
        break;


        
    }
}

    public void todolist() {
        boolean on = true;
        boolean off = false;
    switch(nowstate){
    case IDLE:
        System.out.println("In IDLE");
        Io.intake.set(0);
    break;
    
    case PICKUPA:
        Io.intake.set(1);
    break;
    case PICKUPB:
        Io.lasthope.set(true);
        Io.intake.set(1);

    break;
    case PICKUPC:
    Io.intake.set(0);
    break;
    case PICKUPD:
    Io.intake.set(1);
    break;
    case LOADED:
        Io.intake.set(0);
        Io.lasthope.set(true);
        Io.shoot1.set(false);
    break;
    case FIRINGA:
        Io.lasthope.set(true);
        Io.shoot1.set(true);
        Io.intake.set(-1);
    break;
    case RESET:
        Io.lasthope.set(false);
        Io.shoot1.set(false);
        Io.intake.set(0);
    break;
    case HATCHLOADED:
        Io.lasthope.set(true);
        Io.intake.set(0);
        Io.shoot1.set(false);
        break;
    case HATCHFIRING:
        Io.lasthope.set(true);
        Io.shoot1.set(true);
        Io.intake.set(0);
        break;

        }
    }
}