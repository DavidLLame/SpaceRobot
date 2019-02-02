package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Manip {

    long statetime;
    FiniteStates nowstate = FiniteStates.IDLE;
    int  DEFAULTTIME = 5000;

public void runtime(){
    shooter();
    todolist();
}
    public void changeState(FiniteStates newstate){     
    nowstate = newstate;
    statetime = System.currentTimeMillis();
    }    

public void shooter(){
    long now=System.currentTimeMillis();
switch(nowstate){
case IDLE:
    if(Io.joystick.getRawButton(10))
    changeState(FiniteStates.FIREINGA);
    else if (Io.joystick.getRawButton(11))
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
if (Io.joystick.getRawButton(11) != pressed)
    changeState(FiniteStates.IDLE);
break;
case RESET:
    if (now-statetime>DEFAULTTIME)
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