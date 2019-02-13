package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Manip {

    long statetime;
    FiniteStates nowstate = FiniteStates.IDLE;
    int  DEFAULTTIME = 5000;

public void runtime(){
    shooter();//Compute next state
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
public void shooter(){
    long now=System.currentTimeMillis();
switch(nowstate){
case IDLE:
    if(Io.driveStick.getRawButton(Io.FIRE_BUTTON))
    changeState(FiniteStates.FIREINGA);
    else if (Io.elevatorStick.getRawButton(Io.INTAKE_BUTTON))
    changeState(FiniteStates.PICKUP);
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
if (Io.elevatorStick.getRawButton(Io.INTAKE_BUTTON) != pressed)
    changeState(FiniteStates.IDLE);
break;
case RESET:
    if ((now-statetime>DEFAULTTIME)&&(!Io.elevatorStick.getRawButton(Io.FIRE_BUTTON)))
    changeState(FiniteStates.IDLE);
break;
}
}

    public void todolist() {
        boolean on = true;
        boolean off = false;
    switch(nowstate){
    case IDLE:
        System.out.println("In IDLE");
    break;
    case FIREINGA:
        Io.lasthope.set(on);
    break;
    case FIREINGB:
        Io.shoot1.set(on);

        Io.intake.set(1);
    break;
    case PICKUP:
        Io.intake.set(-1);
    break;
    case LOADED:
        Io.intake.set(0);
    break;
    case RESET:
        Io.lasthope.set(off);
        Io.shoot1.set(off);
        Io.intake.set(0);
    break;

        }
    }
}