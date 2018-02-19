/**
*  Particle Fan Controller
*
*  Copyright 2018 Jmaxxz
*
* MIT License
* Copyright (c) 2018 [Jmaxxz](jmaxxz.com)
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
*/

const uint8_t dip1 = D3;
const uint8_t dip2 = A2;
const uint8_t dip3 = D4;
const uint8_t dip4 = D5;
const uint8_t dip5 = D6;

const uint8_t offBtn = D1;
const uint8_t lightBtn = D2;
const uint8_t lowBtn = A0;
const uint8_t highBtn = A1;
const uint8_t mediumBtn = D0;

int rawAddress = 0;
char m_devhandler[] = "Particle Fan";
char m_fanAddress[6];

void onboot() {
  //Set pin modes
  pinMode(dip1, OUTPUT);
  pinMode(dip2, OUTPUT);
  pinMode(dip3, OUTPUT);
  pinMode(dip4, OUTPUT);
  pinMode(dip5, OUTPUT);
  pinMode(offBtn, INPUT);
  pinMode(lightBtn, INPUT);
  pinMode(highBtn, INPUT);
  pinMode(lowBtn, INPUT);
  pinMode(mediumBtn, INPUT);
  uint8_t fanAddress = EEPROM.read(0);
  rawAddress = fanAddress;
  m_fanAddress[0] = (fanAddress & 0b00000001) == 0 ? '0' : '1';
  m_fanAddress[1] = (fanAddress & 0b00000010) == 0 ? '0' : '1';
  m_fanAddress[2] = (fanAddress & 0b00000100) == 0 ? '0' : '1';
  m_fanAddress[3] = (fanAddress & 0b00001000) == 0 ? '0' : '1';
  m_fanAddress[4] = (fanAddress & 0b00010000) == 0 ? '0' : '1';
  m_fanAddress[5] = 0;

  resetToDefault();
}

STARTUP(onboot());

void setup() {

  Particle.variable("rawAddress", rawAddress);
  Particle.variable("devhandler", m_devhandler, STRING);
  Particle.variable("fanAddress", m_fanAddress, STRING);
  // Initialize cloud functions
  Particle.function("cmd", cmd);
}

void loop() {
}

void pressButton(uint8_t button){
  pinMode(button, OUTPUT);
  digitalWrite(button, LOW);
  delay(800);
  pinMode(button, INPUT);
}

void pressButton(uint8_t button, String address){
  if(strlen(address) < 5){
    pressButton(button);
    return;
  }

  digitalWrite(dip1, address[0] == '0'? LOW : HIGH);
  digitalWrite(dip2, address[1] == '0'? LOW : HIGH);
  digitalWrite(dip3, address[2] == '0'? LOW : HIGH);
  digitalWrite(dip4, address[3] == '0'? LOW : HIGH);
  digitalWrite(dip5, address[4] == '0'? LOW : HIGH);
  pressButton(button);
}

int changeDefault(String address){
  if(strlen(address) < 5){
    return -1;
  }
  m_fanAddress[0] = address[0] == '0'? '0' : '1';
  m_fanAddress[1] = address[1] == '0'? '0' : '1';
  m_fanAddress[2] = address[2] == '0'? '0' : '1';
  m_fanAddress[3] = address[3] == '0'? '0' : '1';
  m_fanAddress[4] = address[4] == '0'? '0' : '1';
  resetToDefault();

  int fanAddress = ((m_fanAddress[4]=='0' ? 0 : 1) << 4);
  fanAddress |= ((m_fanAddress[3]=='0' ? 0 : 1) << 3);
  fanAddress |= ((m_fanAddress[2]=='0' ? 0 : 1) << 2);
  fanAddress |= ((m_fanAddress[1]=='0' ? 0 : 1) << 1);
  fanAddress |= m_fanAddress[0]=='0' ? 0 : 1;
  rawAddress = (uint8_t)fanAddress;
  EEPROM.write(0, (uint8_t)fanAddress);
  return 0;
}

void resetToDefault(){
  digitalWrite(dip1, m_fanAddress[0] == '0'? LOW : HIGH);
  digitalWrite(dip2, m_fanAddress[1] == '0'? LOW : HIGH);
  digitalWrite(dip3, m_fanAddress[2] == '0'? LOW : HIGH);
  digitalWrite(dip4, m_fanAddress[3] == '0'? LOW : HIGH);
  digitalWrite(dip5, m_fanAddress[4] == '0'? LOW : HIGH);
}

int cmd(String command){
  int length = strlen(command);
  if(length < 1){
    return -1;
  }

  char t = command[0];
  switch(t) {
    case 'b':
    //toggle light
    pressButton(lightBtn, command.substring(1));
    break;

    case 'l':
    // low speed
    pressButton(lowBtn, command.substring(1));
    break;

    case 'm':
    // medium speed
    pressButton(mediumBtn, command.substring(1));
    break;

    case 'h':
    // high speed
    pressButton(highBtn, command.substring(1));
    break;

    case 'o':
    // turn off fan
    pressButton(offBtn, command.substring(1));
    break;

    case 'd':
    // change default fan address
    return changeDefault(command.substring(1));
    break;

    default:
    return -2;
  }
  return 0;
}
