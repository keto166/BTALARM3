#include "ColorTool.h"
char c;
int colorpin[6] = {5, 6, 3, 9, 10, 11};
int colorval[6];
int colormap[6];
int alarm_color[6]= {255, 255, 255, 255, 255, 255};
String code;
unsigned long day_time = 78300000;                        //The current time of day, in ms
unsigned long alarm_time = 19800000;           //The wake up time, in ms
unsigned long alarm_duration= 1800000;        //The alarm length, in ms
unsigned long previous_time;                  //Last recorded time, in ms
unsigned long current_time;                    //current time var
boolean alarm_set = true;                      //true if the alarm is set
boolean alarm_started = false;              //true if the alarm has been started for today
boolean alarm_dismissed = false;              //true if the alarm has been dismissed for today
double dbl_temp1;
double dbl_temp2;
int int_temp1;
boolean talkative = false;                  //true to allow output

ColorTool light1;
ColorTool light2;
// Blu light 1 = pin 3
// Red light 1 = pin 5
// Grn light 1 = pin 6
// Red light 2 = pin 9
// Grn light 2 = pin 10
// Blu light 2 = pin 11

void setup()
{
  Serial.begin(9600);   //Sets the baud for serial data transmission
  for (int i = 0; i < 6; i++) {
    pinMode(colorpin[i], OUTPUT);
    colorval[i] = 0;
  }
}


void loop() {
  //if color sync available from serial port, update color
  if (Serial.available() >=  19) {
    if (alarm_started) {
      alarm_dismissed = true;
    }
    c = Serial.read();
    if ( c == 'S'  ) {
      light1.setup0();
      code = "S";
      for (int n = 0; n < 18; n++) {
        c = Serial.read();
        code += c;
      }


      for (int i = 0; i < 6; i++) {
        colorval[i] = 0;
        for (int j = 0; j < 3; j++) {
          //Check for numerical value or error
          colorval[i] = (10 * colorval[i]) + (code.charAt(3 * i + j + 1) - '0');
        }

        if (colorval[i] > 255) {
          colorval[i] = 255;
          //ERROR HANDLING
        } else if (colorval[i] < 0) {
          colorval[i] = 0;
          //ERROR HANDLING
        }

        analogWrite(colorpin[i], colorval[i]);
      }
      spit_out("X" + code + "Z");
      //Return code for success
      while (Serial.available()) {
        Serial.read();
      }
    } else if (c == 'P') {
      for (int i = 0; i < 3; i++) {
        analogWrite(colorpin[i], 0);
      }
      int type = 0;
      bool b = false;
      light1.setup0();
      light2.setup0();
      code = "P";

      for (int i = 0; i < 9; i++) {
        c = Serial.read();
        code += c;

        if (!b && c != '1' && c != '2' && c != '3') {
          //ERROR
        }

        if (!b && c != '0' && i < 3) {
          type = i + 1;
        } else {
          b = true;
        }
      }

      switch (type) {
        case 1:
          colormap[0] = (code.charAt(1) - '0') - 1;
          light1.setup1(colorpin[colormap[0]]);
          break;
        case 2:
          colormap[0] = (code.charAt(1) - '0') - 1;
          colormap[1] = (code.charAt(2) - '0') - 1;
          light1.setup2(colorpin[colormap[0]], colorpin[colormap[1]]);
          break;
        case 3:
          colormap[0] = (code.charAt(1) - '0') - 1;
          colormap[1] = (code.charAt(2) - '0') - 1;
          colormap[2] = (code.charAt(3) - '0') - 1;
          light1.setup3(colorpin[colormap[0]], colorpin[colormap[1]], colorpin[colormap[2]]);
          break;
        default:

          break;
      }

      spit_out("X" + code + "Z");
      while (Serial.available()) {
      Serial.read();
      }

    } else if (c == 'A') {
      //time mode
      c = Serial.read();
      if (c == '1') {
        alarm_set = true;
      }
      day_time = 0;
      for (int i = 0; i < 5; i++) {
          //Check for numerical value or error
          c = Serial.read();
          day_time = (10 * day_time) + (c - '0');
        }
      day_time = day_time * 1000;

      alarm_time = 0;
      for (int i = 0; i < 5; i++) {
          //Check for numerical value or error
          c = Serial.read();
          alarm_time = (10 * alarm_time) + (c - '0');
        }
      alarm_time = alarm_time * 1000;

      alarm_duration = 0;
      for (int i = 0; i < 5; i++) {
          //Check for numerical value or error
          c = Serial.read();
          alarm_duration = (10 * alarm_duration) + (c - '0');
        }
      alarm_duration = alarm_duration * 1000;
      
      while (Serial.available()) {
        Serial.read();
      }
    } else if ( c == 'B'  ) {
      //Setting the alarm colors
      for (int n = 0; n < 18; n++) {
        c = Serial.read();
        code += c;
      }


      for (int i = 0; i < 6; i++) {
        alarm_color[i] = 0;
        for (int j = 0; j < 3; j++) {
          //Check for numerical value or error
          alarm_color[i] = (10 * alarm_color[i]) + (code.charAt(3 * i + j + 1) - '0');
        }

        if (alarm_color[i] > 255) {
          alarm_color[i] = 255;
          //ERROR HANDLING
        } else if (alarm_color[i] < 0) {
          alarm_color[i] = 0;
          //ERROR HANDLING
        }
      }
      spit_out("X" + code + "Z");
      //Return code for success
      while (Serial.available()) {
        Serial.read();
      }
    } else {
      //c = Serial.read();
    }


  }





  
  
  current_time = millis();
  
  day_time += (current_time - previous_time);
  previous_time = current_time;
  if (day_time > 86400000) {
    day_time -= 86400000;
    alarm_dismissed = false;
    alarm_started = false;
  }

  if (  alarm_set &&
        !alarm_dismissed &&
        day_time > alarm_time &&
        (day_time - alarm_time) < alarm_duration) {
    
    alarm_started = true;
    light1.setup0();
    light2.setup0();

    dbl_temp1 = (double) ((day_time - alarm_time)) / alarm_duration;
    dbl_temp2 = pow(dbl_temp1, 4);
      
    for (int i = 0; i < 6; i++) {
      int_temp1 = (int) alarm_color[i]*dbl_temp2;
      analogWrite(colorpin[i], int_temp1);
      spit_out("Alarm Running!");
      //spit_out(dbl_temp1);
      //spit_out(dbl_temp2);
//      spit_out(int_temp1);
    }
  }

  light1.process();
  light2.process();

}

void spit_out(String s) {
  if (talkative) {
    spit_out(s);
  }
}




