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
        attribute "pinName", "string"
    }


    simulator {
        // TODO: define status and reply messages here
    }

    tiles {
        standardTile("switch", "device.switch") {
            state "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"off"
            state "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"on"
        }
        valueTile("name", "pinName") {
            state "default", label: '${currentValue}'
        }
    }
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

def on() {
	parent.invoke("digitalwrite", "${device.name} HIGH")
    sendEvent(name: "switch", value: "on")
}

def off() {
	parent.invoke("digitalwrite", "${device.name} LOW")
    sendEvent(name: "switch", value: "off")
}