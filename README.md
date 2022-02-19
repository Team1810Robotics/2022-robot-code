# 2022 Robot

### Team 1810 Robotics Swerve drive code for the 2022 competition season
**Named `Swiffer`**
---

* Uses Swerve Drive Specialties's MK4 with L2 gear ratio with falcons as the motors.
    * (https://www.swervedrivespecialties.com/products/mk4-swerve-module?variant=39376675045489)

---

* Electrical / Programming Wiring Chart.
    * (https://docs.google.com/spreadsheets/d/1KCYpCz1mNoaCfkUa-aLaaVEg1ivP0he2kCzS-OsVOjM/edit?usp=sharing)

    It is restricted so only added people can open the sheets.

---

**CAN**

| ID    | Mechanism                   | Being Controlled | Controller | Port  |
| ----- | -----                       | -----            | -----      | ----- |
| 01    | Drive - Front Left Drive    | 1 Falcon         | TalonFX    |       |
| 02    | Drive - Front Left Steer    | 1 Falcon         | TalonFX    |       |
| 03    | Drive - Front Right Drive   | 1 Falcon         | TalonFX    |       |
| 04    | Drive - Front Right Steer   | 1 Falcon         | TalonFX    |       |
| 05    | Drive - Back Right Drive    | 1 Falcon         | TalonFX    |       |
| 06    | Drive - Back Right Steer    | 1 Falcon         | TalonFX    |       |
| 07    | Drive - Back Left Drive     | 1 Falcon         | TalonFX    |       |
| 08    | Drive - Back Left Steer     | 1 Falcon         | TalonFX    |       |
|       |~~Turret - Turret Rotation~~ |~~1 Neverest~~    |~~Talon~~   |       |
|       | Shooter - Shooter On/Off    | 1 NEO            | SPARK MAX  |       |
|       | Climb - Right Winch         | 1 NEO 550        | SPARK MAX  |       |
|       | Climb - Left Winch          | 1 NEO 550        | SPARK MAX  |       |

**Relay**

| Port |  Mechanism            | Being Controlled | Controller     |
| -----| -----                 | -----            | -----          |
| 00   | Intake - Left Intake  | 2 JE             | Relay Spike    |
| 01   | Intake - Right Intake | 2 JE             | Relay Spike    |
| 02   | Hood - Hood Movement  | 1 Snowblower     | Relay Spike    |
| 03   | Elevator - Auger      | 1 *TBD*          | Relay Spike    |

**PWM**

| Port  | Mechanism           | Being Controlled | Controller  |
| ----- | -----               | -----            | -----       |
| 00    | Climb - Climb Servo | 2 Servo          | Relay Spike | 
| 01    |                     |                  |             | 
| 02    |                     |                  |             | 
| 03    |                     |                  |             | 
| 04    |                     |                  |             | 
| 05    |                     |                  |             | 
| 06    |                     |                  |             | 
| 07    |                     |                  |             | 
| 08    |                     |                  |             | 
| 09    |                     |                  |             | 
<br>

| DIO   |       |
| ----- | ----- |
| 00    |       |
| 01    |       | 
| 02    |       | 
| 03    |       |
| 04    |       | 
| 05    |       | 
| 06    |       | 
| 07    |       | 
| 08    |       | 
| 09    |       |
<br>

| Analog ln | Mechanism |
| -----     | -----     |
| 00        |           |
| 01        |           |
| 02        |           |
| 03        |           |
