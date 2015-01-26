/*
 * KajiraDouBra.
 * Copyright (C) 2015 Ying-Chun Liu (PaulLiu) <paulliu@debian.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

#include <SoftwareSerial.h>

#define CMDMAXLEN 256

int hexCharToInt(char);
void handleSerialCommand();

const int pinLed = 13;
const int pinPWMLeft = 5;
const int pinPWMRight = 3;
SoftwareSerial BTSerial(10, 11); // (RX, TX)

struct _RepeatMode {
  char cmd[CMDMAXLEN];
  int cmdLen;
  int enabled;
  int pc;
  unsigned long time;
} repeatMode;


void setup() {
  // setup code, run once:
  pinMode(pinPWMLeft, OUTPUT);
  digitalWrite(pinPWMLeft, LOW);
  pinMode(pinPWMRight, OUTPUT);
  digitalWrite(pinPWMRight, LOW);
  repeatMode.enabled = (0==1);
  repeatMode.cmdLen = 0;
  Serial.begin(19200);
  pinMode(pinLed, OUTPUT);
  setupBT();
}

void loop() {
  // main code, run repeatedly: 
  if (BTSerial.available()) {
    handleSerialCommand();
  }
  blink();
  if (repeatMode.enabled) {
    runRepeatCmd();
  }
}

void setupBT() {
  BTSerial.begin(9600);
  BTSerial.println("");
  delay(1000);
  while (BTSerial.available()) {
    Serial.write(BTSerial.read());
  }
  BTSerial.print("AT");
  delay(1000);
  while (BTSerial.available()) {
    Serial.write(BTSerial.read());
  }
  Serial.println("");
  BTSerial.print("AT+VERSION");
  delay(1000);
  while (BTSerial.available()) {
    Serial.write(BTSerial.read());
  }
  Serial.println("");
  BTSerial.print("AT+NAMEKajiraDouBra");
  delay(1000);
  while (BTSerial.available()) {
    Serial.write(BTSerial.read());
  }
  Serial.println("");
}

int pinLedStatus = 0;
void blink() {
  unsigned long current_time = millis();
  unsigned long N=2000;
  if (current_time % N < (N/2) && pinLedStatus == 0) {
    digitalWrite(pinLed, HIGH);
    pinLedStatus = 1;
  } else if (current_time % N >= (N/2) && pinLedStatus == 1) {
    digitalWrite(pinLed, LOW);
    pinLedStatus = 0;
  }
}

char serialCommandBuffer[CMDMAXLEN];
int serialCommandBufferLen = 0;
void handleSerialCommand() {
  while (BTSerial.available()) {
    serialCommandBuffer[serialCommandBufferLen]=BTSerial.read();
    serialCommandBufferLen++;
    if (serialCommandBufferLen >= sizeof(serialCommandBuffer)/sizeof(serialCommandBuffer[0])) {
      serialCommandBufferLen = 0;
    }
    serialCommandBuffer[serialCommandBufferLen] = '\0';
    if (serialCommandBufferLen <= 0) {
      continue;
    }
    repeatMode.enabled = (0==1);
    if (serialCommandBuffer[0] == 'l') {
      if (serialCommandBufferLen >= 2) {
        char levelChar = serialCommandBuffer[1];
	int level = hexCharToInt(levelChar);
        analogWrite(pinPWMLeft, (level*255)/35);
        serialCommandBufferLen = 0;
        serialCommandBuffer[serialCommandBufferLen] = '\0';
      }
    } else if (serialCommandBuffer[0] == 'r') {
      if (serialCommandBufferLen >= 2) {
        char levelChar = serialCommandBuffer[1];
	int level = hexCharToInt(levelChar);
        analogWrite(pinPWMRight, (level*255)/35);
        serialCommandBufferLen=0;
        serialCommandBuffer[serialCommandBufferLen]='\0';
      }
    } else if (serialCommandBuffer[0] == '(') {
      if (serialCommandBufferLen >= 2 && serialCommandBuffer[serialCommandBufferLen-1] == ')' ) {
        if (serialCommandBufferLen < CMDMAXLEN) {
	  for (int i=1; i<serialCommandBufferLen-1; i++) {
	    repeatMode.cmd[i-1] = serialCommandBuffer[i];
	  }
	  repeatMode.cmdLen = serialCommandBufferLen-2;
	  repeatMode.cmd[repeatMode.cmdLen] = '\0';
	  repeatMode.pc = 0;
	  repeatMode.time = millis();
	  repeatMode.enabled = (1==1);
          serialCommandBufferLen=0;
          serialCommandBuffer[serialCommandBufferLen]='\0';
	}
      }
    } else {
      serialCommandBufferLen=0;
      serialCommandBuffer[serialCommandBufferLen]='\0';
    }
  }
}

void runRepeatCmd() {
  if (repeatMode.cmd[repeatMode.pc] == 'l' && repeatMode.pc+1<repeatMode.cmdLen) {
    int level = hexCharToInt(repeatMode.cmd[repeatMode.pc+1]);
    analogWrite(pinPWMLeft, (level*255)/35);
    repeatMode.time = millis();
    repeatMode.pc+=2;
  } else if (repeatMode.cmd[repeatMode.pc] == 'r' && repeatMode.pc+1<repeatMode.cmdLen) {
    int level = hexCharToInt(repeatMode.cmd[repeatMode.pc+1]);
    analogWrite(pinPWMRight, (level*255)/35);
    repeatMode.time = millis();
    repeatMode.pc+=2;
  } else if (repeatMode.cmd[repeatMode.pc] == 's' && repeatMode.pc+1<repeatMode.cmdLen) {
    unsigned long level = 100*hexCharToInt(repeatMode.cmd[repeatMode.pc+1]);
    unsigned long current_time = millis();
    if (current_time - repeatMode.time >= level || current_time < repeatMode.time) {
      repeatMode.time = current_time;
      repeatMode.pc+=2;
    }
  } else if (repeatMode.cmd[repeatMode.pc] == 'e') {
    repeatMode.enabled = (0==1);
    repeatMode.time = millis();
    repeatMode.pc++;
  } else {
    repeatMode.pc++;
    repeatMode.time = millis();
  }
  if (repeatMode.pc >= repeatMode.cmdLen) {
    repeatMode.pc = 0;
  }
}

int hexCharToInt(char c) {
  int ret=0;
  if ('0' <= c && c <= '9') {
    ret = c-'0';
  } else if ('A' <= c && c <= 'Z') {
    ret = c-'A'+10;
  }
  return ret;
}
