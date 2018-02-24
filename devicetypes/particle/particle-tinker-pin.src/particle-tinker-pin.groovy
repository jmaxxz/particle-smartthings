/**
*  Particle Tinker
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
metadata {
    definition (name: "Particle Tinker Pin", namespace: "particle", author: "jmaxxz") {
        capability "Actuator"
        capability "Switch"
        capability "Switch Level"
        attribute "pinName", "string"
        attribute "miliVolts", "number"
        command "analogRead"
        command "writeMv"
        
    }


    simulator {
        // TODO: define status and reply messages here
    }

    tiles {
        standardTile("switch", "device.switch") {
            state "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"off"
            state "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"on"
        }
        controlTile("analogWrite", "miliVolts", "slider", inactiveLabel: false, range:"(0..3300)") {
            state "level", action:"writeMv"
        }
        valueTile("read", "device.level", decoration: "flat") {
            state "default", label: '${currentValue}V', action: "analogRead"
        }     
        valueTile("name", "pinName") {
            state "default", label: '${currentValue}'
        }
    }
}

def analogRead(){
    def result = parent.invokeForChild(this, "analogread", "${device.name}")
    sendEvent(name: "level", value: result * 0.0008)
}

def installed() {
    initialize()
    sendEvent(name: "pinName", value: "${device.name}")
}

def updated() {
    initialize()
    sendEvent(name: "pinName", value: "${device.name}")
}

def initialize() {
}

def writeMv(val){
	setLevel(val/1000.0)
}

def setLevel(correctedVal){
    def steps = (int)(correctedVal/0.0008)
	parent.invoke("analogwrite", "${device.name} ${steps}")
    sendEvent(name: "level", value: correctedVal)
    sendEvent(name: "miliVolts", value: correctedVal*1000.0)
    if(correctedVal <= 1.5) {
        sendEvent(name: "switch", value: "off")
    } else {
        sendEvent(name: "switch", value: "on")
    }
}

def on() {
    parent.invoke("digitalwrite", "${device.name} HIGH")
    sendEvent(name: "switch", value: "on")
}

def off() {
    parent.invoke("digitalwrite", "${device.name} LOW")
    sendEvent(name: "switch", value: "off")
}