package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class RobotClass {
    // Drivetrain motors as CRServos
    public CRServo frontLeft, frontRight, middleLeft, middleRight, backLeft, backRight;

    // Public arrays to hold all gun motor and servo objects
    public DcMotorEx[] gunFireMotors = new DcMotorEx[6];
    public Servo[] gunFillServos = new Servo[6];

    public RobotClass(HardwareMap hardwareMap) {
        // Initialize Drivetrain
        frontLeft = hardwareMap.get(CRServo.class, "frontLeftSparkMax");
        frontRight = hardwareMap.get(CRServo.class, "frontRightSparkMax");
        middleLeft = hardwareMap.get(CRServo.class, "middleLeftSparkMax");
        middleRight = hardwareMap.get(CRServo.class, "middleRightSparkMax");
        backLeft = hardwareMap.get(CRServo.class, "backLeftSparkMax");
        backRight = hardwareMap.get(CRServo.class, "backRightSparkMax");

        // Use a loop to initialize all gun hardware components by name
        for (int i = 0; i < 6; i++) {
            String motorName = "gun" + (i + 1) + "Fire";
            gunFireMotors[i] = hardwareMap.get(DcMotorEx.class, motorName);

            String servoName = "gun" + (i + 1) + "Fill";
            gunFillServos[i] = hardwareMap.get(Servo.class, servoName);
        }
    }
}