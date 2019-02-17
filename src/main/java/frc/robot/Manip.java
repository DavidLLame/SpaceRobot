package frc.robot;


import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Manip {

    long statetime;
    FiniteStates nowstate = FiniteStates.IDLE;
    int  DEFAULTTIME = 5000;

public void runtime(){
    //shooter();//Compute next state
    stateTransition();
    todolist();//Set actuators
}
    public void changeState(FiniteStates newstate){     
    nowstate = newstate;
    statetime = System.currentTimeMillis();
    }    

    /**
     * Finite state machine for pickup and fire mechanism.
     * Compute next state
     */
/*public void shooter(){
    System.out.println(nowstate.toString());
    long now=System.currentTimeMillis();
switch(nowstate){
case IDLE:
    if(UserCom.primaryFire())
    changeState(FiniteStates.FIREINGA);
 //  else if (UserCom.intakeMotorOn())
 //   changeState(FiniteStates.PICKUP);
break;
case FIREINGA:
    if (now-statetime>DEFAULTTIME)
    changeState(FiniteStates.FIREINGB);
break;
case FIREINGB:
    if (now-statetime>DEFAULTTIME)
    changeState(FiniteStates.RESET);
break;
case PICKUP:
    if (now-statetime>DEFAULTTIME)
    changeState(FiniteStates.LOADED);
break;
case LOADED:
boolean pressed = true;
if (!UserCom.intakeMotorOn())
    changeState(FiniteStates.IDLE);
break;
case RESET:
    if ((now-statetime>DEFAULTTIME)&&(!UserCom.primaryFire()))
    changeState(FiniteStates.IDLE);
break;
}
}
*/

public void stateTransition()
{
    long now=System.currentTimeMillis();
    switch (nowstate)
    {
        case IDLE:
           if (UserCom.intakeMotorOn())
           {
               changeState(FiniteStates.PICKUPA);
           }
           break;
        case PICKUPA:
        if (now-statetime>2000)
        {
                changeState(FiniteStates.PICKUPB);
        }
        break;
        case PICKUPB:
        if (now-statetime>2000)
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
        if (now-statetime>1000)
        {
            changeState(FiniteStates.LOADED);
        }
    
        case LOADED:
        if (UserCom.primaryFire())
        {
            changeState(FiniteStates.FIREINGA);
        }
        break;
        case FIREINGA:
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
    break;
    case FIREINGA:
        Io.lasthope.set(on);
        Io.shoot1.set(true);
        Io.intake.set(-1);
    break;
    case RESET:
        Io.lasthope.set(off);
        Io.shoot1.set(false);
        Io.intake.set(0);
    break;

        }
    }
}