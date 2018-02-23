/**
*  Tinker Clone
*
*  Copyright 2018 Jmaxxz
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License. You may obtain a copy of the License at:
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.
*
*/
char m_devhandler[] = "Particle Tinker";

char m_current[128];
int _analogRead(String cmd);
int _analogWrite(String cmd);
int _digitalRead(String cmd);
int _digitalWrite(String cmd);

void setup()
{
  strcpy(m_current, "{}");

  // Smartthings integrations
  Particle.variable("devhandler", m_devhandler, STRING);

  // Standard tinker functions
  Particle.function("analogread", _analogRead);
  Particle.function("analogwrite", _analogWrite);
  Particle.function("digitalread", _digitalRead);
  Particle.function("digitalwrite", _digitalWrite);
}

void loop() {
}

int _analogRead(String cmd){
  if(strlen(cmd) < 2){
    return -1;
  }
  uint8_t pin = getPin(cmd);
  if(pin < 0 || pin > 17){
    return -2;
  }
  pinMode(pin, INPUT_PULLDOWN);
  return analogRead(pin);
}
int _analogWrite(String cmd){
  if(strlen(cmd) < 4){
    return -1;
  }
  uint8_t pin = getPin(cmd);
  if(pin < 0 || pin >17){
    return -2;
  }

  int value = cmd.substring(3).toInt();
  if(value < 0){
    return -3;
  }

  pinMode(pin, OUTPUT);
  analogWrite(pin, value);
  return 0;
}
int _digitalRead(String cmd){
  if(strlen(cmd) < 2){
    return -1;
  }
  uint8_t pin = getPin(cmd);
  if(pin < 0 || pin > 17){
    return -2;
  }

  pinMode(pin, INPUT_PULLDOWN);
  return digitalRead(pin);
}

int _digitalWrite(String cmd){
  int length = strlen(cmd);
  if(length != 6 && length != 7){
    return -1;
  }
  uint8_t pin = getPin(cmd);
  if(pin < 0 || pin >17){
    return -2;
  }
  pinMode(pin, OUTPUT);
  if(length == 7){ //High
    digitalWrite(pin, HIGH);
    publishDigitalWrite(cmd.substring(0, 2), 1);
  } else {
    digitalWrite(pin, LOW);
    publishDigitalWrite(cmd.substring(0, 2), 0);
  }
  return 0;
}

void publishAnalogWrite(String pin, double value){
  sprintf(m_current, "{\"t\":\"aw\",\"p\":\"%s\",\"v\":%d}", pin, value);
  Particle.publish("state-update", m_current, 60, PRIVATE);
}

void publishDigitalWrite(String pin, int value){
  sprintf(m_current, "{\"t\":\"dw\",\"p\":\"%s\",\"v\":%d}", pin, value);
  Particle.publish("state-update", m_current, 60, PRIVATE);
}


uint8_t getPin(String cmd){
  uint8_t pin = 0;
  if(cmd[0] =='a' || cmd[0] == 'A'){
    pin = 10;
  }
  pin += cmd[1] - '0';

  return pin;
}
