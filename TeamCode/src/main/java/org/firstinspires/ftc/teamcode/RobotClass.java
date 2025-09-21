package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

public class RobotClass {
    public DcMotorEx gun1Fire, gun2Fire, gun3Fire, gun4Fire, gun5Fire, gun6Fire;
    public Servo gun1Fill, gun2Fill, gun3Fill, gun4Fill, gun5Fill, gun6Fill;

    public RobotClass(HardwareMap hardwareMap){

        // left side of robot
        // Motors
        gun1Fire = hardwareMap.get(DcMotorEx.class, "gun1Fire");
//        gun2Fire = hardwareMap.get(DcMotorEx.class, "gun2Fire");
//        gun3Fire = hardwareMap.get(DcMotorEx.class, "gun3Fire");
//
//        // Servos
        gun1Fill = hardwareMap.get(Servo.class, "gun1Fill");
//        gun2Fill = hardwareMap.get(Servo.class, "gun2Fill");
//        gun3Fill = hardwareMap.get(Servo.class, "gun3Fill");
//
//        // right side of robot
//        // Motors
//        gun4Fire = hardwareMap.get(DcMotorEx.class, "gun4Fire");
//        gun5Fire = hardwareMap.get(DcMotorEx.class, "gun5Fire");
//        gun6Fire = hardwareMap.get(DcMotorEx.class, "gun6Fire");
//
//        // Servos
//        gun4Fill = hardwareMap.get(Servo.class, "gun4Fill");
//        gun5Fill = hardwareMap.get(Servo.class, "gun5Fill");
//        gun6Fill = hardwareMap.get(Servo.class, "gun6Fill");

        // robot configuration for test chassis currently commented out
        // backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        // frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

    }

}
